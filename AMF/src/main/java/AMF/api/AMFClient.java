package AMF.api;

import AMF.models.Amf3GppAccessRegistration;
import AMF.models.NFProfile;
import AMF.models.SMContextCreateData;
import AMF.models.SubscriptionData;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.exit;
import static org.eclipse.jetty.util.ssl.SslContextFactory.TRUST_ALL_CERTS;

//AMFClient sends requests to network functions in the network (SMF,NRF,UDM)
//Also to try request and responses it prints a menu on terminal and takes input with respect to options
//You can manually send request to other Network Functions with this way
//Normally in the 5G Core Network  all these request send automatically when certain events occur
public class AMFClient extends Thread{

    private NFProfile nfProfile; //AMF's network function profile
    private NRFServiceRequest nrfServiceRequest;
    private UDMServiceRequest udmServiceRequest;
    private SMFServiceRequest smfServiceRequest;
    private String deregCallbackUri;//the Uri to be used by other Network Functions to notify when they deregister this AMF

    AMFClient(int portNumber, NFProfile nfProfile, NFProfile NRFProfile) {

        deregCallbackUri = "https://localhost:"+ portNumber +"/udm-dereg/v1/";
        this.nfProfile = nfProfile;
        OkHttpClient httpClient = getUnsafeOkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        nrfServiceRequest = new NRFServiceRequest(httpClient, mediaType,NRFProfile);
        udmServiceRequest = new UDMServiceRequest(httpClient, mediaType);
        smfServiceRequest = new SMFServiceRequest(httpClient, mediaType);
    }

    @Override
    public void run() {
        try {
            sleep(1000);
            System.out.println();
            System.out.println("AMF CLIENT APP");
            System.out.println("AMF id: " + nfProfile.getNfInstanceID());
            System.out.println("AMF address: " + nfProfile.getIpv4Address());
            System.out.println();
            createClient();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createClient() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input ;
        //main loop for manual tests
        while(true) {
            System.out.println();
            System.out.println("-------REQUEST TO NRF SERVICES-------");
            System.out.println("1.Send registration request to NRF");
            System.out.println("2.Send deregistration request to NRF");
            System.out.println("3.Send service Discovery Request to NRF");
            System.out.println("4.Request NF profile with nfInstanceID from NRF");
            System.out.println("5.Create subscription info at NRF");
            System.out.println("6.Delete Subscription info at NRF");
            System.out.println();
            System.out.println("-------REQUEST TO UDM SERVICES-------");
            System.out.println("7.AMF Registration for 3GPP access");
            System.out.println("8.AMF 3GPP Registration Info Retrieval");
            System.out.println();
            System.out.println("-------REQUEST TO SMF SERVICES-------");
            System.out.println("9.PDU Session Creation");
            System.out.println("Please enter the option that you want to do, write exit to close app.");
            input = br.readLine();
            switch (input) {
                case "1":
                    registerNF();
                    break;
                case "2":
                    deregisterNF();
                    break;
                case "3":
                    discoverNFs(br);
                    break;
                case "4":
                    requestNFProfile(br);
                    break;
                case "5":
                    createSubscription();
                    break;
                case "6":
                    deleteSubscription(br);
                    break;
                case "7":
                    Amf3GPPRegistration(deregCallbackUri, br);
                    break;
                case "8":
                    requestAmf3GPPRegistrationInfo(br);
                    break;
                case "9":
                    createPDUSession(deregCallbackUri, br);
                    break;
                case "exit":
                    deregisterNF();
                    exit(0);
                default:
                    System.out.println("Please select a valid option");
                    break;
            }


        }
    }
    //send request to SMF
    private void createPDUSession(String deregCallbackUri, BufferedReader br) throws IOException {
        NFProfile nfProfile = nrfServiceRequest.getNfProfile("smf");
        //checks if  a registered SMF exists in the network
        if (nfProfile != null) {
            long ueId;
            long pduSessionId;
            String smfAddress = nfProfile.getIpv4Address();
            System.out.println("Please enter the SUPI(IMSI) of the UE");
            ueId = Long.valueOf(br.readLine());
            System.out.println("Please enter the PDU Session ID)");
            pduSessionId = Long.valueOf(br.readLine());
            System.out.println("Please enter the DNN");
            String dnn = br.readLine();
            SMContextCreateData smContextCreateData = new SMContextCreateData(ueId, pduSessionId, dnn, nfProfile.getNfInstanceID(), deregCallbackUri);
            smfServiceRequest.smfContextCreation(smContextCreateData, smfAddress);
        }
    }
    //send request to UDM to get info about a registration made before
    private void requestAmf3GPPRegistrationInfo(BufferedReader br) throws IOException {
        NFProfile nfProfile = nrfServiceRequest.getNfProfile("udm");
        if (nfProfile != null) {
            long ueId;
            String udmAddress = nfProfile.getIpv4Address();
            System.out.println("Please enter the SUPI(IMSI) of the UE");
            ueId = Long.valueOf(br.readLine());
            System.out.println();
            udmServiceRequest.amf3GppAccessRegistrationInfoRetrieval(ueId, udmAddress);
        }
    }
    //send request to UDM to add new AMF registration info
    private void Amf3GPPRegistration(String deregCallbackUri, BufferedReader br) throws IOException {

        NFProfile udmNfProfile = nrfServiceRequest.getNfProfile("udm");
        if (udmNfProfile != null) {
            long ueId;
            long pei;
            String udmAddress = udmNfProfile.getIpv4Address();
            System.out.println("Please enter the SUPI(IMSI) of the UE");
            ueId = Long.valueOf(br.readLine());
            System.out.println("Please enter the UE's PEI(IMEI)");
            pei = Long.valueOf(br.readLine());
            System.out.println();
            Amf3GppAccessRegistration newAmf3GppAccessRegistration = new Amf3GppAccessRegistration(nfProfile.getNfInstanceID(), pei, deregCallbackUri + ueId);
            udmServiceRequest.amf3GppAccessRegistration(newAmf3GppAccessRegistration, ueId, udmAddress);
        }
    }
    //send request to NRF
    private void createSubscription() {
        SubscriptionData subscriptionData = new SubscriptionData(nfProfile.getIpv4Address() + "/nfStatusNotification");
        nrfServiceRequest.nrfCreateSubscription(subscriptionData);
    }
    //send request to NRF
    private void deleteSubscription(BufferedReader br) throws IOException {
        System.out.println("Please enter the subscriptionID of the Subscription Info to be deleted");
        long subscriptionID = Long.valueOf(br.readLine());
        System.out.println();
        nrfServiceRequest.nrfDeleteSubscription(subscriptionID);
    }
    //send request to NRF to get a Network Function's profile with its id
    private void requestNFProfile(BufferedReader br) throws IOException {
        System.out.println("Please enter the nfInstaceId of NF to be requested");
        long nfInstanceId = Long.valueOf(br.readLine());
        System.out.println();
        nrfServiceRequest.nrfGetNFProfile(nfInstanceId);
    }
    //send request to NRF to get all the network functions' profile with given network function type
    private void discoverNFs(BufferedReader br) throws IOException {
        String nftype;
        System.out.println("Please enter the nfType to be discovered");
        nftype = br.readLine();
        System.out.println();
        nrfServiceRequest.nrfDiscovery(null, nfProfile.getNfType(), nftype, nfProfile.getIpv4Address());
    }
    //send request to NRF to register itself so that other network functions can get NF profile
    private void registerNF() {
        System.out.println();
        nrfServiceRequest.nrfRegistration(nfProfile, nfProfile.getNfInstanceID());
    }
    //send request to NRF to deregister itself
    private void deregisterNF() {
        System.out.println();
        nrfServiceRequest.nrfDeregistration(nfProfile.getNfInstanceID());
    }

    //While testing to ignore SSL certification checks used the function below
    //More detailed info at  http://stackoverflow.com/questions/25509296/trusting-all-certificates-with-okhttp
    private OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, TRUST_ALL_CERTS, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
