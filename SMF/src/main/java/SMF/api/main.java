package SMF.api;

import SMF.models.NFProfile;

public class main {
    public static SMF Smf1;

    public static void main(String[] args) {

        //default NF Profile of NRF
        //It is used to register SMF to the NRF so that it can be discovered by other Network Functions
        //The values need to be same with the values of NRF in the network
        NFProfile defaultNRFProfile = new NFProfile(545L,"nrf","https://localhost:8443");
        //the unique network function Id and port number for default AMF it can be changed
        long smf1Id = 222223434222222L;
        int smf1PortNumber = 6443;
        Smf1 = new SMF(smf1Id,smf1PortNumber,defaultNRFProfile); //create SMF1
        Smf1.connect(); //start SMF1
    }
}
