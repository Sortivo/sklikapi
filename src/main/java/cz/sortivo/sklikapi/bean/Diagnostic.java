package cz.sortivo.sklikapi.bean;

import java.util.List;
import java.util.Map;

public class Diagnostic {
    
    public static final String FIELD_REQUEST_ID = "requestId";
    public static final String FIELD_NAME = "id";
    public static final String FIELD_OBJECT_ID = "objectId";
    public static final String FIELD_TYPE = "type";
    
    public static final String FIELD_TYPE_ERROR = "error";
    
    
    private final String name;
    private final Integer requestId;
    private final String type;
    private final Map<String, Object> diagnosticResponse;
   
    public Diagnostic(String name, Integer requestId, String type, Map<String, Object> diagnosticResponse) {
        super();
        this.name = name;
        this.requestId = requestId;
        this.type = type;
        this.diagnosticResponse = diagnosticResponse;
    }

    public String getName() {
        return name;
    }
  
    public Integer getRequestId() {
        return requestId;
    }
 
    public String getType() {
        return type;
    }
  
    public Map<String, Object> getDiagnosticResponse() {
        return diagnosticResponse;
    }

    @Override
    public String toString() {
        return "Diagnostic [name=" + name + ", requestId=" + requestId + ", type=" + type + ", diagnosticResponse="
                + diagnosticResponse + "]";
    }
  
    
}
