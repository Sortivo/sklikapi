package cz.sortivo.sklikapi.exception;

import java.util.List;

import cz.sortivo.sklikapi.bean.AdResponse;

public class AdCreationException extends Exception{

   private List<AdResponse> failedAds;

    public AdCreationException(String message, List<AdResponse> failedAds, Throwable cause) {
        super(message, cause);
        this.failedAds = failedAds;
    }

  

}
