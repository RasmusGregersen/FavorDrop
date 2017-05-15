package Resources;

import Annotations.Secured;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.util.Iterator;

import static Resources.Welcome.baseURL;

/**
 * Created by LarsMyrup on 16/04/2017.
 */

@Path("/orders")
@Secured
public class OrderResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrders() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("New Orders", baseURL + "/orders/new");
        json.put("Orders In Service", baseURL + "/orders/inservice");
        json.put("Completed Orders", baseURL + "/orders/completed");
        json.put("Total Orders", getIndex());
        return Response.status(200).entity(json.toString()).header("Content-Type", "application/json").build();
    }

    @GET
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNewOrders(@QueryParam("page") int page, @QueryParam("size") int size) throws JSONException {
        Client client = ClientBuilder.newClient();

        Response resp = client.target("https://favordrop.firebaseio.com/orders/new.json").request(MediaType.APPLICATION_JSON).get();

        if (size == 0) {
            return resp;
        }
        else {
            JSONObject json = new JSONObject((resp.readEntity(String.class)));

            System.out.println(json);

            int pageStart = page * size;
            int pageEnd = pageStart + size;
            int counter = 0;
            JSONObject output = new JSONObject();

            Iterator<?> keys = json.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( json.get(key) instanceof JSONObject ) {
                    if (counter >= pageEnd) {
                        System.out.println("1");
                    }
                    else if (counter >= pageStart) {
                        output.put(key, json.get(key));
                        System.out.println("2");
                    }

                }
                counter++;
                System.out.println(counter);
            }

            if (page == 0) {
                output.put("Last page", "This is the first page");
            }
            else {
                int lastPage = page - 1;
                output.put("Last page", baseURL + "/orders/new?page=" + lastPage + "&size=" + size);
            }
            if (counter >= pageEnd) {
                int nextPage = page + 1;
                output.put("Next page", baseURL + "/orders/new?page=" + nextPage + "&size=" + size);
            }
            else {
                output.put("Next page", "This is the last page");
            }

            return Response.status(200).entity(output.toString()).build();
        }

    }

    @POST
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public int addNewOrder(String input) {
        Client client = ClientBuilder.newClient();
        int foo = getIndex();

        client.target("https://favordrop.firebaseio.com/orders/new/" + foo + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(input));

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

//    @GET
//    @Path("/new")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getNewOrders() {
//        Client client = ClientBuilder.newClient();
//        Response res = client.target("https://favordrop.firebaseio.com/orders/new.json").request(MediaType.APPLICATION_JSON).get();
//        return res;
//    }

    @GET
    @Path("/new/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNewOrder(@PathParam("id") String id) {
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
            Response resp = getNewOrder(oid);
            String responseAsString = resp.readEntity(String.class);
            output = new JSONObject(responseAsString);
            output.put("partner id", baseURL + "/partners/" + pid);
            output.put("acceptance time", accTime);
            uid = output.getString("client id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        uid = uid.substring(uid.lastIndexOf('/') + 1,uid.length());


        PartnerResource partners = new PartnerResource();
        partners.setOrderInPartnerInService(output.toString(), pid, oid);
        ClientResource clients = new ClientResource();
        clients.setClientOrderInService(output.toString(), uid, oid);
        deleteNewOrder(oid);


        return client.target("https://favordrop.firebaseio.com/orders/inservice/" + oid + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(output.toString()));
    }

    @GET
    @Path("/inservice")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdersInService(@QueryParam("page") int page, @QueryParam("size") int size) throws JSONException {
        Client client = ClientBuilder.newClient();

        Response resp = client.target("https://favordrop.firebaseio.com/orders/inservice.json").request(MediaType.APPLICATION_JSON).get();

        if (size == 0) {
            return resp;
        }
        else {
            JSONObject json = new JSONObject((resp.readEntity(String.class)));

            System.out.println(json);

            int pageStart = page * size;
            int pageEnd = pageStart + size;
            int counter = 0;
            JSONObject output = new JSONObject();

            Iterator<?> keys = json.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( json.get(key) instanceof JSONObject ) {
                    if (counter >= pageEnd) {
                        System.out.println("1");
                    }
                    else if (counter >= pageStart) {
                        output.put(key, json.get(key));
                        System.out.println("2");
                    }

                }
                counter++;
                System.out.println(counter);
            }

            if (page == 0) {
                output.put("Last page", "This is the first page");
            }
            else {
                int lastPage = page - 1;
                output.put("Last page", baseURL + "/orders/inservice?page=" + lastPage + "&size=" + size);
            }
            if (counter >= pageEnd) {
                int nextPage = page + 1;
                output.put("Next page", baseURL + "/orders/inservice?page=" + nextPage + "&size=" + size);
            }
            else {
                output.put("Next page", "This is the last page");
            }

            return Response.status(200).entity(output.toString()).build();
        }
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

    @GET
    @Path("/completed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdersCompleted(@QueryParam("page") int page, @QueryParam("size") int size) throws JSONException {
        Client client = ClientBuilder.newClient();

        Response resp = client.target("https://favordrop.firebaseio.com/orders/completed.json").request(MediaType.APPLICATION_JSON).get();

        if (size == 0) {
            return resp;
        }
        else {
            JSONObject json = new JSONObject((resp.readEntity(String.class)));

            System.out.println(json);

            int pageStart = page * size;
            int pageEnd = pageStart + size;
            int counter = 0;
            JSONObject output = new JSONObject();

            Iterator<?> keys = json.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( json.get(key) instanceof JSONObject ) {
                    if (counter >= pageEnd) {
                        System.out.println("1");
                    }
                    else if (counter >= pageStart) {
                        output.put(key, json.get(key));
                        System.out.println("2");
                    }

                }
                counter++;
                System.out.println(counter);
            }

            if (page == 0) {
                output.put("Last page", "This is the first page");
            }
            else {
                int lastPage = page - 1;
                output.put("Last page", baseURL + "/orders/completed?page=" + lastPage + "&size=" + size);
            }
            if (counter >= pageEnd) {
                int nextPage = page + 1;
                output.put("Next page", baseURL + "/orders/completed?page=" + nextPage + "&size=" + size);
            }
            else {
                output.put("Next page", "This is the last page");
            }

            return Response.status(200).entity(output.toString()).build();
        }
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

        uid = uid.substring(uid.lastIndexOf('/') + 1,uid.length());
        pid = pid.substring(pid.lastIndexOf('/') + 1,pid.length());

        PartnerResource partners = new PartnerResource();
        partners.setOrderInPartnerCompleted(output.toString(), pid, oid);
        ClientResource clients = new ClientResource();
        clients.setClientOrderCompleted(output.toString(), uid, oid);
        deleteInServiceOrder(oid);

        return client.target("https://favordrop.firebaseio.com/orders/completed/" + oid + ".json").request(MediaType.APPLICATION_JSON).put(Entity.json(output.toString()));
    }

}
