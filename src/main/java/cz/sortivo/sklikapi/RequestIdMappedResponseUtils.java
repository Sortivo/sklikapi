package cz.sortivo.sklikapi;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RequestIdMappedResponseUtils extends ResponseUtils {

    private static final String FIELD_REQUEST_ID = "requestId";
     
    public RequestIdMappedResponseUtils(String idsStructName) {
        super(idsStructName);
    }
      
    @Override
    protected Integer parseRequestId(Object object) {
       
        return (Integer)object;
    }

    @Override
    protected String getRequestIdFieldName() {
        return FIELD_REQUEST_ID;
    }

    @Override
    protected <T> Map<Integer, T> mapEntitiesWithRequestId(List<T> entities) {
        Map<Integer, T> mappedEntities = new LinkedHashMap<>();
        
        
        for (T t : entities) {
            mappedEntities.put(t.hashCode(), t);
        }
        return mappedEntities;
    }

}
