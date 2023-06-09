import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LZW lzw = new LZW();

        System.out.println("Digite 1 para compressão ou 2 para descompressão:");
        int operation = scanner.nextInt();

        System.out.println("Digite o caminho do arquivo:");
        String path = scanner.next();

        if (operation == 1) {
            lzw.compress(path);
            System.out.println("Compressão concluída. Cheque a pasta para verificar o arquivo comprimido.");
        } else if (operation == 2) {
            lzw.decompress(path);
            System.out.println("Descompressão concluída. Cheque a pasta para verificar o arquivo descomprimido.");
        } else {
            System.out.println("Operação inválida.");
        }

        scanner.close();
    }
}
