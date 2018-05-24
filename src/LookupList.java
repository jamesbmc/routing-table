public class LookupList implements LookupTable{
    private Node head;
    
    public LookupList() {
        head = null;
    }
    
    public void add(String address, int iface) {
        Node newNode = new Node(address, iface);
        if (head == null) {
            head = newNode;
        } else {
            Node curr = head;
            while (curr != null && curr.next != null) {
                if (curr.address.equals(address)) {
                    curr.iface = iface;
                    return;
                }
                curr = curr.next;
            }
            curr.next = newNode;
        }
    }
    
    public void search(String address) {
        Node curr = head;
        int maxMatchingDigits = 0;
        int iface = -1;
        String matchAddress = "";
        while (curr != null) {
            int matchingDigits = 0;
            for (int i = 0; i < curr.address.length(); i++) {
                if (i > address.length() - 1 || address.charAt(i) != curr.address.charAt(i)) {
                    matchingDigits = 0;
                    break;
                } else {
                    matchingDigits++;
                }
            }
            if (matchingDigits > maxMatchingDigits) {
                maxMatchingDigits = matchingDigits;
                iface = curr.iface;
                matchAddress = curr.address;
            }
            curr = curr.next;
        }
        if (iface == -1) {
            System.out.println("No path to destination " + address);
        } else {
            System.out.println("Destination: " + address + ", Longest Prefix: " + matchAddress + "*");
            System.out.println("Next hop on interface " + iface);
        }
    }
    
    public void print() {
        Node curr = head;
        while (curr != null) {
            System.out.println("Address: " + curr.address + ", Interface: " + curr.iface);
            curr = curr.next;
        }
    }
    
    private class Node {
        private String address;
        private int iface;
        private Node next;
        
        private Node(String address, int iface) {
            this.address = address;
            this.iface = iface;
            next = null;
        }
    }
}
