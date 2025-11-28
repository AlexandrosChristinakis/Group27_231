package def.src;

public class ImportanceMinHeap {
	public class Node {
		private String word;
		private int importance;
		
		public Node(String word, int importance)
		{
			this.word = word;
			this.importance = importance;
		}
		
		public String getWord()
		{
			return this.word;
		}
		
		public int getImportance()
		{
			return this.importance;
		}
	}
	
	private static final int defaultMaxSize = 10;
	private Node heap[];
	private int size;
	private int maxSize;
	
	public ImportanceMinHeap()
	{
		this(defaultMaxSize);
	}
	
	public ImportanceMinHeap(int maxSize)
	{
		this.maxSize = maxSize;
		heap = new Node[this.maxSize + 1];
        // Fill the first node in the heap with a blank node so it's not null.
        heap[0] = new Node("", -1);
	}
	
	private int parent(int pos) { return pos / 2; }
	
	private void swap(int fpos, int spos)
    {
 
		Node tmp;
        tmp = heap[fpos];
 
        heap[fpos] = heap[spos];
        heap[spos] = tmp;
    }
	
	public void insert(String word, int importance) {
		Node newNode = new Node(word, importance);
		
	    if (size >= maxSize) {
	        // Replace root if the new node has higher importance
	        if (heap[1].importance < newNode.importance) {
	            heap[1] = newNode;
	            heapifyDown(1); // Restore min-heap property
	        }
	        return;
	    }
	    
	    // Insert new node at the end
	    heap[++size] = newNode;
	    int current = size;
	    
	    // Bubble up to maintain heap property
	    while (current > 1 && heap[current].importance < heap[parent(current)].importance) {
	        swap(current, parent(current));
	        current = parent(current);
	    }
	}

	// Helper function to maintain heap property from the root down
	private void heapifyDown(int pos) {
	    while (true) {
	        int smallest = pos;
	        int leftChild = pos * 2;
	        int rightChild = pos * 2 + 1;

	        // Check left child
	        if (leftChild <= size && heap[leftChild].importance < heap[smallest].importance) {
	            smallest = leftChild;
	        }

	        // Check right child
	        if (rightChild <= size && heap[rightChild].importance < heap[smallest].importance) {
	            smallest = rightChild;
	        }

	        // If the smallest is not the current node, swap and continue
	        if (smallest != pos) {
	            swap(pos, smallest);
	            pos = smallest;
	        } else {
	            break;
	        }
	    }
	}

	public boolean existsNode(String word, int importance) {
	    for (int i = 1; i <= size; i++) {
	        if (heap[i].word.equals(word) && heap[i].importance == importance) {
	            return true;
	        }
	    }
	    return false;
	}

	
	public void displayDescending() {
		for(int i = this.size-1; i >= 0; i--) {

            if (heap[i].importance == heap[i + 1].importance) {
                swapAlphabetically(i, i + 1);
            }

            System.out.println(heap[i + 1].word + ", " + heap[i + 1].importance);
        }
	}

    public void swapAlphabetically(int i, int j) {
        Node tempNode;
        // We want to find out if a String is “bigger” or “smaller” than another string
        // (that is, whether it comes before or after alphabetically)
        int stringComparisonResult = heap[i].word.compareTo(heap[j].word);

        // If stringComparisonResult > 0 it means that heap[i] is larger than heap[i+1].
        if (stringComparisonResult > 0) {
            tempNode = heap[i+1];
            heap[i+1] = heap[i];
            heap[i] = tempNode;
        }
    }

    public static void main(String[] args) {

        System.out.println("===== Test 1: Basic Insert (size < maxSize) =====");
        ImportanceMinHeap heap1 = new ImportanceMinHeap(5);
        heap1.insert("apple", 10);
        heap1.insert("banana", 5);
        heap1.insert("cat", 7);
        heap1.displayDescending();
        // Expected heap order (min-heap): lowest importance at top


        System.out.println("\n===== Test 2: Insert more than maxSize → should keep ONLY top 5 =====");
        ImportanceMinHeap heap2 = new ImportanceMinHeap(5);
        heap2.insert("apple", 10);
        heap2.insert("banana", 5);
        heap2.insert("cat", 7);
        heap2.insert("dog", 12);
        heap2.insert("egg", 2);
        heap2.insert("frog", 20);  // should replace the smallest (importance 2)
        heap2.insert("goat", 1);   // should NOT enter heap
        heap2.insert("horse", 15); // should replace the smallest
        heap2.displayDescending();
        // Should contain 5 HIGHEST importance values


        System.out.println("\n===== Test 3: Root replacement behavior =====");
        ImportanceMinHeap heap3 = new ImportanceMinHeap(3);
        heap3.insert("a", 1);
        heap3.insert("b", 2);
        heap3.insert("c", 3);
        System.out.println("Initial heap:");
        heap3.displayDescending();

        heap3.insert("d", 10); // should replace root (importance 1)
        System.out.println("After inserting (d,10):");
        heap3.displayDescending();

        heap3.insert("e", 0); // lower importance → should NOT replace anything
        System.out.println("After inserting (e,0) – no change expected:");
        heap3.displayDescending();


        System.out.println("\n===== Test 4: existsNode() =====");

        ImportanceMinHeap heap4 = new ImportanceMinHeap(4);
        heap4.insert("apple", 5);
        heap4.insert("banana", 3);
        heap4.insert("cat", 7);
        heap4.insert("dog", 9);

        System.out.println("Exists apple,5? " + heap4.existsNode("apple", 5));  // true
        System.out.println("Exists banana,1? " + heap4.existsNode("banana", 1)); // false
        System.out.println("Exists cat,7? " + heap4.existsNode("cat", 7)); // true
        System.out.println("Exists lion,10? " + heap4.existsNode("lion", 10)); // false


        System.out.println("\n===== Test 5: Replacement Only When Larger =====");
        ImportanceMinHeap heap5 = new ImportanceMinHeap(3);
        heap5.insert("x", 2);
        heap5.insert("y", 4);
        heap5.insert("z", 6);
        System.out.println("Initial heap:");
        heap5.displayDescending();

        heap5.insert("w", 1); // too small → ignore
        System.out.println("After inserting (w,1) – nothing should change:");
        heap5.displayDescending();

        heap5.insert("v", 10); // large → replace smallest (importance 2)
        System.out.println("After inserting (v,10) – should replace root:");
        heap5.displayDescending();


        System.out.println("\n===== Test 6: Insert in descending order =====");
        ImportanceMinHeap heap6 = new ImportanceMinHeap(5);
        heap6.insert("a", 50);
        heap6.insert("b", 40);
        heap6.insert("c", 30);
        heap6.insert("d", 20);
        heap6.insert("e", 10);
        heap6.displayDescending();  // should show min-heap ordering

        heap6.insert("f", 60); // should replace smallest (10)
        heap6.insert("g", 70); // should replace next smallest (20)
        heap6.displayDescending();
    }


}
