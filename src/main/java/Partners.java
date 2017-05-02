import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;

/**
 * Created by LarsMyrup on 16/04/2017.
 */
@Path("/partners")
public class Partners {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPartners() {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/partners.json").request(MediaType.APPLICATION_JSON).get();
    }


    @GET
    @Secured
    @Path("/{pid}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPartnerOrders(@PathParam("pid") String pid) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/partners/" + pid + "/orders.json").request(MediaType.APPLICATION_JSON).get();
    }

    @GET
    @Path("/{pid}/orders/inservice")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPartnerInServiceOrders(@PathParam("pid") String pid) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/partners/" + pid + "/orders/inservice.json").request(MediaType.APPLICATION_JSON).get();
    }

    @PUT
    @Path("/{pid}/orders/inservice/{oid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setOrderInPartnerInService(String input, @PathParam("pid") String pid, @PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/partners/" + pid + "/orders/inservice/" + oid + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(input.toString()));
    }

    @DELETE
    @Path("/{pid}/orders/inservice/{oid}")
    public Response deleteOrderInPartnerInService(@PathParam("pid") String pid, @PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/partners/" + pid + "/orders/inservice/" + oid + ".json").request(MediaType.APPLICATION_JSON).delete();
    }

    @GET
    @Path("/{pid}/orders/completed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPartnerNewOrders(@PathParam("pid") String pid) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/partners/" + pid + "/orders/completed.json").request(MediaType.APPLICATION_JSON).get();
    }

    @PUT
    @Path("/{pid}/orders/completed/{oid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setOrderInPartnerCompleted(String input, @PathParam("pid") String pid, @PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        deleteOrderInPartnerInService(pid, oid);
        return client.target("https://favordrop.firebaseio.com/partners/" + pid + "/orders/completed/" + oid + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(input.toString()));
    }





}