import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/clients")
public class Clients {

    @OPTIONS
    @Path("/{id}")
    public Response getOptions() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
    }

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
    public Response getClient(@PathParam("id") String id) {
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


    @GET
    @Path("/{id}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientOrders(@PathParam("id") String id) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + id + "/orders.json").request(MediaType.APPLICATION_JSON).get();
    }

    @PUT
    @Path("/{uid}/orders/new/{oid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewOrderToClient(String input, @PathParam("uid") String uid, @PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + uid + "/orders/new/" + oid + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(input));
    }

    @DELETE
    @Path("/{uid}/orders/new/{oid}")
    public Response deleteNewOrderInClient(@PathParam("uid") String uid, @PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + uid + "/orders/new/" + oid + ".json").request(MediaType.APPLICATION_JSON).delete();
    }

    @PUT
    @Path("/{uid}/orders/inservice/{oid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setClientOrderInService(String input, @PathParam("uid") String uid, @PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        deleteNewOrderInClient(uid, oid);
        return client.target("https://favordrop.firebaseio.com/clients/" + uid + "/orders/inservice/" + oid + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(input));
    }

    @DELETE
    @Path("/{uid}/orders/inservice/{oid}")
    public Response deleteInServiceOrderInClient(@PathParam("uid") String uid, @PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + uid + "/orders/inservice/" + oid + ".json").request(MediaType.APPLICATION_JSON).delete();
    }

    @GET
    @Path("/{id}/orders/completed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientOrdersCompleted(@PathParam("id") String id) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + id + "/orders/completed.json").request(MediaType.APPLICATION_JSON).get();
    }

    @PUT
    @Path("/{uid}/orders/completed/{oid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setClientOrderCompleted(String input, @PathParam("uid") String uid, @PathParam("oid") String oid) {
        Client client = ClientBuilder.newClient();
        deleteInServiceOrderInClient(uid, oid);
        return client.target("https://favordrop.firebaseio.com/clients/" + uid + "/orders/completed/" + oid + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(input));
    }





}

