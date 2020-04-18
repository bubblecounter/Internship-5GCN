package AMF.api;

import AMF.models.NFProfile;

// In order to run this, you need the alpn-boot-XXX.jar in the bootstrap classpath.
public class main {

    public static void main(String[] args) {

        //default NF Profile of NRF
        //It is used to register AMF to the NRF so that it can be discovered by other Network Functions
        //The values need to be same with the values of NRF in the network
        NFProfile defaultNRFProfile = new NFProfile(545L,"nrf","https://localhost:8443");
        //the unique network function Id and port number for default AMF it can be changed
        long amf1Id = 352658555555555L;
        int amf1PortNumber = 5423;
        AMF Amf1 = new AMF(amf1Id,amf1PortNumber,defaultNRFProfile); //create AMF1
        Amf1.connect(); //start AMF1
    }
}
