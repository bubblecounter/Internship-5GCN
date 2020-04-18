package AMF.api;

import AMF.models.NFProfile;
import AMF.models.SearchResult;
import AMF.models.SubscriptionData;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

public class NRFServiceRequest {

    private String nrfAddress;          //to store the adress of NRF in the network
    private final MediaType mediaType;  //the media tpye to be used in requests by default it is JSON
    private Object mapper = new ObjectMapper(); // to map strings into given mediaType(Json,xml)
    private OkHttpClient httpClient;    //to send http2 requests
    private ArrayList<NFProfile> nfProfiles = new ArrayList<>(); //to store discovered network functions profiles

    NRFServiceRequest(OkHttpClient okHttpClient, MediaType mediaType, NFProfile NRFProfile) {
    nfProfiles.add(NRFProfile);
    this.nrfAddress = NRFProfile.getIpv4Address();
    this.httpClient = okHttpClient;
    this.mediaType = mediaType;
    nfProfiles.add(NRFProfile);
    }
    //registers Network Function to NRF
    void nrfRegistration(NFProfile nfProfile, long nfInstanceID){
    try {

      String requestUrl = nrfAddress+"/nf-instances/"+nfInstanceID;
      String json = ((ObjectMapper) mapper).writeValueAsString(nfProfile);
      RequestBody body = RequestBody.create(mediaType, json);

      System.out.println("REQUEST SENT:\nInfo: Registration Request \nTo: NRF \nAddress: "+requestUrl);
      System.out.println("Method: PUT \nBody: \n" + json);

      Request request = new Request.Builder()
              .url(requestUrl)
              .put(body)
              .build();

      com.squareup.okhttp.Response response = httpClient.newCall(request).execute();

      System.out.println("RESPONSE: \nInfo: Registration Response \nFrom:NRF");
      System.out.println("Protocol: " + response.protocol().toString());
      System.out.println("Code: " + response.code());
      System.out.println("Body: \n" +response.body().string() + "\n");
    }catch (ConnectException n){
      System.out.println("ERROR:Couldnt Connect to NRF. Make sure the NRF is online.");
    } catch (IOException e) {
      e.printStackTrace();
    }
    }
    //deregisters Network Function with given ID from NRF
    void nrfDeregistration(long nfInstanceID){
    try {
      String requestUrl = nrfAddress+"/nf-instances/"+nfInstanceID;
      System.out.println("REQUEST SENT: \nInfo: Deregistration Request \nTo: NRF \nAddress: "+requestUrl);
      System.out.println("Method: DELETE");

      Request request = new Request.Builder()
              .url(requestUrl)
              .delete()
              .build();
      com.squareup.okhttp.Response response = httpClient.newCall(request).execute();

      System.out.println("RESPONSE: \nInfo: Deregistration Response \nFrom:NRF ");
      System.out.println("Protocol: " + response.protocol().toString());
      System.out.println("Code: " + response.code());
      System.out.println("Body: \n" +response.body().string()+"\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
    }
    //receive Network Function's profile with given ID
    void nrfGetNFProfile(long nfInstanceID){
    try {
      String requestUrl = nrfAddress+"/nf-instances/"+nfInstanceID;
      System.out.println("REQUEST SENT: \nInfo: NF Profile Retrieval \nTo: NRF \nAddress: "+requestUrl);
      System.out.println("Method: GET ");


      Request request = new Request.Builder()
              .url(requestUrl)
              .get()
              .build();
      com.squareup.okhttp.Response response = httpClient.newCall(request).execute();

      System.out.println("RESPONSE: \nInfo: NF Profile Retrieval Response \nFrom:NRF ");
      System.out.println("Protocol: " + response.protocol().toString());
      System.out.println("Code: " + response.code());
      System.out.println("Body: \n" +response.body().string()+"\n");

    } catch (IOException e) {
      e.printStackTrace();
    }
    }
    //returns all Network functions with specified type
    void nrfGetNFProfileList(String nftype, int limit){
    try {
      String requestUrl = nrfAddress + "/nf-instances/?nf-type=" + nftype + "&limit=" + limit;
      System.out.println("REQUEST SENT: \nInfo: NF Profile List Retrieval of specific type \nTo: NRF \nAddress: "+requestUrl);
      System.out.println("Method: GET ");


      Request request = new Request.Builder()
              .url(requestUrl)
              .get()
              .build();
      com.squareup.okhttp.Response response = httpClient.newCall(request).execute();

      System.out.println("RESPONSE: \nInfo: NF Profile Retrieval Response \nFrom:NRF ");
      System.out.println("Protocol: " + response.protocol().toString());
      System.out.println("Code: " + response.code());
      System.out.println("Body: \n" +response.body().string()+"\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
    }

    void nrfCreateSubscription(SubscriptionData subscriptionData){
    try {
      String json;
      String requestUrl = nrfAddress + "/subscriptions";
      json = ((ObjectMapper) mapper).writeValueAsString(subscriptionData);
      RequestBody body = RequestBody.create(mediaType, json);
      System.out.println("REQUEST SENT: \nInfo: Subscription Creation \nTo: NRF \nAddress: "+requestUrl);
      System.out.println("Method: POST \nBody:\n " + json);


      Request request = new Request.Builder()
              .url(requestUrl)
              .post(body)
              .build();
      com.squareup.okhttp.Response response = httpClient.newCall(request).execute();

      System.out.println("RESPONSE: \nInfo: Subscription Creation \nFrom:NRF ");
      System.out.println("Protocol: " + response.protocol().toString());
      System.out.println("Code: " + response.code());
      System.out.println("Body: \n" +response.body().string()+"\n");
    } catch (IOException e) {
      e.printStackTrace();
    }

    }

    void nrfDeleteSubscription(long subscriptionID){
    try {
      String requestUrl = nrfAddress + "/subscriptions/"+subscriptionID;
      System.out.println("REQUEST SENT: \nInfo: Subscription Deletion \nTo: NRF \nAddress: "+requestUrl);
      System.out.println("Method: DELETE ");


      Request request = new Request.Builder()
              .url(requestUrl)
              .delete()
              .build();
      com.squareup.okhttp.Response response = httpClient.newCall(request).execute();

      System.out.println("RESPONSE: \nInfo: Subscription Deletion \nFrom:NRF ");
      System.out.println("Protocol: " + response.protocol().toString());
      System.out.println("Code: " + response.code());
      System.out.println("Body: \n" +response.body().string()+"\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
    }

    void nrfDiscovery(ArrayList<String> serviceNames, String requesterNftype, String targetNftype, String requesterNfInstanceFQDN){
    try {
            String requestUrl = nrfAddress + "/nf-instances?target-nf-type="+targetNftype+
                    "&requester-nf-type="+requesterNftype+
                    "&requester-nf-instance-fqdn="+requesterNfInstanceFQDN;

            Request request = new Request.Builder()
                    .url(requestUrl)
                    .get()
                    .build();
            com.squareup.okhttp.Response response = httpClient.newCall(request).execute();
            System.out.println("RESPONSE: \nInfo: NF Discovery \nFrom:NRF ");
            System.out.println("Protocol: " + response.protocol().toString());
            System.out.println("Code: " + response.code());
      String jsonString = response.body().string();
      System.out.println("Body: \n" +jsonString+"\n");

      try{
        SearchResult searchResult1 = ((ObjectMapper) mapper).readValue(jsonString, SearchResult.class);
        nfProfiles.addAll(searchResult1.getNfInstances());
      }catch (JsonParseException ignored){
      }

    }catch (IOException e) {
      e.printStackTrace();
    }
    }
    //checks if a Network function with given type discovered before and returns it
    //if not discovered before it ask use to send discovery request to NRF
    NFProfile getNfProfile(String nfType) {
    for (NFProfile nf : nfProfiles) {
      if(nf.getNfType().equals(nfType)){
        return nf;
      }
    }
    System.out.println("ERROR");
    System.out.println("There is no "+nfType +" found. Please send NF discovery Message to NRF with nfType.");
    return null;
    }
}
