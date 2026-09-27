import os
import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("results/results.csv")
os.makedirs("docs/plots", exist_ok=True)

wl1 = df[df["Workload"] == "RandomAccess"]
wl2 = df[df["Workload"] == "Search"]
wl3 = df[df["Workload"] == "InsertRemove"].copy()
wl3["Series"] = wl3["Structure"] + " (" + wl3["Operation"] + ")"
wl4 = df[df["Workload"] == "PriorityProcessing"]

STYLE_MAP = {
    "DynamicArray": {"linestyle": "-", "marker": "o", "linewidth": 2.5, "zorder": 1},
    "LinkedList": {"linestyle": "--", "marker": "s", "linewidth": 2.0, "zorder": 2},
    "DynamicArray (Insert-Begin)": {"linestyle": "-", "marker": "o", "linewidth": 2.0},
    "DynamicArray (Remove-Begin)": {"linestyle": "-", "marker": "^", "linewidth": 2.0},
    "LinkedList (Insert-Begin)": {"linestyle": "--", "marker": "s", "linewidth": 2.0},
    "LinkedList (Remove-Begin)": {"linestyle": "--", "marker": "d", "linewidth": 2.0},
    "Insert": {"linestyle": "-", "marker": "o", "linewidth": 2.0},
    "Extract": {"linestyle": "-", "marker": "s", "linewidth": 2.0},
}


def time_plot(data, title, filename, series_col):
    plt.figure(figsize=(10, 7))
    for label, group in data.groupby(series_col):
        group = group.sort_values("N")
        style = STYLE_MAP.get(label, {"linestyle": "-", "marker": "o", "linewidth": 2})
        plt.plot(group["N"], group["AvgTimeNs"], label=label, **style)

    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel("Input Size (N)")
    plt.ylabel("Average Time (nanoseconds)")
    plt.title(title, fontsize=14, fontweight="bold")
    plt.legend()
    plt.grid(True, which="both", linestyle="--", alpha=0.5)
    plt.tight_layout()
    plt.savefig(f"docs/plots/{filename}", dpi=150)
    plt.close()


time_plot(wl1, "Workload 1: Random Access (get)", "workload1_random_access.png", "Structure")
time_plot(wl2, "Workload 2: Search (contains)", "workload2_search.png", "Structure")
time_plot(wl3, "Workload 3: Insert & Remove at Begin", "workload3_insert_remove.png", "Series")
time_plot(wl4, "Workload 4: MinHeap Priority Processing", "workload4_minheap.png", "Operation")


def metric_subplot(ax, data, title, series_col):
    for label, group in data.groupby(series_col):
        group = group.sort_values("N")
        style = STYLE_MAP.get(label, {"linestyle": "-", "marker": "o", "linewidth": 2})
        ax.plot(group["N"], group["MetricValue"], label=label, **style)

    ax.set_xscale("log")
    ax.set_yscale("log")
    ax.set_xlabel("Input Size (N)")
    ax.set_ylabel(data["MetricName"].iloc[0])
    ax.set_title(title, fontsize=11, fontweight="bold")
    ax.legend(fontsize=8)
    ax.grid(True, which="both", linestyle="--", alpha=0.5)


fig, axes = plt.subplots(2, 2, figsize=(14, 10))
metric_subplot(axes[0, 0], wl1, "Workload 1: Accesses", "Structure")
metric_subplot(axes[0, 1], wl2, "Workload 2: Comparisons", "Structure")
metric_subplot(axes[1, 0], wl3, "Workload 3: Movements", "Series")
metric_subplot(axes[1, 1], wl4, "Workload 4: Comparisons", "Operation")

fig.suptitle("Operations / Comparisons / Accesses vs. N", fontsize=15, fontweight="bold")
plt.tight_layout()
plt.savefig("docs/plots/metric_vs_n.png", dpi=150)
plt.close()

print("\n### Execution Time (ns)\n")
pivot_time = df.pivot_table(index=["Structure", "Workload", "Operation"], columns="N", values="AvgTimeNs")
print(pivot_time.round(1).to_markdown())

print("\n### Metric Values (Accesses / Comparisons / Movements)\n")
pivot_metric = df.pivot_table(index=["Structure", "Workload", "Operation", "MetricName"], columns="N", values="MetricValue")
print(pivot_metric.to_markdown())

print("\nSaved 5 plots to docs/plots/ (4 time plots + 1 combined metric plot)")