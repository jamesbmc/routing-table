# Algorithms for Performing Route Lookups in Hardware
As the internet has expanded, **fast** routing of packets has become increasingly important. These packets make up the content that is sent between devices, and we have grown accustomed to on-demand access to that content. How are packets sent from far off servers (say at Google) to your computer?

## A Packet's Journey
The answer is "next hop" routing. A packet contains a few items of information, among which is the IP address of its destination. Routers store a table in hardware that (usually) dynamically learns where to send packets addressed to certain networks.
![alt text](http://www.cis131.com/mediawiki/images/a/af/Cis131-fig12-3.jpg)

(Credit to http://www.cis131.com)

The metrics of interest to us here are the destination, the netmask, and the gateway/interface (the gateway is the address of the interface). When the [netmask is combined with the destination IP address](https://www.webopedia.com/TERM/S/subnet_mask.html), the result is the network address. The routing table may not know how to send the packet to the exact network it is destined for, but odds are good that it will have an entry that is pretty close. For example, if the network address in binary is 001010011 and the routing table has an entry for 0* and 0010* (where * signifies that the bits after can take on many values) then the gateway corresponding to 0010* is the best "next hop" to send the packet through. In general, routers want to send packets to the longest (most specific) network prefix. Packets repeat this process until they arrive at the network, which then handles routing towards the correct MAC address.

## The Nitty Gritty
While reading the paragraph above, the question might have crossed your mind, "How do routers find the longest matching network prefix in the table?" As it turns out, the answer is very important to your internet browsing. There are a few different algorithms routers have used, but the most common revolve around a **list** or a **trie**:

### The Lookup List
The naïve answer to this problem is to store a list of network prefixes along with relevant information.
#### Insertion
Insertion of new information into the routing table is an O(n) operation where n is the number of entries in the table. The router must look through all entries to determine whether it needs to update the information for the network prefix or add it as a new entry to the table.
#### Search
Searching the routing table for the longest matching network is also an O(n) operation where n is defined the same way as above. The router must check each entry to ensure that it has the __longest__ possible network prefix that matches the destination network.

### The Lookup Trie
A more interesting answer to the longest matching network prefix problem is a trie (which can also be used to predict what word you will type based on the letters entered so far!). A trie is a data structure similar to a binary tree in that there is a root node with children that represent the first letter/number of an input. In our case, binary has only two possible values so the trie will be exactly a binary (each node has at most two children) tree with nodes to the left representing 0 and nodes to the right representing 1:
![alt text](https://www.researchgate.net/profile/Hyesook_Lim/publication/224116488/figure/fig1/AS:302885238788097@1449224876335/The-binary-trie-for-an-example-set-of-prefixes.png)

(Credit to https://www.researchgate.net)

Nodes that represent a network prefix in the routing table would contain relevant information.
#### Insertion
Insertion of a new network prefix into the routing table is an O(n) operation where n is the length of the network prefix to be inserted. The will always have to move through all bits of the network address to reach the node where relevant information is stored.
#### Search
Searching the routing table for the longest matching network prefix has the same runtime as insertion for the same reason outlined above.

### My Implementation
**I might add more data here on insertion and search on increasing larger routing tables to fully illustrate the algorithmic runtimes discussed above**

This github repo contains the code necessary to implement a lookup list and a lookup trie. I begin main with the following code block:
```java
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
```
This block of code initializes an array containing 1.4 million network prefixes ranging in length from 1-31 digits long (the data type stored is a string because that is easier to iterate through when checking matching digits). One was the lower end of the range because I thought it would be interesting to observe how many times a path to the destination address was not found. In reality, there would be a default gateway through which to send the packet if there are no other matches. 31 was the high end of the range because IPv$ addresses are 32 bits long and so a 32 bit network address prefix would leave no room for hosts.

My lookup list and lookup trie were populated by the following function:
```java
public static void populate(LookupTable lookupTable, String[] routingTable) {
        Random rand = new Random(42);
        for (int i = 0; i < 30000; i++) {
            lookupTable.add(routingTable[rand.nextInt(routingTable.length)], rand.nextInt(10));
        }
    }
```
This function randomly chooses 30,000 network prefixes from the master routingTable instantiated above and inserts them into the lookup list/trie. Instead of inserting the multiple values a routing table would usually hold, I chose to match each network prefix with a random number between 0 and 9 to represent the interface over which the packet must be sent. The randomly generated number are seeded, so the same values are inserted into each routing table. After populating the routing tables, it was time to test the lookup speed of each implementation.

### Results
```
Trie Method:
Time to populate table: 52020400ns
Destination: 1110100011100011, Longest Prefix: 111010001110*
Next hop on interface 6
Time to lookup longest matching prefix: 191900ns

List Method:
Time to populate table: 5580868700ns
Destination: 1110100011100011, Longest Prefix: 111010001110*
Next hop on interface 6
Time to lookup longest matching prefix: 8322500ns
```

**The trie method is vastly faster than the list method!** In terms of populating the table and searching for a longest matching prefix, the trie method is __~150X__ faster than the list method.

## Why Does it Matter?
The speeds listed above are in nanoseconds or .000000001 of a second. Populating the routing tables may be relatively slow, but what we as consumers care about is access to the internet as fast as possible. When the trie method is 150X faster than the list method, those nanoseconds add up. Next time you browse the internet, be thankful for algorithms like the trie method; without them, much of the internet activity today would be impossible at worst and infuriating at best.
![alt text](http://www.gotfunnypictures.com/wp-content/uploads/2014/07/internet-speed-rage.jpg)

(Credit to http://www.theoatmeal.com)
