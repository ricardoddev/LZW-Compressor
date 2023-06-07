import java.util.HashMap;
import java.util.Map;

public class LZW {
    private static final int WIDTH = 12;
    private static final int MAX = (1 << WIDTH) - 1;

    public void compress(String originalFilePath) {
        String compressedFilePath = originalFilePath.substring(0, originalFilePath.lastIndexOf(".")) + "_compressed.lzw";
        BinaryIn reader = new BinaryIn(originalFilePath);
        BinaryOut writer = new BinaryOut(compressedFilePath);

        Map<String, Integer> dict = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dict.put(Character.toString((char) i), i);
        }

        String current = "";
        int code = 256;

        while (!reader.isEmpty()) {
            char c = reader.readChar();
            current += c;

            if (!dict.containsKey(current)) {
                if (code <= MAX) {
                    dict.put(current, code++);
                }
                current = current.substring(0, current.length() - 1);
                writer.write(dict.get(current), WIDTH);
                current = Character.toString(c);
            }
        }

        writer.write(dict.get(current), WIDTH);
        writer.close();
    }

    public void decompress(String compressedFilePath) {
        String decompressedFilePath = compressedFilePath.substring(0, compressedFilePath.lastIndexOf(".")) + "_decompressed.txt";
        BinaryIn reader = new BinaryIn(compressedFilePath);
        BinaryOut writer = new BinaryOut(decompressedFilePath);

        Map<Integer, String> dict = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dict.put(i, Character.toString((char) i));
        }

        int current = reader.readInt(WIDTH);
        String value = dict.get(current);
        writer.write(value);
        int previous = current;
        int code = 256;

        while (!reader.isEmpty()) {
            current = reader.readInt(WIDTH);

            if (dict.containsKey(current)) {
                value = dict.get(current);
            } else {
                value = dict.get(previous);
                value = value + value.charAt(0);
            }

            writer.write(value);

            if (code <= MAX) {
                dict.put(code++, dict.get(previous) + value.charAt(0));
            }
            previous = current;
        }

        writer.close();
    }
}
