package cz.sortivo.sklikapi;

import cz.sortivo.sklikapi.exception.InvalideRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * It also manages sending network requests 
 * http://api.sklik.cz/client.getAttributes.html
 * @author Jan Dufek
 */
public class Client {
    
    public static final String SKLIK_URL = "https://api.sklik.cz/RPC2/";
    public static final String SKLIK_SANDBOX_URL = "https://api.sklik.cz/sandbox/RPC2";
    
    private String session;
    private boolean useSandbox;
    
    private static final String LOGIN_METHOD_NAME = "client.login";
    private static final String CLIENT_ATTRIBUTES_METHOD_NAME = "client.getAttributes";
    
    private Account account;
    
    private XmlRpcClient rpcClient;
    
    public Client() throws InvalideRequestException{
       this(false);
    }

    public Client(boolean useSandbox) throws InvalideRequestException{
        try {
            this.useSandbox = useSandbox;
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            String url = getUrl();

            config.setServerURL(new URL(url));
            config.setEnabledForExtensions(true);
            rpcClient = new XmlRpcClient();
            rpcClient.setConfig(config);
            rpcClient.setTypeFactory(new XmlRpcTypeNil(rpcClient));
        } catch (MalformedURLException ex) {
            throw new InvalideRequestException("Malformed URL " + getUrl(), ex);
        }
    }
    
    public final String getUrl(){
        if (useSandbox) return SKLIK_SANDBOX_URL;
        return SKLIK_URL;
    }
    
    public Map<String, Object> login(String username, String password) throws InvalideRequestException, SKlikException{
        Map<String, Object> resp = send(LOGIN_METHOD_NAME, new String[]{username, password});
        session = (String) resp.get("session");
        return resp;
    }
    
    public Map<String, Object> getAttributes()throws InvalideRequestException, SKlikException{
        return sendRequest(CLIENT_ATTRIBUTES_METHOD_NAME, new Object[]{});
    }
    
    public Map<String, Object> sendRequest(String method, Object[] params) throws InvalideRequestException, SKlikException{
        if (session == null){
            throw new InvalideRequestException("It is necessary to call login method first! (no session available)");            
        }
        if (params.length > 0){
            Object[] paramsFrom = params;
            params = new Object[paramsFrom.length + 1];
            System.arraycopy(paramsFrom, 0, params, 1, paramsFrom.length);
            params[0] = session;
        }else{
            params = new Object[]{session};
        }  
        return send(method, params);
    }
    
    protected Map<String, Object> send(String method, Object[] params) throws InvalideRequestException, SKlikException{
        try{
            Map<String, Object> response =(HashMap<String, Object>)rpcClient.execute(method, params);
            int status = (Integer) response.get("status");
            if (status >= 400){
                throw new SKlikException("Request error with status code " + status + " and status message " + response.get("statusMessage"), status, response);
            }
            return response;
            
        } catch (XmlRpcException ex){
            throw new InvalideRequestException("XML-RPC Exception for " + Arrays.toString(params), ex);
        }
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    
    
    
    
}
