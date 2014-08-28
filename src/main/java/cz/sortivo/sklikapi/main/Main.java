package cz.sortivo.sklikapi.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.sortivo.sklikapi.Ad;
import cz.sortivo.sklikapi.AdDAO;
import cz.sortivo.sklikapi.Campaign;
import cz.sortivo.sklikapi.CampaignDAO;
import cz.sortivo.sklikapi.Client;
import cz.sortivo.sklikapi.EntityType;
import cz.sortivo.sklikapi.Group;
import cz.sortivo.sklikapi.GroupDAO;
import cz.sortivo.sklikapi.Keyword;
import cz.sortivo.sklikapi.KeywordDAO;
import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

public class Main {

    static final int KEYWORDS_BATCH_SIZE = 3000;
    
    public static void main(String[] args) throws InvalidRequestException {
        
        Client client = new Client();
        CampaignDAO campaignDAO = new CampaignDAO(client);
        GroupDAO groupDAO = new GroupDAO(client);
        AdDAO adDAO = new AdDAO(client);
        KeywordDAO keywordDAO = new KeywordDAO(client);
        
        
        try {
            client.login(args[0], args[1]);
            
            Integer userId = null;
            boolean pauseCampaign = true;
            Integer campaignId = null;
            
            
            userId = new Integer(args[2]);
            campaignId = new Integer(args[3]);
            pauseCampaign = new Boolean(args[4]);
           
            
            
            Campaign campaign = new Campaign();
            campaign.setId(campaignId);
            
            if(pauseCampaign){
                List<Campaign> campaigns = new LinkedList<>();
                campaigns.add(campaign);
                campaignDAO.pause(campaigns, userId);
            }
            
            List<Integer> campaignIds = new ArrayList<Integer>(Arrays.asList(new Integer[]{campaignId}));
            List<Group> groups = groupDAO.listGroups(campaignIds, false, userId);
            List<Ad> ads = adDAO.listAds(campaignIds, EntityType.CAMPAIGN, false, userId); 

            
            int start = 0;
            int stop = 0;

            
            for(int i = 0; i < ads.size(); i+= 100 ){
                if(ads.size()-start < 100){
                    stop = ads.size();
                }else{
                    stop = start + 100;
                }
                
                adDAO.pause(ads.subList(start, stop), userId);
                start += 100;  
            }
            
            ads = null;
            
            start = 0;
            stop = 0;
            for(int i = 0; i < groups.size(); i+= 100 ){
                if(groups.size()-start < 100){
                    stop = groups.size();
                }else{
                    stop = start + 100;
                }
                
                Map<String, Object> res = groupDAO.pause(groups.subList(start, stop), userId);
                start += 100;  
            }
            
            List<Integer> groupIds = new ArrayList<>();
            for(Group g : groups){
                groupIds.add(g.getId());
            }
            groups = null;
            
            List<Keyword> keywords = keywordDAO.listKeywords(groupIds, false, userId);
            start = 0;
            for(int i = 0; i < keywords.size(); i+= KEYWORDS_BATCH_SIZE ){
                if(keywords.size()-start < KEYWORDS_BATCH_SIZE){
                    stop = keywords.size();
                }else{
                    stop = start + KEYWORDS_BATCH_SIZE;
                }
                try{
                    keywordDAO.pause(keywords.subList(start, stop), userId);
                }catch(SKlikException e){
                    System.out.println("Chyba: " + keywords.get(start) + e);
                }
                start += KEYWORDS_BATCH_SIZE;  
               
            }

        } catch (SKlikException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

    }

}
