public class SinglyLinkedList {
    protected Node head;

    public SinglyLinkedList() {
        this.head = null;
    }

    public void insert(Edge edge) {
        if (this.head == null) {
            this.head = new Node(edge);
            return;
        }

        Node temp = this.head;
        while (temp.next != null) {
            temp = temp.next;
        }

        temp.next = new Node(edge);
    }

    public Edge getEdge(char c) {
        Node temp = this.head;
        while (temp != null) {
            if (temp.edge.label.charAt(0) == c) {
                return temp.edge;
            }
            temp = temp.next;
        }
        return null;
    }

    private static class Node {
        Edge edge;
        Node next;

        Node (Edge edge) {
            this.edge = edge;
            this.next = null;
        }
    }
}

