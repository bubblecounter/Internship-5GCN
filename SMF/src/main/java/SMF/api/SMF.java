package SMF.api;

import SMF.models.NFProfile;
import java.util.ArrayList;
// SMF is a class with 2 threads
// One handles incoming request from other Network Functions
// The other one sends requests to other Network Functions
public class SMF {
    private NFProfile nfProfile;
    private SMFServer smfServer;
    public SMFClient smfClient;
    private static ArrayList<NFProfile> nfProfiles = new ArrayList<>(); //To Store nf profiles that are discovered

    public SMF(long smfId, int portNumber, NFProfile NRFProfile) {
        nfProfile = new NFProfile(smfId,"smf","https://localhost:"+ portNumber);
        smfServer = new SMFServer(portNumber);
        smfClient = new SMFClient(nfProfile,NRFProfile);
    }
    public SMF() {
    }

    public void connect(){
        smfServer.start();
        smfClient.start();
    }
}
