const API = {
health: '/actuator/health',
metrics: '/metrics/system',
history: '/self-healing/history',
cpu: '/simulate/cpu',
memory: '/simulate/memory'
};

let eventChart = null;

function formatTimestamp(timestamp) {
return new Date(timestamp).toLocaleString();
}

function showNotification(message, type = "success") {

 // 
const notification =
    document.createElement("div");

notification.className =
    `alert alert-${type} position-fixed top-0 end-0 m-3`;

notification.style.zIndex = "9999";

notification.innerText = message;

document.body.appendChild(notification);

setTimeout(() => {
    notification.remove();
}, 3000);
 // 

}

async function loadHealth() {

 // 
try {

    const response =
        await fetch(API.health);

    const data =
        await response.json();

    const statusElement =
        document.getElementById("healthStatus");

    if (data.status === "UP") {

        statusElement.innerHTML =
            "🟢 HEALTHY";

        statusElement.style.color =
            "#22c55e";

    } else {

        statusElement.innerHTML =
            "🔴 DEGRADED";

        statusElement.style.color =
            "#ef4444";
    }

} catch (error) {

    console.error(error);

    document.getElementById("healthStatus")
        .innerHTML = "⚠️ ERROR";
}
 // 

}

async function loadMetrics() {

 // 
try {

    const response =
        await fetch(API.metrics);

    const data =
        await response.json();

    const degradations =
        data.totalDegradations ?? 0;

    const recoveries =
        data.totalRecoveries ?? 0;

    document.getElementById("degradations")
        .innerText = degradations;

    document.getElementById("recoveries")
        .innerText = recoveries;

    const successRate =
        degradations > 0
            ? ((recoveries / degradations) * 100).toFixed(0)
            : 100;

    const successElement =
        document.getElementById("successRate");

    if (successElement) {
        successElement.innerText =
            `${successRate}%`;
    }

} catch (error) {

    console.error(
        "Failed to load metrics",
        error
    );
}
 // 

}

async function loadHistory() {

 // 
try {

    const response =
        await fetch(API.history);

    const data =
        await response.json();

    const tbody =
        document.getElementById("historyList");

    tbody.innerHTML = "";

    data.slice().reverse().forEach(event => {

        const row =
            document.createElement("tr");

        row.innerHTML = `
            <td>${formatTimestamp(event.timestamp)}</td>

            <td>
                <span class="badge bg-success">
                    ${event.fromState}
                </span>

                →

                <span class="badge bg-danger">
                    ${event.toState}
                </span>
            </td>

            <td>${event.reason}</td>
        `;

        tbody.appendChild(row);
    });

} catch (error) {

    console.error(
        "Failed to load history",
        error
    );
}
 // 

}

async function loadChart() {

 // 
try {

    const response =
        await fetch(API.history);

    const history =
        await response.json();

    const ctx =
        document.getElementById("eventChart");

    if (!ctx) return;

    if (eventChart) {
        eventChart.destroy();
    }

    const labels =
        history.map(event =>
            new Date(event.timestamp)
                .toLocaleTimeString()
        );

    const degradations =
        history.map(event =>
            event.toState === "DEGRADED"
                ? 1
                : 0
        );

    const recoveries =
        history.map(event =>
            event.toState === "HEALTHY"
                ? 1
                : 0
        );

    eventChart = new Chart(ctx, {

        type: "bar",

        data: {

            labels,

            datasets: [
                {
                    label:
                        "Degradation Events",

                    data:
                        degradations,

                    backgroundColor:
                        "rgba(239,68,68,0.8)",

                    borderColor:
                        "#ef4444",

                    borderWidth: 1
                },

                {
                    label:
                        "Recovery Events",

                    data:
                        recoveries,

                    backgroundColor:
                        "rgba(34,197,94,0.8)",

                    borderColor:
                        "#22c55e",

                    borderWidth: 1
                }
            ]
        },

        options: {

            responsive: true,

            maintainAspectRatio: false,

            plugins: {

                title: {

                    display: true,

                    text:
                        "System Event Timeline",

                    color:
                        "#ffffff",

                    font: {
                        size: 18
                    }
                },

                legend: {

                    labels: {
                        color:
                            "#ffffff"
                    }
                }
            },

            scales: {

                x: {

                    ticks: {
                        color:
                            "#ffffff"
                    },

                    grid: {
                        color:
                            "rgba(255,255,255,0.08)"
                    }
                },

                y: {

                    beginAtZero: true,

                    ticks: {
                        color:
                            "#ffffff",

                        stepSize: 1
                    },

                    title: {

                        display: true,

                        text:
                            "Events",

                        color:
                            "#ffffff"
                    },

                    grid: {
                        color:
                            "rgba(255,255,255,0.08)"
                    }
                }
            }
        }
    });

} catch (error) {

    console.error(
        "Failed to load chart",
        error
    );
}
 // 

}

async function simulateCpu() {

 // 
try {

    await fetch(API.cpu);

    showNotification(
        "CPU degradation triggered"
    );

    setTimeout(
        refreshData,
        1500
    );

} catch (error) {

    console.error(error);

    showNotification(
        "CPU simulation failed",
        "danger"
    );
}
 // 

}

async function simulateMemory() {

 // 
try {

    await fetch(API.memory);

    showNotification(
        "Memory degradation triggered"
    );

    setTimeout(
        refreshData,
        1500
    );

} catch (error) {

    console.error(error);

    showNotification(
        "Memory simulation failed",
        "danger"
    );
}
 // 

}

async function refreshData() {

 // 
const refresh =
    document.getElementById("lastRefresh");

if (refresh) {

    refresh.innerText =
        new Date()
            .toLocaleTimeString();
}

await Promise.all([
    loadHealth(),
    loadMetrics(),
    loadHistory(),
    loadChart()
]);
 // 

}

refreshData();

setInterval(
refreshData,
5000
);
