import pandas as pd
import matplotlib.pyplot as plt
import os

os.makedirs("results/plots", exist_ok=True)

df = pd.read_csv("results/results.csv")

workloads = [
    ("RandomAccess", "Workload 1: Random Access (get)", "random_access.png"),
    ("Search", "Workload 2: Search (contains)", "search.png"),
    ("InsertRemove", "Workload 3: Insert & Remove at Begin", "insert_remove.png"),
    ("PriorityProcessing", "Workload 4: MinHeap Priority Processing", "minheap_processing.png")
]

for wl_key, title, filename in workloads:
    plt.figure(figsize=(8, 5))
    wl_df = df[df['Workload'] == wl_key]

    # Plot each data structure or operation line
    for label_name, group in wl_df.groupby(['Structure', 'Operation']):
        struct, op = label_name
        label = f"{struct} ({op})" if wl_key in ["InsertRemove", "PriorityProcessing"] else struct
        plt.plot(group['N'], group['AvgTimeNs'], marker='o', linewidth=2, label=label)

    plt.xscale('log')
    plt.yscale('log')
    plt.xlabel('Input Size (N)')
    plt.ylabel('Average Time (nanoseconds)')
    plt.title(title, fontweight='bold')
    plt.grid(True, which="both", ls="--", alpha=0.5)
    plt.legend()
    plt.tight_layout()

    # Save chart image
    plt.savefig(f"results/plots/{filename}", dpi=300)
    plt.close()

print("All 4 plots created successfully in results/plots")