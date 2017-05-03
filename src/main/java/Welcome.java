import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;

/**
 * Created by LarsMyrup on 02/05/2017.
 */

@Path("")
public class Welcome {

    @GET
    @Produces("application/json")
    public String welcome() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("apiName", "FavorDrop REST api");
        json.put("servertime", Calendar.getInstance().getTime());
        json.put("time zone", "UTC+01:00");
        json.put("company name", "Favor Drop I/S");
        json.put("url", "http://www.favordrop.dk");
        json.put("phone", "71 887 888");
        json.put("mail", "contact@favordrop.dk");

        return json.toString();
    }
}
