class OutOfStockException extends Exception{
    int shortFall;

    OutOfStockException(int shortFall) {
        super("Out of stock! Shortfall:"+shortFall);
        this.shortFall=shortFall;
    }
}

class InvalidQuantityException extends Exception{

    InvalidQuantityException(String msg) {
        super(msg);
    }
    
}

class Warehouse{
    int stock=10;

    void issue(String item,int qty)
        throws OutOfStockException,InvalidQuantityException{

            if(qty<=0){
                throw new InvalidQuantityException("Invalid quantity");
            }

            if(qty>stock){
                throw new OutOfStockException(qty-stock);
            }

            stock=stock-qty;
            System.out.println(qty+" "+item+" "+"issued successfully.");
        }
 }


public class StockIssue {
        public static void main(String[] args) {
            Warehouse w = new Warehouse();

            String[] items = {"Pen", "Book", "Pencil", "Bag"};
            int[] quantities = {3, 5, 7, -2};

            for (int i = 0; i < items.length; i++) {

                try {
                    w.issue(items[i], quantities[i]);

                } catch (OutOfStockException e) {
                    System.out.println(items[i] + ": " + e.getMessage());

                } catch (InvalidQuantityException e) {
                    System.out.println(items[i] + ": " + e.getMessage());
                }
            }

            System.out.println("All requests processed.");
        }
}
