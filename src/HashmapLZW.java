public class HashmapLZW<K, V> {
    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private static final int INITIAL_CAPACITY = 256;
    private static final double LOAD_FACTOR = 0.75;

    public HashmapLZW() {
        table = new Node[INITIAL_CAPACITY];
        size = 0;
        capacity = INITIAL_CAPACITY;
    }

    public void put(K key, V value) {
        if(size >= capacity * LOAD_FACTOR){
            resize();
        }
        int index = hashCode(key);
        Node<K, V> head = table[index];

        // Verifica se a chave já existe
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        // Insere a chave se não existir
        size++;
        head = table[index];
        Node<K, V> newNode = new Node<K, V>(key, value, head);
        table[index] = newNode;
    }

    public V get(K key) {
        int index = hashCode(key);
        Node<K, V> head = table[index];

        // Procura a chave no bucket
        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }
            head = head.next;
        }

        // Retorna null se a chave não for encontrada
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    private int hashCode(K key) {
        return Math.abs(key.hashCode() % capacity);
    }

    public int size() {
        return size;
    }

    private void resize() {
        capacity *= 2;
        Node<K, V>[] oldTable = table;
        table = new Node[capacity];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
