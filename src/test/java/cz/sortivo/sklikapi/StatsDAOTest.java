package cz.sortivo.sklikapi;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

/**
 *
 * @author Michal Ličko
 */
public class StatsDAOTest {

    @Test
    public void testSendRequest() throws Exception{
        Client client = new Client();
        try{
            client.login("USERNAME", "PASSWORD");
        }catch(InvalidRequestException | SKlikException ex){
            return;
        }
        
        CampaignDAO campaignDAO = new CampaignDAO(client);
        StatsDAO statsDAO = new StatsDAO(client);
        
        List<Campaign> campaigns = campaignDAO.listCampaigns();
        
        Campaign firstCamp = campaigns.get(0);
        Stats stats = statsDAO.getStats(firstCamp, new DateTime().minusDays(10), new DateTime());
        
    }
   
}
