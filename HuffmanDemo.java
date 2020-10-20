/* The HuffmanDemo class will demonstrate encode method and decode method
 * <Jerry Guo> <2019.11.13>
 */
import java.io.*;
public class HuffmanDemo {
    public static void main(String[] args) throws IOException {
        try {
            Huffman.encode(); //encode
            System.out.println("\n* * * * *\n");
            Huffman.decode(); //decode
        } catch (Exception e) {
            System.out.println("Something went wrong. Please check your input and start over.");
        }
    }
}