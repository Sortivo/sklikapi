package cz.sortivo.sklikapi.bean;

import java.util.List;


public class AdResponse {
    Ad ad;
    List<Diagnostic> diagnostics;
    
    public AdResponse(){

    }
    
    public AdResponse(Ad ad, List<Diagnostic> diagnostics) {
        super();
        this.ad = ad;
        this.diagnostics = diagnostics;
    }
    public Ad getAd() {
        return ad;
    }
    public void setAd(Ad ad) {
        this.ad = ad;
    }
    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }
    public void setDiagnostics(List<Diagnostic> diagnostics) {
        this.diagnostics = diagnostics;
    }

    @Override
    public String toString() {
        return "AdResponse [ad=" + ad + ", diagnostics=" + diagnostics + "]";
    }
    
    
    
   
}
