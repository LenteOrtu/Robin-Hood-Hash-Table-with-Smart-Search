public interface HashMapInterface<K, V>{
    Node<K, V> get(K key);
    void put(K key, Node<K, V> node);
    void remove(K key);
}