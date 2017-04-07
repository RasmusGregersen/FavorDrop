package Firebase;
/*
import Firebase.Model.Clients;
import Firebase.Model.Order;
import Firebase.Model.Product;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

 * Created by Rasmus on 17-03-2017.
 */
public class FirebaseImpl {
/*
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    static DatabaseReference ref;


    public FirebaseImpl() {
        initializeFirebase();
    }

    private void initializeFirebase() {
        // Fetch the service account key JSON file contents
        try {
            FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");
            // Authenticate with Firebase.FirebaseImpl
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                    .setDatabaseUrl("https://favordrop.firebaseio.com/")
                    .build();
            FirebaseApp.initializeApp(options);
            db = FirebaseDatabase.getInstance();
            db.setLogLevel(Logger.Level.DEBUG);
            auth = FirebaseAuth.getInstance();
            ref = FirebaseDatabase.getInstance().getReference();
        } catch (IOException e) {
            System.out.println("Service Account credentials not found: ");
            e.printStackTrace();
        }

    }

    public DatabaseReference getRef() {
        return ref;
    }

    public void addCustomer() {
        DatabaseReference customerRef = ref.child("customers");
        String ID = customerRef.push().getKey();
        customerRef.child(ID).setValue(new Clients("Lasse Myrup","Vejlegårdsparken 2, 1. 17, 2665 Vallensbæk Strand"));
    }
    public void addOrder() {
        DatabaseReference orderRef = ref.child("orders");
        String ID = orderRef.push().getKey();
        ArrayList<Product> list = new ArrayList<Product>();
        list.add(new Product("Vare","Adresse","Kommentar"));
        orderRef.child(ID).setValue(new Order(23452543,2141345,list,50));
    }
    */
}
