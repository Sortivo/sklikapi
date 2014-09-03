package cz.sortivo.sklikapi.exception;

import java.util.List;

import cz.sortivo.sklikapi.bean.Ad;
import cz.sortivo.sklikapi.bean.Response;

public class AdCreationException extends EntityCreationException {

    List<Response<Ad>> failedAds;
    
    public AdCreationException(String message, List<Response<Ad>> entityResponses, Throwable cause) {
        super(message, cause);
        failedAds = entityResponses;
    }

    private static final long serialVersionUID = 1L;

}
