import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by LarsMyrup on 16/04/2017.
 */
@Path("/orders")
public class orders {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrders() {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/orders.json").request(MediaType.APPLICATION_JSON).get();
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewOrder(String input) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/orders/new.json").request(MediaType.APPLICATION_JSON).post(Entity.json(input));
    }

    @GET
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNewOrders() {
        Client client = ClientBuilder.newClient();
        Response res = client.target("https://favordrop.firebaseio.com/orders/new.json").request(MediaType.APPLICATION_JSON).get();
        return res;
    }

    @GET
    @Path("/new/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNewOrder(String id) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/"+ id + ".json").request(MediaType.APPLICATION_JSON).get();
    }

    @POST
    @Path("/inservice")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response SetInService(String input) {
        Client client = ClientBuilder.newClient();



        return client.target("https://favordrop.firebaseio.com/orders/new.json").request(MediaType.APPLICATION_JSON).post(Entity.json(input));
    }


}
