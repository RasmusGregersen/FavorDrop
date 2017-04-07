package Firebase.Model;

/**
 * Created by Rasmus on 20-03-2017.
 */
public class Clients {
    public String name;
    public String address;
    // Vej
    // Husnummer
    // Etage (Valgfri)
    // Postnummer
    // By

    public Clients(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Clients() {
    }
}
