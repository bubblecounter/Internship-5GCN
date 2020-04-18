package AMF.api;

import AMF.models.SMContextCreateData;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class SMFServiceRequest {
    private OkHttpClient httpClient;
    final MediaType mediaType;
    private Object mapper;

    public SMFServiceRequest(OkHttpClient httpClient, MediaType mediaType) {
        this.httpClient = httpClient;
        this.mediaType = mediaType;
        mapper = new ObjectMapper();
    }
    //create PDU session for UE in SMF
    public void smfContextCreation (SMContextCreateData smContextCreateData, String smfAddress){
        try{

            String requestUrl = smfAddress+"/nsmf-pdusession/v1/sm-contexts";
            String json = ((ObjectMapper) mapper).writeValueAsString(smContextCreateData);

            RequestBody body = RequestBody.create(mediaType, json);

            Request request = new Request.Builder()
                    .url(requestUrl)
                    .post(body)
                    .build();
            com.squareup.okhttp.Response response = httpClient.newCall(request).execute();
            System.out.println("RESPONSE: \nInfo: Registration Response \nFrom:SMF");
            System.out.println("Protocol: " + response.protocol().toString());
            System.out.println("Code: " + response.code());
            System.out.println("Body: \n" +response.body().string() + "\n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
