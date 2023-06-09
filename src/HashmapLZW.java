public class HashmapLZW<K, V> {
    private Node<K, V>[] table;
    private int size;

    public HashmapLZW() {
        table = new Node[256];
        size = 0;
    }

    public void put(K key, V value) {
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
        return Math.abs(key.hashCode() % table.length);
    }

    public int size() {
        return size;
    }
}
