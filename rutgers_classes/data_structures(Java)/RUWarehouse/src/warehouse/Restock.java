package warehouse;

public class Restock {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);
        Warehouse w = new Warehouse();

        int numOfQueries = StdIn.readInt(); 

        for (int i = 0; i < numOfQueries; i++) {
            String word = StdIn.readString(); 
            if(word.equals("add")){
                int day = StdIn.readInt(); 
                int ID = StdIn.readInt();
                String name = StdIn.readString(); 
                int stock = StdIn.readInt(); 
                int demand = StdIn.readInt();
                w.addProduct(ID, name, stock, day, demand);
            }
            else {
                int ID = StdIn.readInt(); 
                int restockAmount = StdIn.readInt();
                w.restockProduct(ID, restockAmount);
            }
        }
        StdOut.print(w);
	// Uset his file to test restock
    }
}