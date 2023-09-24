package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    private double[] charOccurances;
    private double totalChars; 

    private Queue<TreeNode> source = new Queue<>(); 
    private Queue<TreeNode> target = new Queue<>(); 

    private TreeNode leftNode = new TreeNode();
    private TreeNode rightNode = new TreeNode();

    TreeNode tempSum = new TreeNode();  

    private boolean equal = false; 

    private String s = "";

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);
        charOccurances = new double[128];
        sortedCharFreqList = new ArrayList<>();
        while (StdIn.hasNextChar()) {
            charOccurances[StdIn.readChar()]++;
            totalChars+=1;
        }

        for (int i = 0; i < 128; i++) {
            if(charOccurances[i]/totalChars != 0)
            sortedCharFreqList.add(new CharFreq((char)i, charOccurances[i]/totalChars));
        }

        if(sortedCharFreqList.size() == 1){
            sortedCharFreqList.add(new CharFreq((char)(sortedCharFreqList.get(0).getCharacter() + 1), 0));
        }

        Collections.sort(sortedCharFreqList);

	/* Your code goes here */
    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {

        for (int i = 0; i < sortedCharFreqList.size(); i++){
            source.enqueue(new TreeNode(sortedCharFreqList.get(i), null, null));
        }

                leftNode = getLowestNode(); //Get lowest node 1
                rightNode = getLowestNode(); //Get lowest node 2

                tempSum.setData(new CharFreq(null, leftNode.getData().getProbOcc() + rightNode.getData().getProbOcc())); //Make new node with null character and summed prob
                tempSum.setLeft(leftNode);
                tempSum.setRight(rightNode);

                target.enqueue(tempSum);

                leftNode = new TreeNode();
                rightNode = new TreeNode();
                tempSum = new TreeNode();

        while (!source.isEmpty() && !target.isEmpty() || target.size() > 1){
                // leftNode = getLowestNode(); //Get lowest node 1
                
                // rightNode = getLowestNode(); //Get lowest node 2
                
                tempSum.setData(new CharFreq(null, leftNode.getData().getProbOcc() + rightNode.getData().getProbOcc())); //Make new node with null character and summed prob
                
                tempSum.setLeft(leftNode);
                tempSum.setRight(rightNode);
                target.enqueue(tempSum);

                leftNode = new TreeNode();
                rightNode = new TreeNode();
                tempSum = new TreeNode();
                //Queue target with new node and left node 1 and right node 2
        }

        huffmanRoot = target.dequeue();

	/* Your code goes here */
    }

    private TreeNode getLowestNode() {
        if (target.isEmpty())
            return source.dequeue();
        

        else if (target.size() > 1 && source.isEmpty())
            return target.dequeue();
        

        else if (target.size() == 1 && source.size() == 0)
            return target.dequeue();
        
        else if (target.size() > 0 && source.size() > 0) {
            
            if(source.peek().getData().getProbOcc() == target.peek().getData().getProbOcc()){
                equal = true; 
                // System.out.println(source.peek().getData().getCharacter());
                return source.dequeue(); 
            }

            else if (equal == true){
                equal = false; 
                // System.out.println(target.peek().getData().getCharacter());
                return target.dequeue();
            }

            else if (target.peek().getData().getProbOcc() > source.peek().getData().getProbOcc())
                return source.dequeue();
            

            else if (target.peek().getData().getProbOcc() < source.peek().getData().getProbOcc())
                return target.dequeue(); 

        }
        return null; 
        
    //so the one with all the decimals is greater, so shouldn't it just drop down to the greater method and work anyway
    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {
        encodings = new String[128];
        recursiveEncoding(huffmanRoot);
    }

    private void recursiveEncoding(TreeNode t){
         //WHile the left child is not null, go left
         //Go back up and right and repeat going all the way left

        if (t.getData().getCharacter() != null) {
        encodings[t.getData().getCharacter()] = s; 
        return;}

        if(t.getLeft() != null) {
        s += "0";
        recursiveEncoding(t.getLeft()); 
        s= s.substring(0, s.length() -1); }

        if(t.getRight() !=null) {
        s += "1";
        recursiveEncoding(t.getRight()); 
        s= s.substring(0, s.length() -1); }
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);
        String encoded = "";
        
        while(StdIn.hasNextChar())
        encoded += encodings[StdIn.readChar()]; 

        writeBitString(encodedFile, encoded);
        
	/* Your code goes here */
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);
        String bit = readBitString(encodedFile);
        int length = bit.length();
        TreeNode ptr = huffmanRoot; 
        for (int i = 0; i < length; i++){

            if (bit.substring(0, 1).equals("0")){
                // System.out.println("Went Left");
                if(ptr.getLeft() != null) {
                    ptr = ptr.getLeft();
                }
                bit = bit.substring(1, bit.length());
            }
            
            else if (bit.substring(0, 1).equals("1")){
                // System.out.println("Went Right"); 
                if(ptr.getRight() != null) {
                    ptr = ptr.getRight();
                }
                bit = bit.substring(1, bit.length());}

            if(ptr.getData().getCharacter() != null){
                StdOut.print(ptr.getData().getCharacter());
                ptr = huffmanRoot; 
            }
        }


	/* Your code goes here */
    }
    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
