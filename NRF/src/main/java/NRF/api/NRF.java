package NRF.api;

import NRF.model.NFProfile;

// One handles incoming request from other Network Functions(SMF,UDM,AMF)
public class NRF{
    private NFProfile nfProfile;
    private NRFServer nrfServer;

    public NRF(long nrffId, int portNumber) {
        //Create Self Profile
        nfProfile = new NFProfile(nrffId,"nrf","https://localhost:"+ portNumber);
        nrfServer = new NRFServer(portNumber);
        //Add NRF Profile to send registration request
    }
    public NRF() {
    }

    public void connect(){
        nrfServer.createServer();
    }
}
