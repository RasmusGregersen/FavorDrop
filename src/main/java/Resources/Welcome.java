package Resources;

import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.Calendar;

/**
 * Created by LarsMyrup on 02/05/2017.
 */

@Path("")
public class Welcome {
    //URL deployed:
    final static String baseURL = "http://52.213.91.0/FavorDrop_war";
    //URL for testing on localhost:
//    final static String baseURL = "http://localhost:8080/FavorDrop_war_exploded";

    @GET
    @Produces("application/json")
    public Response welcome() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("apiName", "FavorDrop REST api");
        json.put("servertime", Calendar.getInstance().getTime());
        json.put("company name", "Favor Drop I/S");
        json.put("url", "http://www.favordrop.dk");
        json.put("phone", "71 887 888");
        json.put("mail", "contact@favordrop.dk");

        JSONObject resources = new JSONObject();
        resources.put("Clients", baseURL + "/clients");
        resources.put("Orders", baseURL + "/orders");
        resources.put("Partners", baseURL + "/partners");

        json.putOpt("Resources:", resources);

        return Response.status(200).entity(json.toString()).build();
    }
}
