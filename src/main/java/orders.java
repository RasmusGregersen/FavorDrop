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
        return (client.target("https://favordrop.firebaseio.com/orders/new/" + id + ".json").request(MediaType.APPLICATION_JSON).get());
    }

    @GET
    @Path("/new/{id}")
    public Response deleteNewOrder(String id) {
        Client client = ClientBuilder.newClient();
        return (client.target("https://favordrop.firebaseio.com/orders/new/" + id + ".json").request(MediaType.APPLICATION_JSON).delete());
    }

    @PUT
    @Path("/inservice/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response SetInService(String input, @PathParam("id") String id) throws JSONException {

        JSONObject Json = new JSONObject(new JSONTokener(input));

        Client client = ClientBuilder.newClient();
        String[] a = new String[3];
        try {
            a[0] = Json.getString("partner id");
            a[1] = Json.getString("partner name");
            a[2] = Json.getString("accepted time");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Response hej = getNewOrder(id);
        String responseAsString = hej.readEntity(String.class);

        JSONObject output = null;

        try {
            output = new JSONObject(responseAsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        output.put("partner id", a[0]);
        output.put("partner name", a[1]);
        output.put("accepted time", a[2]);

        deleteNewOrder(id);

        return client.target("https://favordrop.firebaseio.com/orders/inservice/" + id + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(output.toString()));
    }

    @GET
    @Path("/inservice/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInServiceOrder(String id) {
        Client client = ClientBuilder.newClient();
        return (client.target("https://favordrop.firebaseio.com/orders/inservice/" + id + ".json").request(MediaType.APPLICATION_JSON).get());
    }

    @DELETE
    @Path("/new/{id}")
    public Response deleteInServiceOrder(String id) {
        Client client = ClientBuilder.newClient();
        return (client.target("https://favordrop.firebaseio.com/orders/inservice/" + id + ".json").request(MediaType.APPLICATION_JSON).delete());
    }

    @PUT
    @Path("/completed/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response SetAsCompleted(String input, @PathParam("id") String id) throws JSONException {

        JSONObject Json = new JSONObject(new JSONTokener(input));

        Client client = ClientBuilder.newClient();
        String[] a = new String[3];
        try {
            a[0] = Json.getString("time");
            a[1] = Json.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response hej = getInServiceOrder(id);
        String responseAsString = hej.readEntity(String.class);

        JSONObject output = null;

        try {
            output = new JSONObject(responseAsString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        output.put("time", a[0]);
        output.put("status", a[1]);

        deleteInServiceOrder(id);

        return client.target("https://favordrop.firebaseio.com/orders/completed/" + id + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(output.toString()));
    }



}
