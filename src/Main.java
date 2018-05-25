import java.util.Random;

public class Main {

    public static void main(String[] args) {
        String[] routingTable = createRoutingMasterTable();
                
        System.out.println("Trie Method:");
        LookupTable lt = new LookupTrie();
        populateLookupTable(lt, routingTable);
        basicTestLookupTable(lt, "1110100011100011");
        testLookupTable(lt, routingTable);
               
        System.out.println();
        
        System.out.println("List Method:");
        LookupTable ll = new LookupList();
        populateLookupTable(ll, routingTable);
        basicTestLookupTable(ll, "1110100011100011");
        testLookupTable(ll, routingTable);        
    }
    
    public static void populateLookupTable(LookupTable lookupTable, String[] routingTable) {
        long startTime = System.currentTimeMillis();
        Random rand = new Random(42);
        for (int i = 0; i < 30000; i++) {
            lookupTable.add(routingTable[rand.nextInt(routingTable.length)], rand.nextInt(10));
        }
        long endTime = System.currentTimeMillis();;
        long duration = endTime - startTime;
        System.out.println("Time to populate table: " + duration + "ms");
    }
    
    public static String[] createRoutingMasterTable() {
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
        return routingTable;
    }
    
    public static void testLookupTable(LookupTable lookupTable, String[] routingTable) {
        long startTime = System.currentTimeMillis();;
        Random rand = new Random(128);
        for (int i = 0; i < 100000; i++) {
            lookupTable.search(routingTable[rand.nextInt(routingTable.length)]);
        }
        long endTime = System.currentTimeMillis();;
        long duration = endTime - startTime;
        System.out.println("Time make 100,000 lookups: " + duration + "ms");
    }
    
    public static void basicTestLookupTable(LookupTable lookupTable, String address) {
        System.out.print("Basic Test: ");
        lookupTable.searchDetailed(address);
    }

}
