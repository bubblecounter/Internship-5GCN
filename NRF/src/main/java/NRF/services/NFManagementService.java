package NRF.services;

import Database.DatabaseConnection;
import NRF.model.NFProfile;
import NRF.model.SubscriptionData;
import javax.ws.rs.core.Response;

public class NFManagementService {
    private static DatabaseConnection db = new DatabaseConnection();
    public static Response retrieveNFInstanceList(String nfType, int limit){

        return db.retrieveNFInstanceList(nfType,limit);
    }
    public static Response createSubscription(SubscriptionData subscriptionData){

        return db.createSubscription(subscriptionData);
    }
    public static Response deleteSubscription(long subscriptionId){

        return db.deleteSubscription(subscriptionId);
    }
    public static Response retrieveNFInstance(long nfInstanceID){

        return db.retrieveNFInstance(nfInstanceID);
    }
    public static Response registerNFInstance(long nfInstanceID, NFProfile nfProfileInfo){

        return db.registerNFInstance(nfInstanceID,nfProfileInfo);
    }
    public  static Response deregisterNFInstance(long nfInstanceID){

        return db.deregisterNFInstance(nfInstanceID);
    }

    public static Response discoverNF(String targetNftype, String requesterNftype, String requesterNfInstanceFQDN){

        return db.discoverNF(targetNftype,requesterNftype,requesterNfInstanceFQDN);
    }
}
