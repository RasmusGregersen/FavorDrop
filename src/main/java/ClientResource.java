import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/clients")
public class ClientResource {

//    @OPTIONS
//    @Secured
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getOptions() {
//        System.out.println("hej med dig");
//        return Response.ok()
//                .header("Access-Control-Allow-Origin", "*")
//                .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
//                .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
//    }

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients() {
        Client client = ClientBuilder.newClient();
        Response res = client.target("https://favordrop.firebaseio.com/clients.json").request(MediaType.APPLICATION_JSON).get();
        return res;
    }

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addClient(String input) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients.json").request(MediaType.APPLICATION_JSON).post(Entity.json(input));
    }

    @GET
    @Secured
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClient(@PathParam("id") String id) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/"+ id + ".json").request(MediaType.APPLICATION_JSON).get();
    }

    @POST
    @Secured
    @Path("/{uid}/orders/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOrder(@PathParam("uid") String uid, String input) throws JSONException {
        Client client = ClientBuilder.newClient();

        JSONObject jsonObject = new JSONObject(input);
        jsonObject.put("client id", uid);

        String jsonStr = jsonObject.toString();

        OrderResource orders = new OrderResource();
        int oid = orders.addNewOrder(jsonStr);

        client.target("https://favordrop.firebaseio.com/clients/"+ uid + "/orders/new/" + oid + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(jsonStr));

        return Response.status(200).entity(oid).build();
    }

    @PUT
    @Secured
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateClient(@PathParam("id") String id, String input) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/"+ id + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(input));
    }


    @GET
    @Secured
    @Path("/{id}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientOrders(@PathParam("id") String id) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + id + "/orders.json").request(MediaType.APPLICATION_JSON).get();
    }

    @PUT
    @Secured
    @Path("/{uid}/orders/new/{oid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewOrderToClient(String input, @PathParam("uid") String uid, @PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + uid + "/orders/new/" + oid + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(input));
    }

    @DELETE
    @Secured
    @Path("/{uid}/orders/new/{oid}")
    public Response deleteNewOrderInClient(@PathParam("uid") String uid, @PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + uid + "/orders/new/" + oid + ".json").request(MediaType.APPLICATION_JSON).delete();
    }

    @PUT
    @Secured
    @Path("/{uid}/orders/inservice/{oid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setClientOrderInService(String input, @PathParam("uid") String uid, @PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        deleteNewOrderInClient(uid, oid);
        return client.target("https://favordrop.firebaseio.com/clients/" + uid + "/orders/inservice/" + oid + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(input));
    }

    @DELETE
    @Secured
    @Path("/{uid}/orders/inservice/{oid}")
    public Response deleteInServiceOrderInClient(@PathParam("uid") String uid, @PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + uid + "/orders/inservice/" + oid + ".json").request(MediaType.APPLICATION_JSON).delete();
    }

    @GET
    @Secured
    @Path("/{id}/orders/completed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientOrdersCompleted(@PathParam("id") String id) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + id + "/orders/completed.json").request(MediaType.APPLICATION_JSON).get();
    }

    @PUT
    @Secured
    @Path("/{uid}/orders/completed/{oid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setClientOrderCompleted(String input, @PathParam("uid") String uid, @PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        deleteInServiceOrderInClient(uid, oid);
        return client.target("https://favordrop.firebaseio.com/clients/" + uid + "/orders/completed/" + oid + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(input));
    }





}

