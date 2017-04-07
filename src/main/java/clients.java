import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/clients")
public class clients {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClichedMessage() {
        Client client = ClientBuilder.newClient();
        Response res = client.target("https://favordrop.firebaseio.com/customers.json").request(MediaType.APPLICATION_JSON).get();
        return res;
    }

    @POST @Consumes("application/json")
    public void addClient(JaxBean input) {
        Client client = ClientBuilder.newClient();
        client.target("https://favordrop.firebaseio.com/customers.json").request(MediaType.APPLICATION_JSON).post(Entity.json(input));
    }

    @XmlRootElement
    class JaxBean {
        @XmlElement public String address;
        @XmlElement public String name;
    }
}

