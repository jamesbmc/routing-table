
public class LookupTrie implements LookupTable{
    private Node head;
    
    public LookupTrie() {
        head = new Node();
    }
    public void add(String address, int iface) {
        String search = address;
        Node curr = head;
        while (search.length() > 0) {
            if (search.charAt(0) == '0') {
                if (curr.left == null) {
                    curr.left = new Node();
                }
                curr = curr.left;
            } else {
                if (curr.right == null) {
                    curr.right = new Node();
                }
                curr = curr.right;
            }
            search = search.substring(1, search.length());
        }
        curr.iface = iface;
        curr.address = address;
    }

    public void search(String address) {
        int matchingDigits = 0;
        String search = address;
        Node curr = head;
        String longestMatchingAddress = "";
        int longestMatchingIface = -1;
        while (curr != null && search.length() > 0) {
            if (curr.address != null) {
                longestMatchingAddress = curr.address;
                longestMatchingIface = curr.iface;
            }
            if (search.charAt(0) == '0') {
                curr = curr.left;
            } else {
                curr = curr.right;
            }
            search = search.substring(1, search.length());
            matchingDigits++;
        }
        if (matchingDigits == 0 || longestMatchingAddress == "") {
            System.out.println("No path to destination " + address);
        } else {
            System.out.println("Destination: " + address + ", Longest Prefix: " + longestMatchingAddress + "*");
            System.out.println("Next hop on interface " + longestMatchingIface);
        }
    }
    
    public void print() {
        print(head, 0);
    }
    
    private void print(Node root, int space) {
        if (root == null) {
            return;
        }
        print(root.left, space);
        if (root.address != null) {
            System.out.println("Address: " + root.address + ", Interface: " + root.iface);

        }
        print(root.right, space);
    }
    
    private class Node {
        private int iface;
        private String address;
        private Node left;
        private Node right;
        
        private Node() {
            iface = -1;
            left = null;
            right = null;
        }
    }

}
