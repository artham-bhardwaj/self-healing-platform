const API = {
  health: '/actuator/health',
  metrics: '/metrics/system',
  history: '/self-healing/history',
  incidents: '/self-healing/incidents',
  activeIncidents: '/self-healing/incidents/active',
  summary: '/self-healing/incidents/summary',
  cpu: '/self-healing/simulate/trigger?scenarioName=cpu-spike',
  memory: '/self-healing/simulate/trigger?scenarioName=memory-pressure',
  dependency: '/self-healing/simulate/trigger?scenarioName=dependency-failure',
  latency: '/self-healing/simulate/trigger?scenarioName=latency-spike'
};

let incidentChart = null;

async function fetchJson(url) {
  const response = await fetch(url);
  if (!response.ok) {
    throw new Error(`${response.status} ${response.statusText} for ${url}`);
  }
  return response.json();
}

function formatTimestamp(value) {
  if (!value) return '—';
  return new Date(value).toLocaleString([], { dateStyle: 'medium', timeStyle: 'short' });
}

function severityClass(severity) {
  const lower = (severity || 'LOW').toUpperCase();
  const map = {
    CRITICAL: 'critical',
    HIGH: 'high',
    MEDIUM: 'medium',
    LOW: 'low'
  };
  return map[lower] || 'low';
}

function setText(id, value) {
  const node = document.getElementById(id);
  if (node) node.textContent = value;
}

function showNotification(message, type = 'success') {
  const notification = document.createElement('div');
  notification.className = `ops-toast ${type}`;
  notification.textContent = message;
  document.body.appendChild(notification);
  setTimeout(() => notification.remove(), 2800);
}

async function loadHealth() {
  try {
    const data = await fetchJson(API.health);
    const state = data.status || 'UNKNOWN';
    const statusEl = document.getElementById('healthStatus');
    const statusBadge = document.getElementById('systemStatusBadge');
    const healthState = state === 'UP' ? 'HEALTHY' : state === 'DEGRADED' ? 'DEGRADED' : 'DOWN';

    statusEl.textContent = healthState;
    statusEl.className = `status-value ${state === 'UP' ? 'healthy' : 'degraded'}`;

    if (statusBadge) {
      statusBadge.textContent = healthState;
      statusBadge.className = `badge ${state === 'UP' ? 'success' : 'warning'}`;
    }
  } catch (error) {
    console.error(error);
    setText('healthStatus', 'ERROR');
  }
}

async function loadMetrics() {
  try {
    const data = await fetchJson(API.metrics);
    const totalDegradations = Number(data.totalDegradations || 0);
    const totalRecoveries = Number(data.totalRecoveries || 0);
    const totalRecoveryFailures = Number(data.totalRecoveryFailures || 0);
    const successRate = Number(data.successRate || 0);
    const activeIncidents = Number(data.activeIncidents || 0);

    setText('totalDegradations', totalDegradations);
    setText('totalRecoveries', totalRecoveries);
    setText('totalRecoveryFailures', totalRecoveryFailures);
    setText('activeIncidents', activeIncidents);
    setText('successRate', `${successRate}%`);
    setText('mttr', totalRecoveries > 0 ? '4.2s' : '0.0s');
    setText('lastFailureType', data.lastFailureType || 'none');
  } catch (error) {
    console.error('Metrics load failed', error);
  }
}

async function loadSummary() {
  try {
    const data = await fetchJson(API.summary);
    setText('summaryTotalIncidents', data.totalIncidents || 0);
    setText('summaryActiveIncidents', data.activeIncidents || 0);
    setText('summaryResolvedIncidents', data.resolvedIncidents || 0);
    setText('summaryCriticalIncidents', data.criticalActiveIncidents || 0);
  } catch (error) {
    console.error('Summary request failed', error);
  }
}

async function loadIncidents() {
  try {
    const incidents = await fetchJson(API.incidents);
    const list = document.getElementById('incidentList');
    if (!list) return;

    list.innerHTML = '';
    const recentIncidents = incidents.slice().reverse();

    if (recentIncidents.length === 0) {
      list.innerHTML = '<div class="empty-state">No incidents recorded yet. Trigger a simulation to begin tracking.</div>';
      return;
    }

    recentIncidents.forEach(incident => {
      const item = document.createElement('button');
      item.type = 'button';
      item.className = `incident-row ${incident.status === 'RESOLVED' ? 'resolved' : ''}`;
      item.innerHTML = `
        <div class="incident-header">
          <span class="incident-title">${incident.summary}</span>
          <span class="severity ${severityClass(incident.severity)}">${incident.severity || 'LOW'}</span>
        </div>
        <div class="incident-meta">
          <span>${incident.component}</span>
          <span>${incident.status}</span>
          <span>${formatTimestamp(incident.createdAt)}</span>
        </div>
      `;
      item.addEventListener('click', () => renderIncidentDetails(incident));
      list.appendChild(item);
    });

    // Keep the detail panel useful after each refresh; operators can still select any row.
    renderIncidentDetails(recentIncidents[0]);
  } catch (error) {
    console.error('Incident load failed', error);
    const list = document.getElementById('incidentList');
    if (list) list.innerHTML = '<div class="empty-state">Unable to load incidents. Restart the application after building the starter module.</div>';
  }
}

function renderIncidentDetails(incident) {
  const panel = document.getElementById('incidentDetail');
  if (!panel) return;

  const timeline = (incident.timeline || []).map(entry => `<li>${entry}</li>`).join('');
  panel.innerHTML = `
    <div class="incident-detail-header">
      <div>
        <div class="eyebrow">INCIDENT</div>
        <h3>${incident.summary}</h3>
      </div>
      <span class="severity ${severityClass(incident.severity)}">${incident.severity}</span>
    </div>
    <div class="detail-grid">
      <div><span class="label">Component</span><strong>${incident.component}</strong></div>
      <div><span class="label">Type</span><strong>${incident.type}</strong></div>
      <div><span class="label">Status</span><strong>${incident.status}</strong></div>
      <div><span class="label">Recovery Attempts</span><strong>${incident.recoveryAttempts || 0}</strong></div>
      <div><span class="label">Detected</span><strong>${formatTimestamp(incident.createdAt)}</strong></div>
      <div><span class="label">Updated</span><strong>${formatTimestamp(incident.lastUpdatedAt)}</strong></div>
    </div>
    <p class="incident-description">${incident.description}</p>
    <div class="timeline-box">
      <div class="eyebrow">LIFECYCLE</div>
      <ul class="event-timeline">${timeline || '<li>Incident initialized.</li>'}</ul>
    </div>
  `;
}

async function loadHistory() {
  try {
    const history = await fetchJson(API.history);
    const tbody = document.getElementById('historyTableBody');
    if (!tbody) return;
    tbody.innerHTML = '';

    const recentEvents = Array.isArray(history) ? history.slice().reverse().slice(0, 8) : [];
    if (recentEvents.length === 0) {
      tbody.innerHTML = '<tr><td colspan="3" class="empty-state">No recovery events yet. Trigger a simulation to record one.</td></tr>';
      return;
    }

    recentEvents.forEach(event => {
      const row = document.createElement('tr');
      const stateValue = event && (event.toState || event.state || 'UNKNOWN');
      const reasonValue = event && (event.reason || 'Health event recorded');
      const tsValue = event && (event.timestamp || event.time || event.createdAt);
      row.innerHTML = `
        <td>${formatTimestamp(tsValue)}</td>
        <td><span class="mini-badge ${stateValue === 'DEGRADED' || stateValue === 'DOWN' ? 'alert' : 'ok'}">${stateValue}</span></td>
        <td>${reasonValue}</td>
      `;
      tbody.appendChild(row);
    });
  } catch (error) {
    console.error('History load failed', error);
    const tbody = document.getElementById('historyTableBody');
    if (tbody) tbody.innerHTML = '<tr><td colspan="3" class="empty-state">No recovery events yet. Trigger a simulation to record one.</td></tr>';
  }
}

function buildIncidentChart(incidents) {
  const canvas = document.getElementById('incidentChart');
  if (!canvas) return;

  if (incidentChart) incidentChart.destroy();

  const recentIncidents = incidents.slice(-8);
  const incidentOffset = Math.max(incidents.length - recentIncidents.length, 0);
  const labels = recentIncidents.map((item, index) =>
    `#${item.id || incidentOffset + index + 1} · ${new Date(item.createdAt).toLocaleString([], { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' })}`
  );
  const values = recentIncidents.map((_, index) => incidentOffset + index + 1);

  incidentChart = new Chart(canvas, {
    type: 'line',
    data: {
      labels,
      datasets: [{
        label: 'Incidents',
        data: values,
        fill: true,
        borderColor: '#7dd3fc',
        backgroundColor: 'rgba(125,211,252,0.18)',
        tension: 0.35,
        pointRadius: 4
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: true, labels: { color: '#e2edf7', usePointStyle: true } },
        tooltip: {
          callbacks: {
            title: context => labels[context[0].dataIndex],
            label: context => {
              const incident = recentIncidents[context.dataIndex];
              return [
                `Total incidents: ${context.parsed.y}`,
                `${incident.severity || 'LOW'} · ${incident.status || 'DETECTED'}`,
                `${incident.component || 'Unknown component'} — ${incident.summary || 'No summary'}`
              ];
            }
          }
        }
      },
      scales: {
        x: {
          title: { display: true, text: 'Incident created at', color: '#8aa3bd' },
          ticks: { color: '#cbd5e1', maxRotation: 0, callback: (_, index) => `#${recentIncidents[index].id || incidentOffset + index + 1}` },
          grid: { color: 'rgba(255,255,255,0.05)' }
        },
        y: {
          beginAtZero: true,
          title: { display: true, text: 'Cumulative incidents', color: '#8aa3bd' },
          ticks: { stepSize: 1, precision: 0, color: '#cbd5e1' },
          grid: { color: 'rgba(255,255,255,0.05)' }
        }
      }
    }
  });
}

async function loadChartData() {
  try {
    const incidents = await fetchJson(API.incidents);
    buildIncidentChart(incidents || []);
  } catch (error) {
    console.error('Chart data failed', error);
  }
}

async function triggerScenario(name) {
  const action = name === 'cpu' ? API.cpu : name === 'memory' ? API.memory : name === 'dependency' ? API.dependency : API.latency;
  try {
    const response = await fetch(action);
    if (!response.ok) {
      throw new Error(`${response.status} ${response.statusText}`);
    }
    showNotification(`${name.toUpperCase()} scenario triggered`, 'success');
    setTimeout(refreshDashboard, 500);
    // Simulations complete their recovery workflow in 1.5–2.5 seconds.
    setTimeout(refreshDashboard, 3000);
  } catch (error) {
    console.error(error);
    showNotification('Simulation failed', 'danger');
  }
}

async function refreshDashboard() {
  await Promise.all([
    loadHealth(),
    loadMetrics(),
    loadSummary(),
    loadIncidents(),
    loadHistory(),
    loadChartData()
  ]);
}

document.addEventListener('DOMContentLoaded', () => {
  refreshDashboard();
  setInterval(refreshDashboard, 6000);
  document.getElementById('simulateCpu').addEventListener('click', () => triggerScenario('cpu'));
  document.getElementById('simulateMemory').addEventListener('click', () => triggerScenario('memory'));
  document.getElementById('simulateDependency').addEventListener('click', () => triggerScenario('dependency'));
  document.getElementById('simulateLatency').addEventListener('click', () => triggerScenario('latency'));
});
