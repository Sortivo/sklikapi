package cz.sortivo.sklikapi;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

/**
 * Sklik API client, provides sending and receiving data through API. It also
 * can be used for obtaining additional information about logged user or
 * maintaining the current session if exists.
 * 
 * @author Michal Ličko licko61@gmail.com (C) 2014
 */
public class Client {

   // Logger logger = LoggerFactory.getLogger(Client.class);

    public static final String SKLIK_URL = "https://api.sklik.cz/cipisek/RPC2/";
    public static final String SKLIK_SANDBOX_URL = "https://api.sklik.cz/sandbox/cipisek/RPC2/";

    private String session;
    private boolean useSandbox;

    private static final String LOGIN_METHOD_NAME = "client.login";
    private static final String API_LIMITS_METHOD_NAME = "api.limits";
    private static final String CLIENT_ATTRIBUTES_METHOD_NAME = "client.getAttributes";

    private static final String FIELD_USER_ID = "userId";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_ACCESS = "access";
    private static final String FIELD_RELATION_NAME = "relationName";
    private static final String FIELD_RELATION_STATUS = "relationStatus";

    private XmlRpcClient rpcClient;

    public Client() throws InvalidRequestException {
        this(false);
    }

    public Client(boolean useSandbox) throws InvalidRequestException {
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
            throw new InvalidRequestException("Malformed URL " + getUrl(), ex);
        }
    }

    public final String getUrl() {
        if (useSandbox)
            return SKLIK_SANDBOX_URL;
        return SKLIK_URL;
    }
    
    
    public Map<String, Object> getApiLimits() throws InvalidRequestException, SKlikException{
        return sendRequest(API_LIMITS_METHOD_NAME, null, null);
    }

    /**
     * Performed login to an SKlik account and sets the current session
     * @param username - user's email
     * @param password 
     * @return received response due to client.login API method specification
     * @throws InvalidRequestException
     * @throws SKlikException
     */
    public Map<String, Object> login(String username, String password) throws InvalidRequestException, SKlikException {
        Map<String, Object> resp = send(LOGIN_METHOD_NAME, new String[] { username, password }, false);
        session = (String) resp.get("session");
        return resp;
    }

    /**
     * Prepares required and additional parameters for send operation. Session
     * must be established at this point.
     * 
     * @param method
     *            - called API method name
     * @param params
     *            - additional parameters
     * @param userId
     *            - SKlik account id, it is required for manipulating with
     *            artifacts which are not owned directly by account used for
     *            login.
     * @return response Map, structure may differ in addition to called method
     *         specification
     * @throws InvalidRequestException
     * @throws SKlikException
     */
    public Map<String, Object> sendRequest(String method, Object[] params, Integer userId)
            throws InvalidRequestException, SKlikException {
        if (session == null) {
            throw new InvalidRequestException("It is necessary to call login method first! (no session available)");
        }

        // build user struct. It is used for request authentication against API
        Map<String, Object> userStruct = new LinkedHashMap<>();
        userStruct.put("session", session);
        if (userId != null) {
            userStruct.put("userId", userId);
        }

        // check for input parameters
        Object[] requestParams;
        if (params != null && params.length > 0) {
            requestParams = new Object[params.length + 1];
            System.arraycopy(params, 0, requestParams, 1, params.length);
            requestParams[0] = userStruct;
        } else {
            requestParams = new Object[] { userStruct };
        }

        Map<String, Object> response = send(method, requestParams, true);
        // check for the refreshed session
        if (response.containsKey("session")) {
            session = (String) response.get("session");
          //  logger.debug("Setting refreshed session to " + session);
        }

        return response;
    }

    /**
     * Sends a request to API using RPC client. Provides automatic Too many
     * requests per second recovery mechanism. If this problem occurs, the
     * request is repeated after 1 second.
     * 
     * @param method
     * @param params
     * @return
     * @throws InvalidRequestException
     * @throws SKlikException
     */
    protected Map<String, Object> send(String method, Object[] params, boolean logParams) throws InvalidRequestException, SKlikException {
        try {
            Map<String, Object> response = (HashMap<String, Object>) rpcClient.execute(method, params);
            

            int status = (Integer) response.get("status");
            if (status == 415) {
              //  logger.info("Too many requests exception detected. Waiting 1 second.");
                // too many requests exception detected, wait 1 second and try
                // again
                try {
                    System.out.println("sleeping");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {     
              //      logger.error("Unable to proceed sleep, thread interrupted", e);
                }
                return send(method, params, logParams);
            } else if (status >= 400) {
                throw new SKlikException("Request error with status code " + status + " and status message "
                        + response.get("statusMessage"), status, response);
            }
            return response;

        } catch (XmlRpcException ex) {
            if(logParams){
                throw new InvalidRequestException("XML-RPC Exception for " + Arrays.toString(params), ex);
            }
            throw new InvalidRequestException("XML-RPC Exception", ex);
        }
    }

    private ForeignAccount transformToObject(Map<String, Object> foreignAccountResp) throws InvalidRequestException {
        try {
            ForeignAccount account = new ForeignAccount();
            if (foreignAccountResp.get(FIELD_ACCESS) != null)
                account.setAccess((String) foreignAccountResp.get(FIELD_ACCESS));
            if (foreignAccountResp.get(FIELD_RELATION_NAME) != null)
                account.setRelationName((String) foreignAccountResp.get(FIELD_RELATION_NAME));
            if (foreignAccountResp.get(FIELD_RELATION_STATUS) != null)
                account.setRelationStatus((String) foreignAccountResp.get(FIELD_RELATION_STATUS));
            if (foreignAccountResp.get(FIELD_USERNAME) != null)
                account.setUsername((String) foreignAccountResp.get(FIELD_USERNAME));
            if (foreignAccountResp.get(FIELD_USER_ID) != null)
                account.setUserId((Integer) foreignAccountResp.get(FIELD_USER_ID));

            return account;
        } catch (NumberFormatException ex) {
            throw new InvalidRequestException(ex);
        }

    }

}
