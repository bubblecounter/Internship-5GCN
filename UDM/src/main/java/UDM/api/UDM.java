package UDM.api;

import UDM.models.NFProfile;

// UDM is a class with 2 threads
// One handles incoming request from other Network Functions
// The other one sends requests to other Network Functions
public class UDM {

    private NFProfile nfProfile; // to keep self network function profile to use in Server and Client side
    private UDMServer udmServer; // HAS-A relation
    private UDMClient udmClient; // HAS-A relation
    public UDM(long udmId, int portNumber, NFProfile NRFProfile) {
        nfProfile = new NFProfile(udmId,"amf","https://localhost:"+ portNumber);
        udmServer = new UDMServer(portNumber);
        udmClient = new UDMClient(portNumber,nfProfile,NRFProfile);
    }
    public UDM() {
    }

    public void connect(){
        udmServer.start();
        udmClient.start();
    }

}
