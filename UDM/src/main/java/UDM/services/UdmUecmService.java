package UDM.services;

import Database.DatabaseConnection;
import UDM.models.Amf3GppAccessRegistration;
import UDM.models.SmfRegistration;
import javax.ws.rs.core.Response;

public class UdmUecmService {
    public static Response registerAmf(Amf3GppAccessRegistration amfInfo , long ueId) {
        DatabaseConnection db = new DatabaseConnection();
        return db.insertAmfRegistrationInfo(amfInfo,ueId);
    }
    public static Response retrieveAmfRegistrationInfo(long ueId){
        DatabaseConnection db = new DatabaseConnection();
        return db.retrieveAmfRegistrationInfo(ueId);
    }

    public static Response registerSmf(SmfRegistration smfRegistrationInfo,long ueId, long pduSessionId){
        DatabaseConnection db = new DatabaseConnection();
        return db.insertSmfRegistrationInfo(smfRegistrationInfo,ueId,pduSessionId);
    }

    public static Response deregisterSmf(long ueId, long pduSessionId){
        DatabaseConnection db = new DatabaseConnection();
        return db.deleteSmfRegistrationInfo(ueId,pduSessionId);
    }
}
