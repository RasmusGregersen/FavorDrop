import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/clients")
public class clients {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getClichedMessage() {
        String url = "https://favordrop.firebaseio.com/clients";
        return "";
    }
}
