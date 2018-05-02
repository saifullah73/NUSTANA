package backendless;

import org.json.*;
import java.net.*;
import java.io.*;
/**
 *
 * @author Abdul Rahman
 */
public class BackendlessClient {
    public static final String SERVER = "https://api.backendless.com/";
    public static final String VERSION = "v4.0";
    
    private final String applicationId;
    private final String secretKey;
    public BackendlessClient(String applicationId,String secretKey) {
        this.applicationId = applicationId;
        this.secretKey = secretKey;
    }
    public synchronized HttpURLConnection CreateConnection(String method,String parameters,byte[] content,String contentType) throws IOException{
        URL url = new URL(SERVER+applicationId+"/"+secretKey+"/"+parameters);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", contentType);
        if(content != null){
            connection.setDoOutput(true);
            DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
            stream.write(content);
            stream.flush();
            stream.close();
        }
        return connection;
    }
    public synchronized HttpURLConnection CreateConnection(String method,String parameters,byte[] data) throws IOException{
        return CreateConnection(method, parameters,data,"application/json");
    }
    public synchronized HttpURLConnection CreateConnection(String method,String parameters) throws IOException{
        return CreateConnection(method, parameters,null);
    }
    public synchronized String CreateRequest(String method,String parameters,byte[] content,String contentType) throws IOException{
        HttpURLConnection connection = CreateConnection(method, parameters, content, contentType);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        int status = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer output = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            output.append(inputLine);
        }
        in.close();
        connection.disconnect();
        return output.toString();
    }
    public synchronized String CreateRequest(String method,String parameters,byte[] content) throws IOException{
        return CreateRequest(method,parameters,content,"application/json");
    }
    public synchronized String CreateRequest(String method,String parameters) throws IOException{
        return CreateRequest(method,parameters,null);
    }
    public synchronized JSONObject CreateObject(String table,JSONObject object)throws IOException {
        return new JSONObject(CreateRequest("POST", "data/"+table, object.toString().getBytes()));
    }
    public synchronized JSONObject UpdateObject(String table,JSONObject object,String objectId) throws IOException {
        return new JSONObject(CreateRequest("PUT", "data/"+table+"/"+objectId, object.toString().getBytes()));
    }
    public synchronized JSONObject GetObject(String table,String objectId) throws IOException{
        return new JSONObject(CreateRequest("GET","data/"+table+"/"+objectId));
    }
    public synchronized JSONObject DeleteObject(String table,String objectId) throws IOException{
        return new JSONObject(CreateRequest("DELETE","data/"+table+"/"+objectId));
    }
    public synchronized JSONArray GetObjects(String table) throws IOException{
        return new JSONArray(CreateRequest("GET","data/"+table));
    }
    public synchronized JSONArray GetObjects(String table,int offset) throws IOException{
        return new JSONArray(CreateRequest("GET","data/"+table+"?offset"+offset));
    }
    public synchronized JSONArray GetObjects(String table,int offset,int limit)throws IOException{
        return new JSONArray(CreateRequest("GET","data/"+table+"?offset"+offset+",pageSize="+limit));
    }
}
