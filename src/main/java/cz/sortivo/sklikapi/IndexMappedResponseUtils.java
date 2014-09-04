package cz.sortivo.sklikapi;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IndexMappedResponseUtils extends ResponseUtils {

    private static final String FIELD_REQUEST_ID = "structName";
    private static final String STRUCT_NAME_PREFIX = "structName[";
    private static final String STRUCT_NAME_POSTFIX = "]";
    
    
    public IndexMappedResponseUtils(String idsStructName) {
        super(idsStructName);
    }
      
    @Override
    protected Integer parseRequestId(Object object) {
       
        if(object == null)return null;
        
        String str = (String)object;
         
        return new Integer(str.replaceAll(".*\\[", "").replace(STRUCT_NAME_POSTFIX, ""));
    }

    @Override
    protected String getRequestIdFieldName() {
        return FIELD_REQUEST_ID;
    }

    @Override
    protected <T> Map<Integer, T> mapEntitiesWithRequestId(List<T> entities) {
        Map<Integer, T> mappedEntities = new LinkedHashMap<>();
        
        int i = 0;
        for (T t : entities) {
            mappedEntities.put(i++, t);
        }
        return mappedEntities;
    }

}
