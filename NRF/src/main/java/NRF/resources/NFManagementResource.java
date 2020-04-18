package NRF.resources;

import NRF.model.NFProfile;
import NRF.model.SubscriptionData;
import NRF.services.NFManagementService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON})
@Path("/")
public class NFManagementResource {

   @Path("/nf-instances/")
   @GET
   public Response discoverNF(
            @QueryParam("target-nf-type") String targetNftype,
            @QueryParam("requester-nf-type") String requesterNftype,
            @QueryParam("requester-nf-instance-fqdn") String requesterNfInstanceFQDN){
       return NFManagementService.discoverNF(targetNftype,requesterNftype,requesterNfInstanceFQDN);
    }

    @Path("/nf-instances/{nfInstanceID}")
    public nfInstanceResource nfInstance( @PathParam("nfInstanceID") long nfInstanceID){
        return new nfInstanceResource();
    }

    @Path("/subscriptions")
    @POST
    public Response createSubscription(SubscriptionData subscriptionData) {
        return NFManagementService.createSubscription(subscriptionData);
    }

    @Path("/subscriptions/{subscriptionID}")
    @DELETE
    public Response deleteSubscription(@PathParam("subscriptionID") long subscriptionId){
        return NFManagementService.deleteSubscription(subscriptionId);
    }

    public class nfInstanceResource {

        @GET
        public Response retrieveNFInstance(@PathParam("nfInstanceID") long nfInstanceID){
            return NFManagementService.retrieveNFInstance(nfInstanceID);
        }

        @PUT
        public Response registerNFInstance(@PathParam("nfInstanceID") long nfInstanceID, NFProfile nfProfileInfo){
            return NFManagementService.registerNFInstance(nfInstanceID,nfProfileInfo);
        }

        @DELETE
        public Response deregisterNFInstance(@PathParam("nfInstanceID") long nfInstanceID){
            return NFManagementService.deregisterNFInstance(nfInstanceID);
        }
    }
}
