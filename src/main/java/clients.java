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
        Response res = client.target("https://favordrop.firebaseio.com/clients.json").request(MediaType.APPLICATION_JSON).get();
        return res;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addClient(String input) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients.json").request(MediaType.APPLICATION_JSON).post(Entity.json(input));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClient(String id) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/"+ id + ".json").request(MediaType.APPLICATION_JSON).get();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateClient(@PathParam("id") String id, String input) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/"+ id + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(input));
    }


    @Path("/{id}/orders")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientOrders(String id) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + id + "/Orders.json").request(MediaType.APPLICATION_JSON).get();
    }




}

