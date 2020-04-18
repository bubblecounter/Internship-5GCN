package AMF.resources;

import AMF.services.deregCallbackService;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

//Path for deregistration request coming from UDM
//Path and variables defined in the 5GCN specification
@Path("/udm-dereg/v1/{ueId}")
public class deregCallbackResource {
    @POST
    public Response notificationNF(){
        return deregCallbackService.deregUe();
    }
}
