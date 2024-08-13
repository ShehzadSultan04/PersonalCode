package warehouse;

/*
 * Use this class to put it all together.
 */ 
public class Everything {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);
        Warehouse w = new Warehouse(); 

        int numberOfQueries = StdIn.readInt(); 

        for (int i = 0; i < numberOfQueries; i++) {
            String word = StdIn.readString(); 
            if(word.equals("add")){
                int day = StdIn.readInt(); 
                int ID = StdIn.readInt();
                String name = StdIn.readString(); 
                int stock = StdIn.readInt(); 
                int demand = StdIn.readInt();
                w.addProduct(ID, name, stock, day, demand);
            }
            else if (word.equals("restock")){
                int ID = StdIn.readInt(); 
                int restockAmount = StdIn.readInt();
                w.restockProduct(ID, restockAmount);
            }
            else if (word.equals("purchase")) {
                int day = StdIn.readInt(); 
                int ID = StdIn.readInt(); 
                int amount = StdIn.readInt();
                w.purchaseProduct(ID, day, amount);
            }
            else {
                int ID = StdIn.readInt(); 
                w.deleteProduct(ID);
            }
        }

        StdOut.print(w);

	// Use this file to test all methods
    }
}
