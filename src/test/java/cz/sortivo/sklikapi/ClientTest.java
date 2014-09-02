package cz.sortivo.sklikapi;

import cz.sortivo.sklikapi.exception.SKlikException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Jan Dufek
 */
public class ClientTest {


    /**
     * Test of login method, of class Client.
     * 
     */
    @Test
    public void testLogin() throws Exception {
        XmlRpcClient rpcClient = mock(XmlRpcClient.class);
        Client client = new Client();
        
        Field clientField = client.getClass().getDeclaredField("rpcClient");
        clientField.setAccessible(true);
        clientField.set(client, rpcClient);
        
        Map<Object, Object> mapToReturn = new HashMap<>();
        mapToReturn.put("session", "asdlkfjsdfasdf");
        mapToReturn.put("status", 200);
        mapToReturn.put("statusMessage", "OK");
        
        when(rpcClient.execute(anyString(), any(String[].class))).thenReturn(mapToReturn);
        
        client.login("username", "password");
        
        verify(rpcClient, times(1)).execute("client.login", new String[]{"username", "password"});
        
    }
//    @Test
//    public void testSendRequest() throws Exception{
//        XmlRpcClient rpcClient = mock(XmlRpcClient.class);
//        Client client = new Client();
//        
//        Field clientField = client.getClass().getDeclaredField("rpcClient");
//        clientField.setAccessible(true);
//        clientField.set(client, rpcClient);
//        
//        String method = "method.name";
//        String session = "asdlkfjsdfasdf";
//        
//        Map<Object, Object> mapToReturn = new HashMap<>();
//        mapToReturn.put("session", session);
//        mapToReturn.put("status", 200);
//        mapToReturn.put("statusMessage", "OK");
//        
//        when(rpcClient.execute(anyString(), any(String[].class))).thenReturn(mapToReturn);
//        
//        client.login("", "");
//        client.sendRequest(method, new String[]{});
//        
//        verify(rpcClient, times(1)).execute(method, new String[]{session});
//       
//    }
    
    @Test(expected=SKlikException.class)
    public void testExecutionFailure() throws Exception {
        XmlRpcClient rpcClient = mock(XmlRpcClient.class);
        Client client = new Client();
        
        Field clientField = client.getClass().getDeclaredField("rpcClient");
        clientField.setAccessible(true);
        clientField.set(client, rpcClient);
        
        Map<Object, Object> mapToReturn = new HashMap<>();
        mapToReturn.put("session", "asdlkfjsdfasdf");
        mapToReturn.put("status", 406);
        mapToReturn.put("statusMessage", "OK");
        
        when(rpcClient.execute(anyString(), any(String[].class))).thenReturn(mapToReturn);
        
        client.login("", "");
        client.sendRequest("method", new String[]{});
    }
    
}
