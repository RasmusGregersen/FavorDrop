package Filters;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by LarsMyrup on 02/05/2017.
 */

@Provider
public class ResourceFilter implements ContainerResponseFilter
{
    @Override
    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException
    {
        if(request.getMethod().equals("OPTIONS"))
        {
            response.setStatus(Response.Status.OK.getStatusCode());
        }

        this.update(response);
    }

    private void update(ContainerResponseContext response)
    {
        MultivaluedMap<String, Object> headers = response.getHeaders();

        if(!headers.containsKey("Access-Control-Allow-Origin"))
        {
            headers.add("Access-Control-Allow-Origin", "*");
        }

        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
}