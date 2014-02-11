package cz.sortivo.sklikapi;
import java.util.List;

import org.junit.Test;

import cz.sortivo.sklikapi.Attributes;
import cz.sortivo.sklikapi.Client;
import cz.sortivo.sklikapi.Keyword;
import cz.sortivo.sklikapi.KeywordDAO;
import cz.sortivo.sklikapi.Status;
import cz.sortivo.sklikapi.exception.InvalideRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

public class KeywordDAOTest {
    
    @Test
    public void setAttributesTest(){
        
        try{
            Client client = new Client();
            client.login("user", "pwd");
   
            KeywordDAO kwDAO = new KeywordDAO(client);
            List<Keyword> kws = kwDAO.listKeywords(10183744);
            Keyword kw = null;
            for(Keyword k : kws){
                if (k.getId() == 449198718){
                    kw = k;
                    break;
                }
            }
            
            Attributes a = new Attributes();
            a.setCpc(-1);
            a.setStatus(Status.ACTIVE);
            
            kwDAO.setAttributes(kw.getId(), a);
           
        }catch(InvalideRequestException | SKlikException ex){
            ex.printStackTrace();
        }
    }

}
