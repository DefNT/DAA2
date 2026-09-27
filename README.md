# DAA Assignment 2

## 1. Overview

This project implements and tests three core data structures in Java: a **Dynamic Array**, a **Linked List**, and a **Min-Heap**. Both `DynamicArray` and `LinkedList` share a common `ListStructure` interface (`add`, `remove`, `get`, `contains`), while `MinHeap` provides priority queue functions (`insert`, `peekMin`, `extractMin`)

The main goals of this assignment are:

1. **Prove Correctness**: Use loop invariants to prove two key algorithms work properly
2. **Analyze Complexity**: Determine the Big-O time and space costs for every operation
3. **Run Experiments**: Measure real execution times and operation counts across four workloads at sizes $N = 100$, $1,000$, $10,000$, and $100,000$
4. **Compare Theory vs. Practice**: See how CPU caching and memory layout make real code behave compared to Big-O predictions

---

## 2. Complexity Analysis

### Time and Space Complexity Table

| Data Structure | Operation | Best Time | Average Time | Worst Time | Extra Space |
| --- | --- | --- | --- | --- | --- |
| **Dynamic Array** | `add(x)` | $\Theta(1)$ | $\Theta(1)$ (Amortized) | $O(N)$ | $O(1)$ |
| **Dynamic Array** | `add(index, x)` | $\Theta(1)$ | $\Theta(N)$ | $\Theta(N)$ | $O(1)$ |
| **Dynamic Array** | `remove(index)` | $\Theta(1)$ | $\Theta(N)$ | $\Theta(N)$ | $O(1)$ |
| **Dynamic Array** | `get(index)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $O(1)$ |
| **Dynamic Array** | `contains(x)` | $\Theta(1)$ | $\Theta(N)$ | $\Theta(N)$ | $O(1)$ |
| **Linked List** | `add(x)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $O(1)$ |
| **Linked List** | `add(index, x)` | $\Theta(1)$ | $\Theta(N)$ | $\Theta(N)$ | $O(1)$ |
| **Linked List** | `remove(index)` | $\Theta(1)$ | $\Theta(N)$ | $\Theta(N)$ | $O(1)$ |
| **Linked List** | `get(index)` | $\Theta(1)$ | $\Theta(N)$ | $\Theta(N)$ | $O(1)$ |
| **Linked List** | `contains(x)` | $\Theta(1)$ | $\Theta(N)$ | $\Theta(N)$ | $O(1)$ |
| **Min-Heap** | `insert(x)` | $\Theta(1)$ | $O(\log N)$ | $O(\log N)$ | $O(1)$ |
| **Min-Heap** | `peekMin()` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $O(1)$ |
| **Min-Heap** | `extractMin()` | $\Theta(1)$ | $O(\log N)$ | $O(\log N)$ | $O(1)$ |

### Explanations:

* **Array Lookups vs. List Lookups**: `DynamicArray.get(index)` calculates an exact memory address instantly $Θ(1)$. `LinkedList.get(index)` has to start at the front and follow `next` pointers node-by-node $Θ(N)$
* **Array Shifting**: Adding or removing elements at index `0` in a `DynamicArray` forces every other element to move over one slot $Θ(N)$. For a `LinkedList`, it just changes one reference pointer $Θ(1)$
* **Min-Heap Efficiency**: A binary heap of $N$ items has a tree height of $\log_2(N)$. Moving elements up or down during inserts or extractions visits at most $\log_2(N)$ levels $O(\log N)$

---

## 3. Correctness (Loop Invariant Proofs)

### Proof 1 — `LinkedList.get(index)`

```java
Node current = head;
for (int i = 0; i < index; i++) {
    current = current.next;
}
return current.value;

```

* **Loop Invariant**: At the start of each pass `i`, `current` points to the node at index `i` (starting from `head` at index `0`)
* **Initialization**: Before the loop starts, $i = 0$ and `current = head`. The head is at index 0, so the invariant starts true
* **Maintenance**: Assume `current` is at index $i$. The loop body does `current = current.next`, moving it to node $i + 1$. The counter then increases to $i + 1$. At the start of the next pass, `current` is still pointing at node $i$ (for the new value of $i$)
* **Termination**: The loop stops when $i = \text{index}$. By our invariant, `current` is pointing directly at the node at position `index`. Returning `current.value` gives the correct element

---

### Proof 2 — `MinHeap.siftDown(index)`

```java
private void siftDown(int index) {
    while (true) {
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        int smallest = index;

        if (left < size && data[left] < data[smallest]) smallest = left;
        if (right < size && data[right] < data[smallest]) smallest = right;

        if (smallest == index) break;
        swap(index, smallest);
        index = smallest;
    }
}

```

* **Loop Invariant**: At the start of each pass, the min-heap rule ($\text{parent} \le \text{children}$) holds everywhere in the tree, except possibly at `index` relative to its immediate children
* **Initialization**: `extractMin()` calls this right after moving the last element to the root (`index = 0`). The rest of the tree was untouched and is valid, so only the root might be invalid
* **Maintenance**: The code finds the smallest value among `index` and its children. If `index` is already smallest, the heap is fixed and the loop stops. Otherwise, swapping `index` with `smallest` fixes that spot and pushes the out-of-order element down one level. The invariant now holds for the new, deeper `index`
* **Termination**: Each swap pushes `index` deeper down the tree. Since tree depth is bounded by $\log_2(N)$, `index` will eventually reach the bottom or find its correct spot. Once it stops, the entire array is guaranteed to be a valid min-heap

---

## 4. Experimental Setup

* **Data Sizes ($N$)**: $100$, $1,000$, $10,000$, and $100,000$
* **Operation Count ($M$)**
* **Workload 1 (Random Access)**: $10,000$ lookups (`get`)
* **Workload 2 (Search)**: $1,000$ linear searches (`contains`)
* **Workload 3 (Insert/Remove Front)**: $1,000$ insertions and $1,000$ removals at index `0`
* **Workload 4 (Priority Processing)**: $N$ inserts followed by $N$ extractions


* **Repetitions**: Every test is run 5 times and averaged
* **Timing Method**: Measured using `System.nanoTime()` around only the operations being benchmarked
* **Random Seed**: Fixed using `new Random(42)` so every run processes identical key sequences

---

## 5. Results

### Execution Time Summary (nanoseconds)

| Structure | Workload | Operation | N=100 | N=1000 | N=10000 | N=100000 |
| --- | --- | --- | --- | --- | --- | --- |
| **DynamicArray** | RandomAccess | `get` | 262,500.0 | 98,860.0 | 45,080.0 | 46,420.0 |
| **LinkedList** | RandomAccess | `get` | 924,340.0 | 5,185,960.0 | 57,202,740.0 | 600,735,680.0 |
| **DynamicArray** | Search | `contains` | 214,200.0 | 662,840.0 | 1,689,840.0 | 16,504,780.0 |
| **LinkedList** | Search | `contains` | 211,120.0 | 1,396,760.0 | 13,304,700.0 | 137,628,020.0 |
| **DynamicArray** | InsertRemove | `Insert-Begin` | 1,175,180.0 | 81,320.0 | 334,320.0 | 6,610,300.0 |
| **DynamicArray** | InsertRemove | `Remove-Begin` | 78,960.0 | 1,523,580.0 | 255,880.0 | 5,423,620.0 |
| **LinkedList** | InsertRemove | `Insert-Begin` | 65,060.0 | 44,620.0 | 25,240.0 | 39,480.0 |
| **LinkedList** | InsertRemove | `Remove-Begin` | 6,860.0 | 51,460.0 | 40,840.0 | 6,580.0 |
| **MinHeap** | PriorityProcessing | `Insert` | 15,400.0 | 47,040.0 | 299,640.0 | 1,488,400.0 |
| **MinHeap** | PriorityProcessing | `Extract` | 39,460.0 | 105,220.0 | 799,260.0 | 7,464,480.0 |

---

### Internal Operations Metric Table

| Structure | Workload | Operation | Metric Name | N=100 | N=1000 | N=10000 | N=100000 |
| --- | --- | --- | --- | --- | --- | --- | --- |
| **DynamicArray** | RandomAccess | `get` | Accesses | 10,000 | 10,000 | 10,000 | 10,000 |
| **LinkedList** | RandomAccess | `get` | Accesses | 502,068 | 4,953,268 | 49,618,268 | 493,288,268 |
| **DynamicArray** | Search | `contains` | Comparisons | 95,002 | 955,145 | 9,464,362 | 95,206,221 |
| **LinkedList** | Search | `contains` | Comparisons | 95,002 | 955,145 | 9,464,362 | 95,206,221 |
| **DynamicArray** | InsertRemove | `Insert-Begin` | Movements | 599,500 | 1,499,500 | 10,499,500 | 100,499,500 |
| **DynamicArray** | InsertRemove | `Remove-Begin` | Movements | 4,950 | 499,500 | 9,499,500 | 99,499,500 |
| **LinkedList** | InsertRemove | `Insert-Begin` | Movements | 1,000 | 1,000 | 1,000 | 1,000 |
| **LinkedList** | InsertRemove | `Remove-Begin` | Movements | 100 | 1,000 | 1,000 | 1,000 |
| **MinHeap** | PriorityProcessing | `Insert` | Comparisons | 184 | 2,247 | 22,597 | 227,159 |
| **MinHeap** | PriorityProcessing | `Extract` | Comparisons | 854 | 14,974 | 216,604 | 2,832,159 |

---

### Benchmark Plots

* **Time Scale Charts**: <img width="1500" height="1050" alt="image" src="https://github.com/user-attachments/assets/a043c211-1dea-4198-8407-514cda71dbe8" />
, <img width="1500" height="1050" alt="image" src="https://github.com/user-attachments/assets/d6034aa1-b2d6-4975-ab42-5f4dc4352be0" />
, <img width="1500" height="1050" alt="image" src="https://github.com/user-attachments/assets/af17fcdb-fd80-4740-99d8-eedababe2be2" />
, <img width="1500" height="1050" alt="image" src="https://github.com/user-attachments/assets/d61230e3-a158-49c6-8223-b0d08cddd161" />



* **Metric Scaling Chart**: `<img width="2100" height="1500" alt="image" src="https://github.com/user-attachments/assets/4bfcc254-ddb8-4a71-be42-f78450691c27" />
`



---

## 6. Discussion

### Do experimental results match theory?

Yes, closely:

* **Random Lookups (`get`)**: `DynamicArray` stays flat around $45μ\text{s}$, matching $Θ(1)$. `LinkedList` scales directly with $N$, taking over **0.6 seconds** at $N=100,000$ due to nearly 500 million pointer accesses
* **Front Operations**: `LinkedList` stays extremely fast ($\le 40μ\text{s}$), confirming $Θ(1)$ front ops. `DynamicArray` grows rapidly up to $6.6\text{ms}$, showing the cost of shifting elements
* **Min-Heap**: Operates in logarithmic time $O(\log N)$, taking under $8\text{ms}$ total to extract 100,000 items

### Cache Locality vs. Big-O

In Workload 2 (`contains`), both structures perform the **exact same number of comparisons** ($95,206,221$ at $N=100,000$). However, `DynamicArray` finishes in $16.5\text{ms}$ while `LinkedList` takes $137.6\text{ms}$ over **8 times slower**

This happens because arrays store data together in continuous memory, letting the CPU load elements into its ultra-fast cache ahead of time. Linked list nodes are scattered across memory, causing frequent CPU cache misses

---

## 7. Design Recommendations

| Workload Requirement | Recommended Choice | Why |
| --- | --- | --- |
| **Lots of random lookups (`get`)** | `DynamicArray` | Instant $Θ(1)$ indexing |
| **Frequent searching (`contains`)** | `DynamicArray` | Contiguous memory makes CPU caching $8\times$ faster |
| **Inserting/removing at the front** | `LinkedList` | $Θ(1)$ pointer updates without shifting memory |
| **Always need the smallest item** | `MinHeap` | Fast $O(\log N)$ inserts and removals |

---

## 8. Conclusion

The results confirm our theoretical complexity analysis: arrays excel at indexing and sequential scanning due to CPU caching, linked lists excel at boundary insertions without memory shifts, and binary min-heaps handle priority items efficiently. Big-O analysis gives a accurate picture of how algorithms scale, but hardware details like memory layout play a big role in real-world speed

---
