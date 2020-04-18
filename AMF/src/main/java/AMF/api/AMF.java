package AMF.api;

import AMF.models.NFProfile;

// AMF is a class with 2 threads
// One handles incoming request from other Network Functions
// The other one sends requests to other Network Functions
public class AMF {

    private NFProfile nfProfile; // to keep self network function profile to use in Server and Client side
    private AMFServer amfServer; // HAS-A relation
    private AMFClient amfClient; // HAS-A relation

    public AMF(long amfId, int portNumber, NFProfile NRFProfile) {
        nfProfile = new NFProfile(amfId,"amf","https://localhost:"+ portNumber);
        amfServer = new AMFServer(portNumber);
        amfClient = new AMFClient(portNumber,nfProfile,NRFProfile);
    }

    public AMF() {
    }

    public void connect(){
        amfServer.start();
        amfClient.start();
    }

}
