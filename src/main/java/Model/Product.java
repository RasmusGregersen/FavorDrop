package Model;

/**
 * Created by Rasmus on 20-03-2017.
 */
public class Product {
    public String name;
    public String address;
    public String comment;

    public Product(String name, String address, String comment) {
        this.name = name;
        this.address = address;
        this.comment = comment;
    }

    public Product() {
    }
}
