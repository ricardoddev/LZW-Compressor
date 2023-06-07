import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LZW lzw = new LZW();

        System.out.println("Digite 1 para compactar ou 2 para descompactar:");
        int operation = scanner.nextInt();

        System.out.println("Digite o caminho do arquivo:");
        String path = scanner.next();

        if (operation == 1) {
            lzw.compress(path);
        } else if (operation == 2) {
            lzw.decompress(path);
        } else {
            System.out.println("Operação inválida.");
        }

        scanner.close();
    }
}
