package UDM.api;

import UDM.models.NFProfile;
import UDM.models.SubscriptionData;
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


//UDMClient sends requests to network functions in the network (SMF,NRF,UDM)
//Also to try request and responses it prints a menu on terminal and takes input with respect to options
//You can manually send request to other Network Functions with this way
//Normally in the 5G Core Network  all these request send automatically when certain events occur
public class UDMClient extends Thread{
    private NFProfile nfProfile; //UDM's network function profile
    private NRFServiceRequest nrfServiceRequest;

   UDMClient(int portNumber, NFProfile nfProfile, NFProfile NRFProfile) {

        this.nfProfile = nfProfile;
        //@Todo explain why used unsafe okhttp client
        OkHttpClient httpClient = getUnsafeOkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        nrfServiceRequest = new NRFServiceRequest(httpClient, mediaType,NRFProfile);
    }

    @Override
    public void run() {
        try {
            sleep(1000);
            System.out.println();
            System.out.println("UDM APP");
            System.out.println("UDM id: " + nfProfile.getNfInstanceID());
            System.out.println("UDM address: " + nfProfile.getIpv4Address());
            System.out.println();

            createClient();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private  void createClient() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input ;
        while(true) {
            System.out.println();
            System.out.println("-------REQUEST TO NRF SERVICES-------");
            System.out.println("1.Send registration request to NRF");
            System.out.println("2.Send deregistration request to NRF");
            System.out.println("3.Send service Discovery Request to NRF");
            System.out.println("4.Request NF profile with nfInstanceID from NRF");
            System.out.println("5.Create subscription info at NRF");
            System.out.println("6.Delete Subscription info at NRF");
            System.out.println("7.Shutdown UDM");
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
                    deregisterNF();
                    exit(0);
                case "exit":
                    deregisterNF();
                    exit(0);
                default:
                    System.out.println("Please select a valid option");
                    break;
            }
        }

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

    //While testing to ignore SSL ceritfication checks used this function below
    //More detailed info at  http://stackoverflow.com/questions/25509296/trusting-all-certificates-with-okhttp
    public static OkHttpClient getUnsafeOkHttpClient() {
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
