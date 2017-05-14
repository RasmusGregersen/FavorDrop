package Resources;

import Annotations.PATCH;
import Annotations.Secured;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Iterator;

import static Resources.Welcome.baseURL;

@Path("/clients")
public class ClientResource {

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients(@QueryParam("page") int page, @QueryParam("size") int size) throws JSONException {
        Client client = ClientBuilder.newClient();
        Response resp = client.target("https://favordrop.firebaseio.com/clients.json").request(MediaType.APPLICATION_JSON).get();

        String respStr = (resp.readEntity(String.class));

        if ("null".equals(respStr)) {
            return Response.status(200).entity("null").build();
        }
        else if (size == 0) {
            JSONObject json = new JSONObject(respStr);
            Iterator<?> keys = json.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( json.get(key) instanceof JSONObject ) {
                    if (!((JSONObject) json.get(key)).isNull("orders")) {
                        JSONObject orders = setOrderSubtree((JSONObject)json.get(key), key);
                        ((JSONObject) json.get(key)).put("orders", orders);
                    }
                }
            }

            return Response.status(200).entity(json.toString()).build();
        }
        else {
            JSONObject json = new JSONObject(respStr);
            int pageStart = page * size;
            int pageEnd = pageStart + size;
            int counter = 0;
            JSONObject output = new JSONObject();

            Iterator<?> keys = json.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (json.get(key) instanceof JSONObject) {
                    if (counter >= pageEnd) {
                        System.out.println("1");
                    } else if (counter >= pageStart) {
                        output.put(key, json.get(key));
                        if (!((JSONObject) output.get(key)).isNull("orders")) {
                            ((JSONObject) json.get(key)).put("orders", setOrderSubtree((JSONObject)json.get(key), key));
                        }
                    }
                }
                counter++;
                System.out.println(counter);
            }

            if (page == 0) {
                output.put("Last page", "This is the first page");
            } else {
                int lastPage = page - 1;
                output.put("Last page", baseURL + "/clients?page=" + lastPage + "&size=" + size);
            }
            if (counter - 1 >= pageEnd) {
                int nextPage = page + 1;
                output.put("Next page", baseURL + "/clients?page=" + nextPage + "&size=" + size);
            } else {
                output.put("Next page", "This is the last page");
            }

            return Response.status(200).entity(output.toString()).build();
        }
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
    @Path("/{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClient(@PathParam("uid") String uid) throws JSONException {

        Client client = ClientBuilder.newClient();
        Response resp = client.target("https://favordrop.firebaseio.com/clients/"+ uid + ".json").request(MediaType.APPLICATION_JSON).get();

        String respString = resp.readEntity(String.class);

        if ("null".equals(respString)) {
            return Response.status(200).entity("null").build();
        }
        else {
            JSONObject json = new JSONObject((respString));
            if (!(json.isNull("orders"))) {
                JSONObject orders = setOrderSubtree(json, uid);
                json.put("orders", orders);
            }
            return Response.status(200).entity(json.toString()).build();
        }
    }

    @POST
    @Secured
    @Path("/{uid}/orders/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOrder(@PathParam("uid") String uid, String input) throws JSONException {
        Client client = ClientBuilder.newClient();

        JSONObject jsonObject = new JSONObject(input);
        jsonObject.put("client id", baseURL + "/clients/"+ uid);

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

    @PATCH
    @Secured
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response patchClient(@PathParam("id") String id, String input) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/"+ id + ".json").request(MediaType.APPLICATION_JSON).header("X-HTTP-Method-Override", "PATCH").post(Entity.json(input));
    }


    @GET
    @Secured
    @Path("/{uid}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientOrders(@PathParam("uid") String uid) throws JSONException {
        Client client = ClientBuilder.newClient();
        Response resp = client.target("https://favordrop.firebaseio.com/clients/" + uid + ".json").request(MediaType.APPLICATION_JSON).get();
        String respStr = (resp.readEntity(String.class));
        System.out.println("respStr er: " + respStr);
        JSONObject json = new JSONObject(respStr);
        JSONObject orders = setOrderSubtree(json, uid);

        return Response.status(200).entity(orders.toString()).build();
    }

    @GET
    @Secured
    @Path("/{uid}/orders/new")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientOrdersNew(@PathParam("uid") String uid, @QueryParam("page") int page, @QueryParam("size") int size) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + uid + "/orders/new.json").request(MediaType.APPLICATION_JSON).get();
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
    @GET
    @Secured
    @Path("/{uid}/orders/inservice")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientOrdersInService(@PathParam("uid") String uid) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + uid + "/orders/inservice.json").request(MediaType.APPLICATION_JSON).get();
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
    @Path("/{uid}/orders/completed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientOrdersCompleted(@PathParam("uid") String uid) {
        Client client = ClientBuilder.newClient();
        return client.target("https://favordrop.firebaseio.com/clients/" + uid + "/orders/completed.json").request(MediaType.APPLICATION_JSON).get();
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

    private JSONObject setOrderSubtree(JSONObject obj, String uid) throws JSONException {
        JSONObject orders = new JSONObject();
        orders.put("all orders", baseURL + "/clients/" + uid + "/orders");
        if (!(obj.getJSONObject("orders").isNull("new"))) {
            System.out.println("der er nye ordre");
            orders.put("new orders", baseURL + "/clients/" + uid + "/orders/new");
        }
        else {
            System.out.println("der er ikke nye ordre");
            orders.put("new orders", "null");
        }
        if (!(obj.getJSONObject("orders").isNull("inservice"))) {
            orders.put("orders in service", baseURL + "/clients/" + uid + "/orders/inservice");
        }
        else {
            orders.put("orders in service", "null");
        }
        if (!(obj.getJSONObject("orders").isNull("completed"))) {
            orders.put("completed orders", baseURL + "/clients/" + uid + "/orders/completed");
        }
        else {
            orders.put("completed orders", "null");
        }
        return orders;
    }





}

