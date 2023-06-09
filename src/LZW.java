import java.util.HashMap;
import java.util.Map;

public class LZW {
    private static final int WIDTH = 12; //tamanho de 12 bits, permite que armazene até 4096 sequencias de caracteres diferentes
    private static final int MAX = (1 << WIDTH) - 1; //(1 << WIDTH) é uma operação equivalenete a 2**WIDTH / -1 para ober o maior código possível para a largura dada

    public void compress(String originalFilePath) {
        String compressedFilePath = originalFilePath.substring(0, originalFilePath.lastIndexOf(".")) + "_compressed.lzw"; //caminho do arquivo que será gerado -> nomeDoArquivo_compressed.lzw
        BinaryIn reader = new BinaryIn(originalFilePath); //ler arquivo original -> BIBLIOTECA DE PRINCETON
        BinaryOut writer = new BinaryOut(compressedFilePath); //escrever novo arquivo -> BIBLIOTECA DE PRINCETON

        HashmapLZW<String, Integer> dict = new HashmapLZW<>(); //dicionário com todos os caracteres ASCII, sendo o caractere com um código inteiro de 0 a 255
        for (int i = 0; i < 256; i++) {
            dict.put(Character.toString((char) i), i); //criando pares chave-valor com o dicionário - atribuindo cada número a um caractere
        }

        String current = ""; //valor atual que será lido (poderá se tornar um par)
        int code = 256; //novo código

        while (!reader.isEmpty()) { //enquanto o algoritmo continuar lendo
            char c = reader.readChar(); //caractere que está sendo lido
            current += c; //é adicionado a string current

            if (!dict.containsKey(current)) { //se não possui a sequencia atual
                if (code <= MAX) { //se ainda possuir espaço no dicionário
                    dict.put(current, code++); //adiciona a sequencia atual e incrementa o código
                }
                current = current.substring(0, current.length() - 1); //após adicionar a sequência ao dicionário, remove o último caractere da sequência atual porque esse caractere vai ser o início da próxima sequência, apenas se a sequência atual não existir no dicionário
                writer.write(dict.get(current), WIDTH); //escreve a sequência atual no arquivo comprimido
                current = Character.toString(c); //inicia uma nova sequência a partir do último caractere que foi lido e removido anteriormente
            }
        }

        writer.write(dict.get(current), WIDTH); //escreve as seuências restantes caso o dicionário não seja suficiente
        writer.close(); //fecha a aplicação
    }

    public void decompress(String compressedFilePath) {
        String decompressedFilePath = compressedFilePath.substring(0, compressedFilePath.lastIndexOf(".")) + "_decompressed.txt"; //caminho do arquivo que será gerado -> nomeDoArquivo_descompressed.lzw
        BinaryIn reader = new BinaryIn(compressedFilePath); //ler arquivo original -> BIBLIOTECA DE PRINCETON
        BinaryOut writer = new BinaryOut(decompressedFilePath); //escrever novo arquivo -> BIBLIOTECA DE PRINCETON

        HashmapLZW<Integer, String> dict = new HashmapLZW<>(); //dicionário com todos os caracteres ASCII, só que INVERTIDO (int, string)
        for (int i = 0; i < 256; i++) {
            dict.put(i, Character.toString((char) i)); //criando pares chave-valor com o dicionário - atribuindo cada número a um caractere
        }

        int current = reader.readInt(WIDTH); //leitura do arquivo por meio da classe reader, da biblioteca de princeton. Isso funciona pois o arquivo inteiro é representado por números inteiros, e ele faz a leitura desses números que representam uma sequência de caracteres
        String value = dict.get(current); //após a leitura do arquivo, aqui são retornadas as sequências de caracteres relacionadas ao inteiro que foi lido anteriormente
        writer.write(value); //aqui a string é escrita no arquivo que esta sendo descomprimido
        int previous = current; //inicia a variável previous como todos os dados do arquivo
        int code = 256;

        while (!reader.isEmpty()) { //enquanto não ler todos os inteiros
            current = reader.readInt(WIDTH); //current é definido como o próximo valor

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
