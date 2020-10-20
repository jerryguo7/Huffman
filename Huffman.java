/* The Huffman class will provides methods to encode and decode
 * text files according to the Huffman coding algorithm
 * <Jerry Guo> <2019.11.13>
 */
import java.io.*;
import java.util.*;
public class Huffman {
    public static void encode()throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the filename to read from/encode: ");
        String filename = scan.nextLine();
        File file = new File(filename);
        Scanner inputFile = new Scanner(file);

        int[] freq = new int[256]; //ASCII array
        int length = 0; //length of chars
        while (inputFile.hasNext()) {
            String line = inputFile.nextLine(); //Read a line of words
            String[] words = line.split(" "); //Split the line into a words array
            for (String text : words) {
                //Non-whitespace character in the text String
                char[] chars = text.replaceAll("\\s", "").toCharArray();
                for (char c : chars) {
                    freq[c]++; //Count frequencies
                    length++; //Count the length of chars
                }
            }
        }
        inputFile.close();

        //ArrayList which includes Pairs
        ArrayList<Pair> pairArrayList = new ArrayList<>();
        for (int i=0; i<freq.length; i++) {
            //Derive the relative probabilities of the characters
            double probNum = Math.round(freq[i]*10000d/length)/10000d;
            //Assign the ASCII characters and probabilities
            Pair pair = new Pair((char)i, probNum);
            //Add the pair with nonzero probabilities
            if (probNum != 0) pairArrayList.add(pair);
        }

        //Copy of pairArrayList
        ArrayList<Pair> pairArrayList2 = new ArrayList<>(pairArrayList);
        Collections.sort(pairArrayList); //Sort the pairArrayList in ascending order

        ArrayList <BinaryTree<Pair>> S = new ArrayList <>(); //Queue S
        ArrayList <BinaryTree<Pair>> T = new ArrayList <>(); //Queue T
        //Enqueue Pairs into queue S
        for (Pair value : pairArrayList) {
            BinaryTree<Pair> binaryTree = new BinaryTree<>();
            binaryTree.makeRoot(value);
            S.add(binaryTree);
        }

        //Build trees until queue S is empty
        while (S.size() != 0) {
            BinaryTree<Pair> A = getPairBinaryTree(S, T);
            BinaryTree<Pair>  B = getPairBinaryTree(S, T);
            combineTwoTrees(T, A, B);
        }

        //Combine tree until queue T's size is 1
        while (T.size() != 1) {
            BinaryTree<Pair> A = T.remove(0);
            BinaryTree<Pair>  B = T.remove(0);
            combineTwoTrees(T, A, B);
        }

        //Get all the non-null Huffman codes
        ArrayList<String> encoding = new ArrayList<>();
        for (int i=0; i<findEncoding(T.get(0)).length; i++) {
            if (findEncoding(T.get(0))[i] != null) {
                encoding.add(findEncoding(T.get(0))[i]);
            }
        }

        //Print derived codes to an output file called Huffman.txt
        System.out.println("Printing codes to Huffman.txt");
        PrintWriter output = new PrintWriter("Huffman.txt");
        output.println("Symbol Prob.\tHuffman\t\tCode");
        for (int i=0; i<pairArrayList2.size(); i++) {
            output.println(pairArrayList2.get(i) + "\t\t" + encoding.get(i));
        }
        output.close();

        //Encode each character in the text using the Huffman codes that have derived
        System.out.println("Printing encoded text to Encoded.txt");
        output = new PrintWriter("Encoded.txt");
        File file2 = new File("Huffman.txt");
        inputFile = new Scanner(file);
        while (inputFile.hasNext()) {
            String line = inputFile.nextLine(); //Read a line of words
            String[] words = line.split(" "); //Split the line into a words array
            for (String text : words) {
                //Non-whitespace character in the text String
                char[] chars = text.replaceAll("\\s", "").toCharArray();
                for (char c : chars) {
                    Scanner inputFile2 = new Scanner(file2);
                    inputFile2.nextLine();
                    while (inputFile2.hasNext()) {
                        String line2 = inputFile2.nextLine(); //Read a line of words
                        //Split the line into a words array
                        String[] words2 = line2.split("\t\t");
                        //Get the corresponding Huffman codes in Huffman.txt
                        char chars2 = words2[0].charAt(0);
                        if (chars2 == c)  output.print(words2[2]);
                    }
                    inputFile2.close();
                }
                output.print(" "); //Print a space between two words
            }
            output.println(); //Start a new line
        }
        inputFile.close();
        output.close();

    }

    public static void decode()throws IOException{
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the filename to read from/decode: ");
        String filename = scan.nextLine();
        File file = new File(filename);
        Scanner inputFile = new Scanner(file);

        System.out.print("Enter the filename of document containing Huffman codes: ");
        String filename2 = scan.nextLine();
        File codes = new File(filename2);

        System.out.println("Printing decoded text to Decoded.txt");
        PrintWriter output = new PrintWriter("Decoded.txt");
        String hold = "";

        while (inputFile.hasNext()) {
            String line = inputFile.nextLine(); //Read a line of words
            String[] words = line.split(" "); //Split the line into a words array
            for (String text : words) {
                //Non-whitespace character in the text String
                char[] chars = text.replaceAll("\\s", "").toCharArray();
                for (char c : chars) {
                    hold += c; //Append the character to the string
                    Scanner ls = new Scanner(codes);
                    ls.nextLine();
                    while (ls.hasNext()) {
                        String s = ls.nextLine(); //Read a line of words
                        //Split the line into a words array
                        String[] words2 = s.split("\t\t");
                        //Get the corresponding characters in Huffman.txt
                        char chars2 = words2[0].charAt(0);
                        if (words2[2].equals(hold)) {
                            output.print(chars2);
                            hold = ""; //Clear the string for the next word
                        }
                    }
                    ls.close();
                }
                output.print(" "); //Print a space between two words
            }
            output.println(); //Start a new line
        }
        inputFile.close();
        output.close();

    }

    //Dequeue a BinaryTree<Pair> from queue S or queue T
    private static BinaryTree<Pair> getPairBinaryTree(ArrayList<BinaryTree<Pair>> S, ArrayList<BinaryTree<Pair>> T) {
        BinaryTree<Pair> binaryTree;
        //Dequeue from S when T is empty
        if (T.size() == 0) binaryTree = S.remove(0);
        //Dequeue from T when S is empty
        else if (S.size() == 0) binaryTree = T.remove(0);
        //Find the smaller weight tree of the trees in front of S and in front of T
        else {
            if (S.get(0).getData().getProb() < T.get(0).getData().getProb()) {
                binaryTree = S.remove(0);
            }
            else binaryTree = T.remove(0);
        }
        return binaryTree;
    }

    //Construct a new tree P by creating a root
    //and attaching A and B as the subtrees of this root
    private static void combineTwoTrees(ArrayList<BinaryTree<Pair>> T, BinaryTree<Pair> A, BinaryTree<Pair> B) {
        //Construct a new tree P
        BinaryTree<Pair> P = new BinaryTree<>();
        //The weight of the root is the combined weights of the roots of A and B
        double probNum = A.getData().getProb() + B.getData().getProb();
        //Create a root
        Pair pair = new Pair('⁂', probNum);
        P.makeRoot(pair);
        //Attach A and B as the subtrees of this root
        P.attachLeft(A);
        P.attachRight(B);
        T.add(P); //Enqueue tree P to queue T
    }

    //Simplify the findEncoding method
    private static String[] findEncoding(BinaryTree<Pair> bt){
        String[] result = new String[256];
        findEncoding(bt, result, "");
        return result;
    }

    //Derive the Huffman codes
    private static void findEncoding(BinaryTree<Pair> bt, String[] a, String prefix){
        // test is node/tree is a leaf
        if (bt.getLeft()==null && bt.getRight()==null){
            a[bt.getData().getValue()] = prefix;
        }
        // recursive calls
        else{
            findEncoding(bt.getLeft(), a, prefix+"0");
            findEncoding(bt.getRight(), a, prefix+"1");
        }
    }

}
