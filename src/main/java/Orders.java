import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;

/**
 * Created by LarsMyrup on 16/04/2017.
 */
@Path("/orders")
public class Orders {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrders() {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/orders.json").request(MediaType.APPLICATION_JSON).get();
    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public int addNewOrder(String input) {
        Client client = ClientBuilder.newClient();

        int foo = getIndex();

        client.target("https://favordrop.firebaseio.com/orders/new/" + foo + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(input));
        JSONObject jInput;
        String clientId = "";
        try {
            jInput = new JSONObject(input);
            clientId = jInput.getString("client id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.target("https://favordrop.firebaseio.com/clients/" + clientId + "/orders/new/" + foo + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(input));

        return foo;
    }

    public int getIndex() {
        Client client = ClientBuilder.newClient();
        String counter = client.target("https://favordrop.firebaseio.com/orders/totalorders.json").request(MediaType.TEXT_PLAIN).get().readEntity(String.class);

        int foo = Integer.parseInt(counter);
        foo++;
        client.target("https://favordrop.firebaseio.com/orders/totalorders.json").request(MediaType.TEXT_PLAIN).put(Entity.text(foo));
        return foo;

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
        return (client.target("https://favordrop.firebaseio.com/orders/new/" + id + ".json").request().get());
    }

    @DELETE
    @Path("/new/{oid}")
    public Response deleteNewOrder(@PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        return (client.target("https://favordrop.firebaseio.com/orders/new/" + oid + ".json").request(MediaType.APPLICATION_JSON).delete());
    }

    @PUT
    @Path("/inservice/{oid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response SetInService(String input, @PathParam("oid") String oid) throws JSONException {

        JSONObject Json = new JSONObject(input);

        Client client = ClientBuilder.newClient();

        JSONObject output = null;
        String pid = "";
        String uid = "";
        String accTime = "";

        try {
            pid = Json.getString("partner id");
            accTime = Json.getString("acceptance time");
            Response hej = getNewOrder(oid);
            String responseAsString = hej.readEntity(String.class);
            output = new JSONObject(responseAsString);
            output.put("partner id", pid);
            output.put("acceptance time", accTime);
            uid = output.getString("client id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("hej mor");

        Partners partners = new Partners();
        partners.setOrderInPartnerInService(output.toString(), pid, oid);
        Clients clients = new Clients();
        clients.setClientOrderInService(output.toString(), uid, oid);
        deleteNewOrder(oid);

        System.out.println("ska der ske");

        return client.target("https://favordrop.firebaseio.com/orders/inservice/" + oid + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(output.toString()));
    }

    @GET
    @Path("/inservice/{oid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInServiceOrder(@PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        return (client.target("https://favordrop.firebaseio.com/orders/inservice/" + oid + ".json").request().get());
    }

    @DELETE
    @Path("/inservice/{oid}")
    public Response deleteInServiceOrder(@PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        return (client.target("https://favordrop.firebaseio.com/orders/inservice/" + oid + ".json").request().delete());
    }

    @PUT
    @Path("/completed/{oid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response SetAsCompleted(String input, @PathParam("oid") String oid) throws JSONException {

        JSONObject Json = new JSONObject(input);

        Client client = ClientBuilder.newClient();

        JSONObject output = null;
        String pid = "";
        String uid = "";
        String endTime = "";
        String status = "";

        try {
            endTime= Json.getString("end time");
            status = Json.getString("status");
            Response hej = getInServiceOrder(oid);
            String responseAsString = hej.readEntity(String.class);
            output = new JSONObject(responseAsString);
            output.put("end time", endTime);
            output.put("status", status);
            uid = output.getString("client id");
            pid = output.getString("partner id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Partners partners = new Partners();
        partners.setOrderInPartnerCompleted(output.toString(), pid, oid);
        Clients clients = new Clients();
        clients.setClientOrderCompleted(output.toString(), uid, oid);
        deleteNewOrder(oid);

        return client.target("https://favordrop.firebaseio.com/orders/completed/" + oid + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(output.toString()));
    }

}
