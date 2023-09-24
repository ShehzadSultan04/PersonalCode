package warehouse;

/*
 * Use this class to test the betterAddProduct method.
 */ 
public class BetterAddProduct {
    public static void main(String[] args) {
        StdIn.setFile(args[0]);
        StdOut.setFile(args[1]);

        Warehouse w = new Warehouse(); 

        int numProducts = StdIn.readInt();
        for(int i = 0; i < numProducts; i++){
            int day = StdIn.readInt(); 
            int ID = StdIn.readInt();
            String name = StdIn.readString(); 
            int stock = StdIn.readInt(); 
            int demand = StdIn.readInt();
            w.betterAddProduct(ID, name, stock, day, demand);
        }

        StdOut.print(w);
        
        // Use this file to test betterAddProduct
    }
}
