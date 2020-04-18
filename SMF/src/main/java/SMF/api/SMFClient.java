package SMF.api;

import SMF.models.NFProfile;
import SMF.models.SMContextCreateData;
import SMF.models.SmfRegistration;
import SMF.models.SubscriptionData;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.exit;
import static org.eclipse.jetty.util.ssl.SslContextFactory.TRUST_ALL_CERTS;

//SMFClient sends requests to network functions in the network (NRF,UDM)
//Also to try request and responses it prints a menu on terminal and takes input with respect to options
//You can manually send request to other Network Functions with this way
//Normally in the 5G Core Network  all these request send automatically when certain events occur
public class SMFClient extends Thread{

    private NFProfile nfProfile; //SMF's network function profile
    private NRFServiceRequest nrfServiceRequest;
    private UDMServiceRequest udmServiceRequest;

    SMFClient(NFProfile nfProfile, NFProfile NRFProfile) {

        this.nfProfile = nfProfile;
        OkHttpClient httpClient = getUnsafeOkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        nrfServiceRequest = new NRFServiceRequest(httpClient, mediaType,NRFProfile);
        udmServiceRequest = new UDMServiceRequest(httpClient, mediaType);
    }

    @Override
    public void run() {
        try {
            sleep(1000);
            System.out.println();
            System.out.println("SMF CLIENT APP");
            System.out.println("SMF id: " + nfProfile.getNfInstanceID());
            System.out.println("SMF address: " + nfProfile.getIpv4Address());
            System.out.println();
            createClient();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createClient() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input;
        //main loop for manual tests
        while (true) {
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
            System.out.println("7.SMF Registration");
            System.out.println("8.SMF Deregistration");
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
                    SmfRegistration(br);
                    break;
                case "8":
                    SmfDeregistration(br);
                case "exit":
                    deregisterNF();
                    exit(0);
                    break;
                default:
                    System.out.println("Please select a valid option");
                    break;
            }
        }

    }

    private void registerNF() {
        System.out.println();
        nrfServiceRequest.nrfRegistration(nfProfile, nfProfile.getNfInstanceID());
    }
    //send request to NRF to deregister itself
    private void deregisterNF() {
        System.out.println();
        nrfServiceRequest.nrfDeregistration(nfProfile.getNfInstanceID());
    }
    //send request to NRF to get all the network functions' profile with given network function type
    private void discoverNFs(BufferedReader br) throws IOException {
        String nftype;
        System.out.println("Please enter the nfType to be discovered");
        nftype = br.readLine();
        System.out.println();
        nrfServiceRequest.nrfDiscovery(null, nfProfile.getNfType(), nftype, nfProfile.getIpv4Address());
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
    //registers this SMF as the serving SMF of a specified UE
    private void SmfRegistration(BufferedReader br) throws IOException{
        NFProfile udmNfProfile = nrfServiceRequest.getNfProfile("udm");
        if (udmNfProfile != null) {
            long ueId;
            long pduSessionId;
            String dnn;
            String udmAddress = nfProfile.getIpv4Address();
            System.out.println("Please enter the SUPI(IMSI) of the UE");
            ueId = Long.valueOf(br.readLine());
            System.out.println("Please enter the pdu Session Id");
            pduSessionId = Long.valueOf(br.readLine());
            System.out.println("please enter the DNN");
            dnn = br.readLine();
            SmfRegistration newSmfRegistration = new SmfRegistration(nfProfile.getNfInstanceID(), pduSessionId, dnn);
            udmServiceRequest.smfRegistration(newSmfRegistration, ueId, pduSessionId, udmAddress);
        }
    }
    //deregisters this SMF since it doesn't serve to specified UE not anymore
    private void SmfDeregistration(BufferedReader br)throws IOException {
        NFProfile udmNfProfile = nrfServiceRequest.getNfProfile("udm");
        if (udmNfProfile != null) {
            long ueId;
            long pduSessionId;
            String udmAddress = nfProfile.getIpv4Address();
            System.out.println("Please enter the SUPI(IMSI) of the UE");
            ueId = Long.valueOf(br.readLine());
            System.out.println("Please enter the pdu Session Id");
            pduSessionId = Long.valueOf(br.readLine());
            udmServiceRequest.smfDeregistration(ueId, pduSessionId, udmAddress);

        }
    }
    //creates pdu Session upon request from another Network Function
    public Response pduSessionCreation(SMContextCreateData smContextCreateData){
        System.out.println("REQUEST: \nInfo: Create SMF Registration Info");
        System.out.println("From NF with Id: " + smContextCreateData.getServingNfId());
        System.out.println("Access Type: " + smContextCreateData.getAnType());
        NFProfile udmNfProfile = nrfServiceRequest.getNfProfile("udm");
        if (udmNfProfile != null) {
            String udmAddress = nfProfile.getIpv4Address();
            SmfRegistration newSmfRegistration = new SmfRegistration(nfProfile.getNfInstanceID(), smContextCreateData.getPduSessionId(), smContextCreateData.getDnn());
            return udmServiceRequest.smfRegistration(newSmfRegistration, smContextCreateData.getSupi(), smContextCreateData.getPduSessionId(), udmAddress);
        }
        return null;
    }
    //While testing to ignore all SSL errors used the function below
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