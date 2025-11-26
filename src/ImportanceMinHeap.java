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

	
	public void display()
	{
		for(int i = 1; i <= this.size; i++)
			System.out.println(heap[i].word + ", " + heap[i].importance);
	}
	
	public static void main(String[] args)
	{
		
		ImportanceMinHeap heap = new ImportanceMinHeap(2);
		/*heap.insert(node1);
		heap.insert(node3);
		//heap.insert(node2);*/
		
		heap.display();
		
	}

}
