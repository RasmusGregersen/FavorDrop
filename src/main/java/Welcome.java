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

@Path("/welcome")
public class Welcome {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response welcome() throws JSONException {
        JSONObject json = new JSONObject();
        json.append("apiName", "FavorDrop REST api");

        return Response.status(200).entity(json).build();
    }
}
