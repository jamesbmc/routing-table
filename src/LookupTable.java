
public interface LookupTable {
    public void add(String address, int iface);
    
    public void search(String address);
    
    public void searchDetailed(String address);
}
