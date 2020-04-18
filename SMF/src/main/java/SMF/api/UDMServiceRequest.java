package SMF.api;

import SMF.models.SmfRegistration;
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
    //registers UE and its serving SMF to UDM
    public Response smfRegistration(SmfRegistration newSmfRegistration, long ueId, long pduSessionId, String udmAddress) {

        try {
                String requestUrl = udmAddress+"/nudm-uecm/v1/"+ueId+"/registrations/smf-registrations/"+pduSessionId;
                String json;
                json = ((ObjectMapper) mapper).writeValueAsString(newSmfRegistration);
                RequestBody body = RequestBody.create(mediaType, json);
                Request request = new Request.Builder()
                        .url(requestUrl)
                        .put(body)
                        .build();
                Response response = httpClient.newCall(request).execute();
                System.out.println("RESPONSE: \n Info: SMF Registration Response \n From:UDM");
                System.out.println("Protocol: " + response.protocol().toString());
                System.out.println("Code: " + response.code());
                System.out.println("Body: \n" + response.body().string() + "\n");
                return response;
        } catch (IOException a) {
            System.out.println("Error: Problem occurred while sending request to UDM, make sure it is running");
            return null;
        }
    }

    public  void smfDeregistration(long ueId, long pduSessionId,String udmAddress){
        try {
                String requestUrl=udmAddress+"/nudm-uecm/v1/"+ueId+"/registrations/smf-registrations/"+pduSessionId;
                Request request = new Request.Builder()
                        .url(requestUrl)
                        .delete()
                        .build();
                Response response = httpClient.newCall(request).execute();
                System.out.println("RESPONSE: \n Info: SMF Deregistration Response \n From:UDM");
                System.out.println("Protocol: " + response.protocol().toString());
                System.out.println("Code: " + response.code());
                System.out.println("Body: \n" + response.body().string() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
