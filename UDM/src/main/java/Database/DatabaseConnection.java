package Database;

import UDM.api.UDMClient;
import UDM.models.*;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.*;

public class DatabaseConnection {

    private Statement stmt;
    //create a db connection to execute queries
    public DatabaseConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver") ;
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/udm?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey","root","1") ;
            this.stmt = conn.createStatement() ;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //returns the registered AMF's info with given UE id
    public Response retrieveAmfRegistrationInfo(long ueId){
        System.out.println("REQUEST: \nInfo: Retrieve AMF Registration Info");
        System.out.println("RESPONSE SENT: \nInfo: ");
        try {
            if(ueInfoExist(ueId)){
                return getServiceInfo(ueId);
            }
            else{
                System.out.println("User with ueId: "+ ueId +" does not exist");
                ProblemDetails pd = new ProblemDetails();
                pd.setTitle("User with ueId: "+ ueId +" does not exist");
                pd.setStatus(404);
                pd.addParam("IMSI","There is no UE with imsi: " + ueId +" in UDR");

                return Response.status(404)
                                .entity(pd)
                                .build();
            }
        } catch (SQLException e) {
            return getResponseSQLException(e);
        }
    }
    //add new AMF registration info
    public Response insertAmfRegistrationInfo(Amf3GppAccessRegistration amfInfo, long ueId){
        System.out.println("REQUEST: \nInfo: Insert AMF Registration Info");
        System.out.println("RESPONSE SENT: \nInfo: ");
        try {
            boolean amfInfoExist = amfInfoExist(amfInfo.getAmfId());
            boolean ueInfoExist  = ueInfoExist(ueId);
            if(amfInfoExist && ueInfoExist){
                    Amf3GppAccessRegistration tmpAmfInfo = getAmf3GppAccessRegistrationInfo(ueId);
                    if(tmpAmfInfo!=null){
                        if(tmpAmfInfo.getAmfId() != amfInfo.getAmfId())
                        {
                            DeregistrationData x = new DeregistrationData("UE_REGISTRATION_AREA_CHANGE");
                            String deregCallbackUri = tmpAmfInfo.getDeregCallbackUri();
                            deregistrationNotification(deregCallbackUri,x);
                        }
                            return updateServiceInfo(amfInfo,ueId);
                    }
                    else{
                        return createServiceInfo(amfInfo,ueId);
                    }
            }
            else{
                System.out.println("No entry found with given ID in UDR");
                ProblemDetails pd = new ProblemDetails();
                pd.setTitle("ID not found");
                pd.setStatus(404);
                pd.setCause("No entry found with given ID in UDR");
                if(!ueInfoExist){
                    pd.addParam("IMSI","There is no UE with imsi: " + ueId +" in UDR");
                }
                if (!amfInfoExist) {
                    pd.addParam("amfId","There is no AMF with amfId: "+ amfInfo.getAmfId() +" in UDR");
                }
                return Response.status(404)
                        .entity(pd)
                        .build();
                }

        } catch (SQLException e) {
            return getResponseSQLException(e);
        }
    }
    //updates the AMf registration info for given UE id
    private Response updateServiceInfo(Amf3GppAccessRegistration amfInfo,long ueId) throws SQLException {
        System.out.println("REQUEST: \nInfo: Update AMF Registration Info");
        System.out.println("RESPONSE SENT: \nInfo: ");
        String query =  " UPDATE    givesservice "+
                        " SET       amfId = " + amfInfo.getAmfId()+","+
                        "           pei   = " + amfInfo.getPei()  +","+
                        "           deregCallbackUri= '" + amfInfo.getDeregCallbackUri()+
                        "'WHERE     imsi  = " + ueId;
        System.out.println("Amf Registration Info Updated");
        return getResponse(amfInfo, query);
    }

    private Response createServiceInfo(Amf3GppAccessRegistration amfInfo, long ueId) {
        String query =  "INSERT INTO givesservice (amfId,imsi,pei,deregCallbackUri)" +
                        "VALUES ("+amfInfo.getAmfId()+ ","+
                                    ueId +","+
                                    amfInfo.getPei()+",'"+
                                    amfInfo.getDeregCallbackUri()+"')";

        try {
            stmt.execute(query);
            System.out.println("AMF Registration Info Created");
            return Response.status(201)
                    .entity(amfInfo)
                    .build();
        } catch (SQLException e) {
            return getResponseSQLException(e);
        }

    }

    private Response getServiceInfo(long ueId) throws SQLException {
        Amf3GppAccessRegistration amfInfo = getAmf3GppAccessRegistrationInfo(ueId);
        if(amfInfo==null){
            System.out.println("There is no service registration info for User Equipment with Imsi: "+ueId);
            ProblemDetails pd = new ProblemDetails();
            pd.setTitle("There is no service registration info for User Equipment with Imsi: "+ueId);
            pd.setStatus(404);
            return Response.status(404)
                    .entity(pd)
                    .build();
        }
        else
        {
            System.out.println("AMF Info Successfully Returned");
            return Response.accepted()
                    .entity(amfInfo)
                    .build();
        }
    }
    //checks if AMF exists with given id
    private boolean amfInfoExist(long amfId) throws SQLException {
        String selectquery = "SELECT * FROM amf WHERE amfId = " + amfId;
        ResultSet rs = stmt.executeQuery(selectquery);
        if(rs.next()){
            return true;
        }
        else{
            return false;
        }
    }
    //checks if UE exists with given imsi
    private boolean ueInfoExist(long imsi) throws SQLException {
        String selectquery = "SELECT * FROM ue WHERE imsi = "+imsi;
        ResultSet rs = stmt.executeQuery(selectquery);
        if(rs.next()){
            return true;
        }
        else{
            return false;
        }
    }

    private Amf3GppAccessRegistration getAmf3GppAccessRegistrationInfo(long ueId) throws SQLException {
        String selectquery = "SELECT * FROM givesservice WHERE imsi = " + ueId;
        ResultSet rs = stmt.executeQuery(selectquery);
        if(rs.next()){
            return new Amf3GppAccessRegistration(rs.getLong("amfId"),rs.getLong("pei"),rs.getString("deregCallbackUri"));
        }
        else{
            return null;
        }
    }

    public Response insertSmfRegistrationInfo(SmfRegistration smfRegistrationInfo,long ueId, long pduSessionId){
        try {
            System.out.println("REQUEST: \nInfo: Insert SMF Registration Info");
            System.out.println("RESPONSE SENT: \nInfo: ");
            boolean ueInfoExist  = ueInfoExist(ueId);
            if(ueInfoExist){
                SmfRegistration tmpSmfInfo = getSmfRegistrationInfo(ueId, smfRegistrationInfo.getPduSessionId());
                if(tmpSmfInfo!=null){
                    return updateSmfServiceInfo(smfRegistrationInfo,ueId,pduSessionId);
                }
                else{
                    return createSmfServiceInfo(smfRegistrationInfo,ueId,pduSessionId);
                }
            }
            else{
                System.out.println("User (SUPI) does not exist");
                ProblemDetails pd = new ProblemDetails();
                pd.setTitle("ID not found");
                pd.setStatus(404);
                pd.setCause("No entry found with given ID in UDR");
                pd.addParam("imsi","User (SUPI) does not exist");
                return Response.status(404)
                        .entity(pd)
                        .build();
            }

        } catch (SQLException e) {
            return getResponseSQLException(e);
        }
    }

    private Response updateSmfServiceInfo(SmfRegistration smfRegistrationInfo, long ueId, long pduSessionId) throws SQLException {
        String query =  " UPDATE    smfRegistration "+
                        " SET       smfId          = " + smfRegistrationInfo.getSmfId()+","+
                         "           pduSessionId   = " + pduSessionId  +","+
                        "           dnn           = '" + smfRegistrationInfo.getDnn()+
                        "'WHERE     imsi  = " + ueId + " AND " + " pduSessionId = " + pduSessionId;
        System.out.println("Serving SMF UPDATED for PDU Session with id: " + pduSessionId);
        return getResponse(smfRegistrationInfo, query);
    }

    private Response createSmfServiceInfo(SmfRegistration smfRegistrationInfo, long ueId, long pduSessionId) throws SQLException {
        String query =  "INSERT INTO smfRegistration (smfId,imsi,pduSessionId,dnn)" +
                        "VALUES ("+ smfRegistrationInfo.getSmfId()+ ","+
                        ueId +","+
                        pduSessionId+",'"+
                        smfRegistrationInfo.getDnn()+"')";
        stmt.execute(query);
        System.out.println("SMF Registration Info Created");
        return Response.status(201)
                .entity(smfRegistrationInfo)
                .build();
    }

    private SmfRegistration getSmfRegistrationInfo(long ueId , long pduSessionId) throws SQLException {

        String selectquery = "SELECT * FROM smfRegistration WHERE imsi = " + ueId + " AND " + " pduSessionId = " + pduSessionId ;
        ResultSet rs = stmt.executeQuery(selectquery);
        if(rs.next()){
            return new SmfRegistration(rs.getLong("smfId"),rs.getLong("pduSessionId"),rs.getString("dnn"));
        }
        else{
            return null;
        }
    }

    public Response deleteSmfRegistrationInfo(long ueId, long pduSessionId)  {
        String query = "DELETE FROM smfRegistration WHERE imsi = " + ueId + " AND " + " pduSessionId = " + pduSessionId ;
        System.out.println("REQUEST: \nInfo: DELETE SMF Registration Info");
        System.out.println("RESPONSE SENT: \nInfo: ");
        try {
            stmt.execute(query);
        } catch (SQLException e) {
            return getResponseSQLException(e);
        }
        System.out.println("SMF DEREGISTERED for PDU Session with Id : "+pduSessionId);
        return Response.status(204)
                .build();
    }

    private Response getResponse(Object amfInfo, String query) throws SQLException {

        if(stmt.executeUpdate(query)==1) {
            System.out.println("Successfull");
            return Response.accepted()
                    .entity(amfInfo)
                    .build();
        }
        else{
            ProblemDetails pd = new ProblemDetails();
            pd.setTitle("Problem occurred while updating serving AMF");
            return Response.status(404)
                    .entity(pd)
                    .build();
        }
    }

    private Response getResponseSQLException(SQLException e) {
        System.out.println("SQL Exception");

        e.printStackTrace();
        ProblemDetails pd = new ProblemDetails();
        pd.setTitle("SQL Exception");
        pd.setStatus(404);
        return Response.status(404)
                .entity(pd.toString())
                .build();
    }

    public  void deregistrationNotification(String deregCallbackUri, DeregistrationData deregistrationData){
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Object mapper = new ObjectMapper();
        String json;
        try {
            OkHttpClient client = UDMClient.getUnsafeOkHttpClient();

            json = ((ObjectMapper) mapper).writeValueAsString(deregistrationData);
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(deregCallbackUri)
                    .post(body)
                    .build();
            com.squareup.okhttp.Response response = client.newCall(request).execute();
            System.out.println(response.code());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Statement getStmt() {
        return stmt;
    }

}
