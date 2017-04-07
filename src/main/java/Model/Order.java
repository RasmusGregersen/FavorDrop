package Model;

import java.util.ArrayList;

/**
 * Created by Rasmus on 20-03-2017.
 */
public class Order {
    public int customer_id;
    public int partner_id;
    public ArrayList<Product> products;
    public int price;

    public Order(int customer_id, int partner_id, ArrayList<Product> products, int price) {
        this.customer_id = customer_id;
        this.partner_id = partner_id;
        this.products = products;
        this.price = price;
    }

    public Order() {
    }
}
