package AMF.api;

import AMF.models.Amf3GppAccessRegistration;
import com.squareup.okhttp.*;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;

public class UDMServiceRequest {
    private OkHttpClient httpClient;
    final MediaType mediaType;
    private Object mapper;

    public UDMServiceRequest(OkHttpClient httpClient, MediaType mediaType) {
        this.httpClient = httpClient;
        this.mediaType = mediaType;
        mapper = new ObjectMapper();
    }
    //registers UE and its serving AMF to UDM
    public void amf3GppAccessRegistration(Amf3GppAccessRegistration newAmf3GppAccessRegistration, long ueId, String udmAddress) {
        try {
                String requestUrl=udmAddress+"/nudm-uecm/v1/"+ueId+"/registrations/amf-3gpp-access";
                String json;

                json = ((ObjectMapper) mapper).writeValueAsString(newAmf3GppAccessRegistration);
                RequestBody body = RequestBody.create(mediaType, json);

                Request request = new Request.Builder()
                        .url(requestUrl)
                        .put(body)
                        .build();
                Response response = httpClient.newCall(request).execute();
                System.out.println("RESPONSE: \nInfo: Registration Response \nFrom:UDM");
                System.out.println("Protocol: " + response.protocol().toString());
                System.out.println("Code: " + response.code());
                System.out.println("Body: \n" +response.body().string() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //gets AMF registration info of a specified UE
    public void amf3GppAccessRegistrationInfoRetrieval (long ueId, String udmAddress){
        try {
            String requestUrl = udmAddress + "/nudm-uecm/v1/" + ueId + "/registrations/amf-3gpp-access";

            Request request = new Request.Builder()
                    .url(requestUrl)
                    .get()
                    .build();
            Response response = httpClient.newCall(request).execute();
            System.out.println("RESPONSE: \nInfo: Registration Info Retrieval Response \nFrom:UDM");
            System.out.println("Protocol: " + response.protocol().toString());
            System.out.println("Code: " + response.code());
            System.out.println("Body: \n" +response.body().string() + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
