public class HashMap<K, V> implements HashMapInterface<K, V> {

    public Node<K, V>[] items;
    public int map_size;
    private int max_psl;
    private int total_psl;
    
    public HashMap(int size) {
       map_size = 0;
       items = (Node<K, V>[]) new Node[size * 2 - 3];
       max_psl = 0;
       total_psl = 0;
    }

    @Override
    public Node<K, V> get(K key) {
        int pos = findPos(key);

        return (pos == -1 ? null : items[pos]);
    } 
    
    private int findPos(K key) {
        int pos = Hash(key);
        if (map_size == 0) return -1;
        int mean_pos = total_psl / map_size; // calculate MEAN PSL.
        int down = mean_pos;
        int up = mean_pos + 1;

        if (items[pos] == null) return -1;
    
        while (up <= max_psl && down >= 0) {
            int down_loc = (down + pos) % items.length; 
            if (items[down_loc] != null){
                if (items[down_loc].key.equals(key)) {
                    return down_loc;
                }
            }

            int up_loc = (up + pos) % items.length;
            if (items[up_loc] != null){
                if (items[up_loc].key.equals(key)){
                    return up_loc;
                }
            }

            --down;
            ++up;
        }
        
        while (down >= 0) {
            int down_loc = (down + pos) % items.length;
            if (items[down_loc] != null) {
                if (items[down_loc].key.equals(key)) return down_loc;
            }
            --down;
        }

        while (up <= max_psl) {
            int up_loc = (up + pos) % items.length;
            if (items[up_loc] != null) {
                if (items[up_loc].key.equals(key)) return up_loc;
            }
            ++up;
        }

        return -1;
    }

    @Override
    public void put(K key, Node<K, V> node) {
        int pos = Hash(key);

        while (true) {
            if (items[pos] == null) {
                max_psl = max_psl < node.psl ? node.psl : max_psl;
                items[pos] = node;
                ++map_size;
                break;
            }
            else if (items[pos].psl < node.psl) {
                max_psl = max_psl < node.psl ? node.psl : max_psl;
                Node<K, V> temp = items[pos];
                items[pos] = node;
                node = temp;
            }

            pos = (pos + 1) % items.length;
            ++node.psl;
            //max_psl = max_psl < node.psl ? node.psl : max_psl;
            ++total_psl;
        }
    }

    @Override
    public void remove(K key) {

        int pos = findPos(key);
        int new_pos;
        if (pos == -1) return;

        while (true) {
            new_pos = (pos + 1) % items.length;
            if (items[new_pos] == null || items[new_pos].psl == 0) {
                items[pos] = null;
                --map_size;
                return;
            }

            items[pos] = items[new_pos];
            --items[pos].psl;
            --total_psl;

            pos = new_pos;
        }
    }

    public int Hash(K key){
        int hash = key.hashCode();
        return hash % items.length;
    }
}