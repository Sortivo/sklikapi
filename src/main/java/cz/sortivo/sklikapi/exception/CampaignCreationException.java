package cz.sortivo.sklikapi.exception;

import java.util.LinkedList;
import java.util.List;

import cz.sortivo.sklikapi.SKlikObject;
import cz.sortivo.sklikapi.bean.Ad;
import cz.sortivo.sklikapi.bean.Campaign;
import cz.sortivo.sklikapi.bean.Response;

public class CampaignCreationException extends EntityCreationException {


    public CampaignCreationException(String message, List<Response<Campaign>> entityResponses, Throwable cause) {
        super(message, CampaignCreationException.mapResponses(entityResponses), cause);

    }

    public static List<Response<? extends SKlikObject>> mapResponses(List<Response<Campaign>> entityResponses) {
        List<Response<? extends SKlikObject>> responsesList = new LinkedList<>();
        responsesList.addAll(entityResponses);

        return responsesList;
    }
    private static final long serialVersionUID = 1L;

}
