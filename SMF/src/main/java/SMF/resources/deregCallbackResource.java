package SMF.resources;

import SMF.models.DeregistrationData;
import SMF.services.deregCallbackService;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

//Path for deregistration request coming from UDM
//Path and variables defined in the 5GCN specification
@Path("/udm-dereg/v1/{ueId}")
public class deregCallbackResource {
    @POST
    public Response deregUe(@PathParam("ueId") long ueId , DeregistrationData deregistrationData){
        return deregCallbackService.deregUe();
    }
}
