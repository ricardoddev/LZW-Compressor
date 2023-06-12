public class HashmapLZW<K, V> {
    private Node<K, V>[] table; //tabela de armazenamento dos pares chave valor
    private int size; //tamanho da tabela
    private int capacity; //capacidade que vai sendo alterada
    private static final int INITIAL_CAPACITY = 256;
    private static final double LOAD_FACTOR = 0.75;

    public HashmapLZW() {
        table = new Node[INITIAL_CAPACITY];
        size = 0;
        capacity = INITIAL_CAPACITY;
    }

    public void put(K key, V value) { //acrescentar par na tabela
        if(size >= capacity * LOAD_FACTOR){ //sempre que o tamanho chegar a 75% da capacidade, a tabela dobra de tamanho
            resize();
        }
        int index = hashCode(key); //calcula o índice da nova chave
        Node<K, V> head = table[index]; //cria o novo par

        while (head != null) { //verifica se a chave já existe
            if (head.key.equals(key)) {
                head.value = value; //se existir, ele atualiza o valor
                return;
            }
            head = head.next;
        }

        //se não existir, ele aumenta de tamanho e cria um novo nó com o novo par chave valor
        size++;
        head = table[index];
        Node<K, V> newNode = new Node<K, V>(key, value, head);
        table[index] = newNode;
    }

    public V get(K key) { //retornar par que está na tabela
        int index = hashCode(key);
        Node<K, V> head = table[index];

        while (head != null) { //procura a chave no bucket
            if (head.key.equals(key)) {
                return head.value; //retorna chave se encontrar
            }
            head = head.next;
        }

        return null; //retorna null se não encontrar
    }

    public boolean containsKey(K key) { //verificar se um par está ou não na tabela
        return get(key) != null;
    }

    private int hashCode(K key) { //mapeia a criação de um índice
        return Math.abs(key.hashCode() % capacity); //por ser em módulo, sempre garante que o novo índice vai estar dentro da tabela
    }

    public int size() { //retornar tamanho da tabela
        return size;
    }

    private void resize() { //redimensionamento do vetor / é chamado quando a tabela está em 75% da sua capacidade
        capacity *= 2; //dobra a capacidade

        //cria uma nova tabela e insere todos os valores da tabela antiga na nova, que possui o dobro da capacidade
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
