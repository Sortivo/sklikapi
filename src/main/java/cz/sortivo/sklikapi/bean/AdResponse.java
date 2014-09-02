package cz.sortivo.sklikapi.bean;


public class AdResponse extends Response {
    Ad ad;
    
    
    AdResponse(int status, String statusMessage, Ad ad){
        super(status, statusMessage);
        this.ad = ad;
    }
}
