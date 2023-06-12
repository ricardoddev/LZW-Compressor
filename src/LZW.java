import java.util.NoSuchElementException;

public class LZW {
    private static final int WIDTH = 12; //tamanho de 12 bits, permite que armazene até 4096 sequencias de caracteres diferentes
    private static final int MAX = (1 << WIDTH) - 1; //(1 << WIDTH) é uma operação equivalenete a 2**WIDTH / -1 para ober o maior código possível para a largura dada

    public void compress(String originalFilePath) {
        String compressedFilePath = originalFilePath.substring(0, originalFilePath.lastIndexOf(".")) + "_compressed.lzw"; //caminho do arquivo que será gerado -> nomeDoArquivo_compressed.lzw
        BinaryIn reader = new BinaryIn(originalFilePath); //ler arquivo original -> BIBLIOTECA DE PRINCETON
        BinaryOut writer = new BinaryOut(compressedFilePath); //escrever novo arquivo -> BIBLIOTECA DE PRINCETON

        HashmapLZW<String, Integer> dict = new HashmapLZW<>(); //dicionário com todos os caracteres ASCII, sendo o caractere com um código inteiro de 0 a 255
        for (int i = 0; i < 256; i++) {
            dict.put(Character.toString((char) i), i); //criando pares chave-valor do dicionário baseado na tabela ASCII
        }

        String current = ""; //valor atual que será lido (poderá se tornar um par)
        int code = 256; //novo código

        while (!reader.isEmpty()) { //enquanto o algoritmo continuar lendo
            char c = reader.readChar(); //caractere que está sendo lido
            current += c; //é adicionado a string current

            if (!dict.containsKey(current)) { //se não possui a sequencia atual
                if (code <= MAX) { //se ainda possuir memória no dicionárior
                    dict.put(current, code++); //adiciona a sequencia atual e incrementa o código
                }
                if (!current.isEmpty()) {
                    current = current.substring(0, current.length() - 1); //após adicionar a sequência ao dicionário, remove o último caractere da sequência atual porque esse caractere vai ser o início da próxima sequência, apenas se a sequência atual não existir no dicionário
                }
                writer.write(dict.get(current), WIDTH); //escreve a sequência atual no arquivo comprimido
                current = Character.toString(c); //inicia uma nova sequência a partir do último caractere que foi lido e removido anteriormente
            }
        }

        writer.write(dict.get(current), WIDTH); //escreve as seuências restantes caso o dicionário não seja suficiente
        writer.close(); //fecha a aplicação
    }

    public void decompress(String compressedFilePath) {
        String decompressedFilePath = compressedFilePath.substring(0, compressedFilePath.lastIndexOf(".")) + "_decompressed.txt"; //caminho do arquivo e acrescenta _descompressed.txt
        BinaryIn reader = new BinaryIn(compressedFilePath); //importação do leitor do arquivo
        BinaryOut writer = new BinaryOut(decompressedFilePath); //importação do que vai escrever o novo arquivo

        HashmapLZW<Integer, String> dict = new HashmapLZW<>(); //criação do dicionario
        for (int i = 0; i < 256; i++) {
            dict.put(i, Character.toString((char) i)); //isso aqui adiciona todos os caracteres ASCII possíveis, se tiverem todos
        }

        int previous = reader.readInt(WIDTH); //percorre o arquivo para realizar a leitura das CHAVES e armazena nessa variavel
        String value = dict.get(previous); //aqui ele pega o valor das chaves e armazena em value
        writer.write(value); //escreve value no novo arquivo
        int code = 256; //a próxima chave tem que ser 256 já que já temos até a 255

        while (true) { //esse while(true) está aqui para realizar uma prevenção de erros
            int current;
            try {
                current = reader.readInt(WIDTH); //se ainda tiver dados para ler, ele realiza a leitura aqui
            } catch (NoSuchElementException e) {
                break; //se ele encontrar um erro, ele para a execução do loop
            }

            if (dict.containsKey(current)) { //se tiver no dicionario
                value = dict.get(current); //já temos essa sequência salva, então só retornamos ela
            } else { //se não tivermos no dicionário
                value = dict.get(previous);
                value = value + value.charAt(0); //adiciona o primeiro caractere da sequência ao final, formando a nova sequência
            }

            writer.write(value); //independente se tiver a sequencia no dicionário ou não, o caractere é escrito no arquivo

            if (code <= MAX) { //verifica se o próximo código gerado ainda cabe no limite de bits
                dict.put(code++, dict.get(previous) + value.charAt(0)); //adiciona no dicionário o código inteiro dele e a string da sequencia anterior(previous) mais o primeiro caractere da sequencia atual (int, string)
            }
            previous = current; //current atual se torna o antigo e o loop executa novamente
        }

        writer.close(); //fecha a aplicação
    }
}
