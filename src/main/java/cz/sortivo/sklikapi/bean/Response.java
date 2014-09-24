package cz.sortivo.sklikapi.bean;

import java.util.List;


public class Response<T> {
    
    boolean failed = false;
    T entity;
    List<Diagnostic> diagnostics;
    
    protected Response(){
        
    }
    
    
    
    public boolean isFailed() {
        return failed;
    }



    public void setFailed(boolean failed) {
        this.failed = failed;
    }



    public Response(T entity, List<Diagnostic> diagnostics){
        this.diagnostics = diagnostics;
        this.entity = entity;
    }
    
    public List<Diagnostic> getDiagnostics() {
        return diagnostics;
    }
    public void setDiagnostics(List<Diagnostic> diagnostics) {
        this.diagnostics = diagnostics;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "Response [entity=" + entity + ", diagnostics=" + diagnostics + "]";
    }
    
    
    

}
