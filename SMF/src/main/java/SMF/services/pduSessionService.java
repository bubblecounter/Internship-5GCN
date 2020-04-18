package SMF.services;

import SMF.api.main;
import SMF.models.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class pduSessionService {

    public static Response createSMContext(SMContextCreateData smContextCreateData){
        com.squareup.okhttp.Response response = main.Smf1.smfClient.pduSessionCreation(smContextCreateData);
        if (response != null &&(response.code() == 201 || response.code() == 200)) {
            SMContextCreatedData smContextCreatedData = new SMContextCreatedData(smContextCreateData.getPduSessionId());
            System.out.println("SM Context Created");
            System.out.println("Body:");
            System.out.println(smContextCreatedData);
            return Response.status(201)
                    .entity(smContextCreatedData)
                    .build();
        } else {
            System.out.println("Unsuccessful creation of an SM Context - Couldn't create SMF Registration Info at UDM");
            ProblemDetails pd = new ProblemDetails();
            pd.setTitle("Unsuccessfull Creation of an SM Context at UDM");
            pd.setStatus(400);
            try {
                pd.setCause(response.body().string());
            } catch (IOException e) {
                pd.setCause("Unsuccesfull");
            }
            System.out.println(pd);
            return Response.status(500)
                    .entity(pd)
                    .build();
        }
    }
}
