import Filters.AuthenticationFilter;
import Filters.OptionsFilter;
import Resources.ClientResource;
import Resources.OrderResource;
import Resources.PartnerResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

//Defines the base URI for all resource URIs.
@ApplicationPath("/")
//The java class declares root resource and provider classes
public class REST extends Application{
    //The method returns a non-empty collection with classes, that must be included in the published JAX-RS application
    @Override
    public Set<Class<?>> getClasses() {
        HashSet h = new HashSet<Class<?>>();
        h.add( ClientResource.class );
        h.add ( OrderResource.class );
        h.add ( PartnerResource.class);
        h.add ( AuthenticationFilter.class );
        h.add ( Welcome.class );
        h.add ( OptionsFilter.class );

        return h;
    }
}
