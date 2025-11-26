package def.src;

public class SinglyLinkedList {
	protected Node head;

	protected class Node {
		Edge edge;
		Node next;

		Node(Edge edge) {
			this.edge = edge;
			next = null;
		}
	}

	public SinglyLinkedList() {
		head = null;
	}

	public boolean isEmpty() {
		return (head == null);
	}

	public void insertFront(Edge edge) {
		Node temp = new Node(edge);
		temp.next = head;
		head = temp;
	}

	public void insertLast(Edge edge) {
		Node temp = head;
		while (temp.next != null) {
			temp = temp.next;
		}
		temp.next = new Node(edge);
	}

	public void deleteNode(Edge edge) {
		if (head.edge == edge) {
			head = head.next;
			return;
		}
		Node temp = head;
		while (temp.next != null && temp.next.edge != edge) {
			temp = temp.next;
		}
		if (temp.next == null) {
			// value not found
			return;
		}
		Node next = temp.next;
		next = next.next;
		temp.next = next; // skips the node we want to delete
	}

	public boolean findNode(Edge edge) {
		if (this.isEmpty()) {
			return false;
		}
		Node temp = head;
		while (temp != null) { // need to check for last Node
			if (temp.edge == edge) {
				return true;
			}
			temp = temp.next;
		}
		return false;
	}

	public String toString() { // headElem -> head.nextElem -> ... -> null
		if (this.isEmpty()) {
			return "null";
		}
		StringBuilder sb = new StringBuilder();
		Node temp = head;
		while (temp != null) {
			sb.append(temp.edge);
			sb.append(" -> ");
			temp = temp.next;
		}
		sb.append("null");
		return sb.toString();
	}

}
