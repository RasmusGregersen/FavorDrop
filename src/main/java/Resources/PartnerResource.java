package Resources;

import Annotations.Secured;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.io.StringReader;
import java.util.Iterator;

import static Resources.Welcome.baseURL;

/**
 * Created by LarsMyrup on 16/04/2017.
 */
@Path("/partners")
@Secured
public class PartnerResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPartners(@QueryParam("page") int page, @QueryParam("size") int size) throws JSONException {
        Client client = ClientBuilder.newClient();
        Response resp = client.target("https://favordrop.firebaseio.com/partners.json").request(MediaType.APPLICATION_JSON).get();

        String respStr = resp.readEntity(String.class);


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
                        JSONObject orders = setOrderSubtree((JSONObject) json.get(key), key);
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
                        if (!((JSONObject) json.get(key)).isNull("orders")) {
                            JSONObject orders = setOrderSubtree((JSONObject) json.get(key), key);
                            ((JSONObject) json.get(key)).put("orders", orders);
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
                output.put("Last page", baseURL + "/partners?page=" + lastPage + "&size=" + size);
            }
            if (counter - 1 >= pageEnd) {
                int nextPage = page + 1;
                output.put("Next page", baseURL + "/partners?page=" + nextPage + "&size=" + size);
            } else {
                output.put("Next page", "This is the last page");
            }

            return Response.status(200).entity(output.toString()).build();
        }
    }

    @GET
    @Secured
    @Path("/{pid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPartner(@PathParam("pid") String pid) throws JSONException {

        Client client = ClientBuilder.newClient();
        Response resp = client.target("https://favordrop.firebaseio.com/partners/"+ pid + ".json").request(MediaType.APPLICATION_JSON).get();

        String respString = resp.readEntity(String.class);

        if ("null".equals(respString)) {
            return Response.status(200).entity("null").build();
        }
        else {
            JSONObject json = new JSONObject((respString));
            if (!(json.isNull("orders"))) {
                JSONObject orders = setOrderSubtree(json, pid);
                json.put("orders", orders);
            }
            return Response.status(200).entity(json.toString()).build();
        }
    }

    @GET
    @Path("/{pid}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPartnerOrders(@PathParam("pid") String pid) throws JSONException {
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

    private JSONObject setOrderSubtree(JSONObject obj, String pid) throws JSONException {
        JSONObject orders = new JSONObject();
        orders.put("all orders", baseURL + "/partners/" + pid + "/orders");
        if (!(obj.getJSONObject("orders").isNull("inservice"))) {
            orders.put("orders in service", baseURL + "/partners/" + pid + "/orders/inservice");
        }
        else {
            orders.put("orders in service", "null");
        }
        if (!(obj.getJSONObject("orders").isNull("completed"))) {
            orders.put("completed orders", baseURL + "/partners/" + pid + "/orders/completed");
        }
        else {
            orders.put("completed orders", "null");
        }
        return orders;
    }





}