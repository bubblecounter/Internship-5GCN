package UDM.api;

import UDM.models.NFProfile;

// In order to run this, you need the alpn-boot-XXX.jar in the bootstrap classpath.
public class main {
    public static void main(String[] args) {

        //default NF Profile of NRF
        //It is used to register AMF to the NRF so that it can be discovered by other Network Functions
        //The values need to be same with the values of NRF in the network
        NFProfile defaultNRFProfile = new NFProfile(545L,"nrf","https://localhost:8443");
        //the unique network function Id and port number for default AMF it can be changed
        long udm1Id = 444444444444444L;
        int udm1PortNumber = 7443;
        UDM Udm1 = new UDM(udm1Id,udm1PortNumber,defaultNRFProfile); //create AMF1
        Udm1.connect(); //start AMF1
    }
}
