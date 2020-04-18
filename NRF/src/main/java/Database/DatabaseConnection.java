package Database;
import NRF.model.*;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {

    private Statement stmt = null;

    public DatabaseConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver") ;
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/nrf?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey","root","1") ;
            this.stmt = conn.createStatement() ;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Statement getStmt() {
        return stmt;
    }

    private boolean  nfInstanceExist(long nfInstanceID) {
        String selectquery = "SELECT * FROM nfinstances WHERE nfInstanceID = " + nfInstanceID;
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(selectquery);
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public  Response retrieveNFInstanceList(String nfType, int limit){
        try {

            String selectquery = "SELECT ipv4Address FROM nfinstances WHERE nfType = '" + nfType + "'";
            ArrayList<String> uriList = new ArrayList<>();
            ResultSet rs = stmt.executeQuery(selectquery);
            System.out.println("REQUEST: \nInfo: "+nfType+" list requested ");
            System.out.println("RESPONSE SENT:\nInfo");
            while(rs.next()){
                uriList.add(rs.getString("ipv4Address"));
            }
            if (uriList.isEmpty()){
                return getResponseNotFound(nfType);
            }
            else{
                System.out.println("NFInstanceList returned Successfully");
                System.out.println(uriList.toString());
                return Response.status(200)
                        .entity(uriList.toString())
                        .build();
            }
        } catch (SQLException e) {
            return getResponseSQLException(e);
        }
    }

    public  Response retrieveNFInstance(long nfInstanceID){
        try {
            String selectquery = "SELECT * FROM nfinstances WHERE nfInstanceID = " + nfInstanceID;
            ResultSet rs = stmt.executeQuery(selectquery);
            System.out.println("REQUEST: \nInfo: NFInstance  with id "+nfInstanceID+"  requested");
            System.out.println("RESPONSE SENT: \nInfo: ");
            if (rs.next()){
                System.out.println("NF Profile returned Successfully");

                NFProfile nfProfile = new NFProfile(rs.getLong("nfInstanceID"),
                                                    rs.getString("nfType"),
                                                    rs.getString("ipv4Address"));
                return Response.status(200)
                                .entity(nfProfile.toString())
                                .build();
            }
            else{
                System.out.println("No nf instance with id "+ nfInstanceID + " found registered in NRF");

                ProblemDetails pd = new ProblemDetails();
                pd.setTitle("No nf instance with id "+ nfInstanceID + " found registered in NRF");
                pd.setStatus(404);
                return Response.status(404)
                        .entity(pd.toString())
                        .build();
            }
        } catch (SQLException e) {
            return getResponseSQLException(e);
        }
    }

    public  Response registerNFInstance(long nfInstanceID, NFProfile nfProfileInfo){
        if(nfInstanceExist(nfInstanceID)){
            return updateNFInstanceInfo(nfInstanceID,nfProfileInfo);
        }
        else{
            return createNFInstanceInfo(nfInstanceID, nfProfileInfo);
        }
    }

    private Response updateNFInstanceInfo(long nfInstanceID, NFProfile nfProfileInfo) {
        String query =  "UPDATE nfinstances "+
                " SET       nfType           = '" + nfProfileInfo.getNfType()+"',"+
                "          ipv4Address       = '" + nfProfileInfo.getIpv4Address()+
                "'WHERE     nfInstanceID  = " + nfInstanceID;
        System.out.println("REQUEST:\nInfo: Update NFInstanceInfo with nfIntanceId: "+ nfInstanceID);
        System.out.println("RESPONSE SENT:\nInfo:");
        try {
            stmt.execute(query);
            System.out.println("Successfully Updated");
            return Response.status(200)
                    .entity(nfProfileInfo.toString())
                    .build();
        } catch (SQLException e) {
            return getResponseSQLException(e);
        }
    }

    private Response createNFInstanceInfo(long nfInstanceID, NFProfile nfProfileInfo) {
        String query =  "INSERT INTO nfinstances (nfInstanceID,nfType,ipv4Address)" +
                "VALUES ("+ nfInstanceID+",'"+
                            nfProfileInfo.getNfType()+"','"+
                            nfProfileInfo.getIpv4Address()+"')";
        System.out.println("REQUEST: \nInfo: Registration of NF Instance");
        System.out.println("RESPONSE SENT:\nInfo:");
        try {
            stmt.execute(query);
            System.out.println("Successfully registered(NF Instance)");
            NFRegistrationData nfRegistrationData = new NFRegistrationData(60,nfProfileInfo);
            return Response.status(201)
                    .entity(nfRegistrationData.toString())
                    .build();

        } catch (SQLException e) {
            return getResponseSQLException(e);
        }
    }

    public  Response deregisterNFInstance(long nfInstanceID){
        String query = "DELETE FROM nfinstances WHERE nfInstanceID = " + nfInstanceID ;
        System.out.println("REQUEST: \nInfo: Deregistration of nfInstance");
        try {
            stmt.execute(query);
            System.out.println("RESPONSE SENT:\nInfo:");
            System.out.println("Successfully Deregistered");
            return Response.status(204)
                    .build();
        } catch (SQLException e) {
            return getResponseSQLException(e);
        }
    }

    public  Response createSubscription(SubscriptionData subscriptionData){
        String query =  "INSERT INTO subscription (nfStatusNotificationUri)" +
                "VALUES ('"+ subscriptionData.getNfStatusNotificationUri()+ "')";
        System.out.println("REQUEST: \nSubscription Info Creation");
        System.out.println("RESPONSE SENT: \nInfo: ");
        try {
            stmt.execute(query);
            System.out.println("Succcessfully Created(Subscription Info)");
            return Response.status(201)
                    .entity(subscriptionData.toString())
                    .build();
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION");
            e.printStackTrace();
            ProblemDetails pd = new ProblemDetails();
            pd.setTitle("Bad Request, SQL Exception");
            pd.setStatus(400);
            return Response.status(404)
                    .entity(pd.toString())
                    .build();
        }
    }

    public  Response deleteSubscription(long subscriptionId){
        try {
            System.out.println("REQUEST:\nInfo: Deletion of Subscription Info");
            System.out.println("RESPONSE SENT:\nInfo:");
            String selectquery = "SELECT * FROM subscription WHERE subscriptionId = " + subscriptionId;
            ResultSet rs = null;
            rs = stmt.executeQuery(selectquery);
            if(rs.next()){
                String query = "DELETE FROM subscription WHERE subscriptionId = " + subscriptionId ;

                stmt.execute(query);
                System.out.println("Successfully Deleted(Subscription Info)");
                return Response.status(204)
                        .build();
            }
        else{
                System.out.println("No subscription found with given id");
                ProblemDetails pd = new ProblemDetails();
                pd.setTitle("No subscription found with given id");
                pd.setStatus(404);
                return Response.status(404)
                        .entity(pd.toString())
                        .build();
            }
        } catch (SQLException e) {
            return getResponseSQLException(e);
        }
    }

    public  Response discoverNF(String targetNftype, String requesterNftype, String requesterNfInstanceFQDN){
        String selectquery = "SELECT * FROM nfinstances WHERE nfType = '" + targetNftype+"'";
        ArrayList<NFProfile> nfInstances = new ArrayList<>();
        System.out.println("REQUEST: \nInfo: Discovery of nf Instance");
        System.out.println("RESPONSE SENT: \nInfo: ");
        try {
            ResultSet rs = stmt.executeQuery(selectquery);
            while(rs.next()){
                nfInstances.add(new NFProfile(  rs.getLong("nfInstanceID"),
                                                rs.getString("nfType"),
                                                rs.getString("ipv4Address")));
            }
            if (nfInstances.isEmpty()){
                return getResponseNotFound(targetNftype);
            }
            else{
                System.out.println("Successfully Returned the discovered NF Instances");
                SearchResult searchResult = new SearchResult(nfInstances,600);
                return Response.status(200)
                        .entity(searchResult)
                        .build();
            }
        } catch (SQLException e) {
            return getResponseSQLException(e);
        }
    }

    private Response getResponseNotFound(String targetNftype) {
        System.out.println("There is no " + targetNftype + " found registered in NRF");

        ProblemDetails pd = new ProblemDetails();
        pd.setTitle("There is no " + targetNftype + " found registered in NRF");
        pd.setStatus(404);
        return Response.status(404)
                .entity(pd.toString())
                .build();
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

}
