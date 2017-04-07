package Model;

/**
 * Created by Rasmus on 20-03-2017.
 */
public class Customer {
    public String name;
    public String address;
    // Vej
    // Husnummer
    // Etage (Valgfri)
    // Postnummer
    // By

    public Customer(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Customer() {
    }
}
