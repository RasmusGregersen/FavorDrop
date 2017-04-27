package Firebase;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
/*
 * Created by Rasmus on 17-03-2017.
 */
public class FirebaseImpl {

    private FirebaseDatabase db;
    private FirebaseAuth auth;
    static DatabaseReference ref;


    public FirebaseImpl() {
        initializeFirebase();
    }

    private void initializeFirebase() {
//         Fetch the service account key JSON file contents
        try {
            File file = new File("serviceAccountKey.json");
            FileInputStream serviceAccount = new FileInputStream(file);
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

    public FirebaseAuth getAuth() {
        return auth;
    }


}
