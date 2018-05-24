import java.util.Random;

public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Random rand = new Random();
        final int tableSize = 1400000;
        String[] routingTable = new String[tableSize];
        for (int i = 0; i < tableSize; i++) {
            int networkSize = rand.nextInt(31) + 1;
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < networkSize; j++) {
                sb.append("" + rand.nextInt(2));
            }
            routingTable[i] = sb.toString();
        }
        
        long startTime;
        long endTime;
        long duration;
        
        System.out.println("Trie Method:");
        LookupTable lt = new LookupTrie();
        startTime = System.nanoTime();
        populate(lt, routingTable);
        endTime = System.nanoTime();
        duration = endTime - startTime;
        System.out.println("Time to populate table: " + duration + "ns");
        startTime = System.nanoTime();
        lt.search("1110100011100011");
        endTime = System.nanoTime();
        duration = endTime - startTime;
        System.out.println("Time to lookup longest matching prefix: " + duration + "ns");
        
        System.out.println();
        
        System.out.println("List Method:");
        LookupTable ll = new LookupList();
        startTime = System.nanoTime();
        populate(ll, routingTable);
        endTime = System.nanoTime();
        duration = endTime - startTime;
        System.out.println("Time to populate table: " + duration + "ns");
        startTime = System.nanoTime();
        ll.search("1110100011100011");
        endTime = System.nanoTime();
        duration = endTime - startTime;
        System.out.println("Time to lookup longest matching prefix: " + duration + "ns");           
    }
    
    public static void populate(LookupTable lookupTable, String[] routingTable) {
        Random rand = new Random(42);
        for (int i = 0; i < 30000; i++) {
            lookupTable.add(routingTable[rand.nextInt(routingTable.length)], rand.nextInt(10));
        }
    }

}
