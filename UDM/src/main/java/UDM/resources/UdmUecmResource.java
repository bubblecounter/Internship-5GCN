package UDM.resources;

import UDM.models.Amf3GppAccessRegistration;
import UDM.models.SmfRegistration;
import UDM.services.UdmUecmService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Path and variables defined in the 5GCN specification
@Path("/nudm-uecm/v1/{ueId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UdmUecmResource {

    @PUT
    @Path("/registrations/amf-3gpp-access")
    public Response registerAmf(@PathParam("ueId") long ueId, Amf3GppAccessRegistration amfRegisterInfo){
    return UdmUecmService.registerAmf(amfRegisterInfo,ueId);
    }

    @GET
    @Path("/registrations/amf-3gpp-access")
    public Response retrieveAmfRegistrationInfo(@PathParam("ueId") long ueId){
        return UdmUecmService.retrieveAmfRegistrationInfo(ueId);
    }

    @PUT
    @Path("/registrations/smf-registrations/{pduSessionId}")
    public Response registerSmf(@PathParam("ueId") long ueId, @PathParam("pduSessionId") long pduSessionId, SmfRegistration smfRegistrationInfo){
        return UdmUecmService.registerSmf(smfRegistrationInfo,ueId,pduSessionId);
    }

    @DELETE
    @Path("/registrations/smf-registrations/{pduSessionId}")
    public Response deregisterSmf(@PathParam("ueId") long ueId, @PathParam("pduSessionId") long pduSessionId) {
        return UdmUecmService.deregisterSmf(ueId,pduSessionId);
    }

}
