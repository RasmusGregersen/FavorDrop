package Firebase;

public class Main {
    public static void main(String[] args) {
        FirebaseImpl fb = new FirebaseImpl();

        fb.addCustomer();
        fb.addOrder();
        while(true) {}
    }
}
