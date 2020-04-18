package SMF.resources;

import SMF.models.SMContextCreateData;
import SMF.services.pduSessionService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import javax.ws.rs.core.Response;

@Path("/nsmf-pdusession/v1/sm-contexts")

public class pduSessionResource {
    @POST
    public Response createSMContext(SMContextCreateData smContextCreateData){
        return  pduSessionService.createSMContext(smContextCreateData);
    }
}
