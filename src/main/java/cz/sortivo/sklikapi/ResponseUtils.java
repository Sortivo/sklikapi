package cz.sortivo.sklikapi;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.sortivo.sklikapi.bean.Diagnostic;
import cz.sortivo.sklikapi.bean.Response;

public abstract class ResponseUtils {
    
    private static final String FIELD_DIAGNOSTICS = "diagnostics";
    private String idsStructName;
    
    public ResponseUtils(String idsStructName) {
        this.idsStructName = idsStructName;
    }
    
    abstract Integer parseRequestId(Object object);
    abstract String getRequestIdFieldName();
    abstract <T> Map<Integer, T> mapEntitiesWithRequestId(List<T> entities);
    
    @SuppressWarnings("unchecked")
    public Map<Integer, List<Diagnostic>> mapDiagnostics(Map<String, Object> response,  boolean errorsOnly) {

        Map<Integer, List<Diagnostic>> diagnostics = new LinkedHashMap<>();

        if (response.containsKey(FIELD_DIAGNOSTICS)) {
            Object[] diagnosticMapsObj = (Object[]) response.get(FIELD_DIAGNOSTICS);
            for (Object obj : diagnosticMapsObj) {

                Map<String, Object> map = (Map<String, Object>) obj;
                
                if (map.containsKey(getRequestIdFieldName())) {
                    // identified request found
                    List<Diagnostic> curDiagnostic;
                    Integer requestId = (Integer) parseRequestId(map.get(getRequestIdFieldName()));
                    String type = (String) map.get(Diagnostic.FIELD_TYPE);

                    if (!errorsOnly || Diagnostic.FIELD_TYPE_ERROR.equals(type)) {

                        if (diagnostics.containsKey(requestId)) {
                            curDiagnostic = diagnostics.get(requestId);
                        } else {
                            curDiagnostic = new LinkedList<>();
                            diagnostics.put(requestId, curDiagnostic);
                        }

                        curDiagnostic.add(new Diagnostic((String) map.get(Diagnostic.FIELD_NAME), (Integer) map
                                .get(Diagnostic.FIELD_REQUEST_ID), (String) map.get(Diagnostic.FIELD_TYPE),
                                (Map<String, Object>) map));
                    }
                }
            }

        }

        return diagnostics;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SKlikObject> List<Response<T>> buildResponses(List<T> entities, Map<String, Object> response, boolean errorsOnly) {
        Map<Integer, List<Diagnostic>> diagnostics = mapDiagnostics(response, errorsOnly);
        Map<Integer, T> mappedEntities = mapEntitiesWithRequestId(entities);
        
        List<Response<T>> responses = new LinkedList<>();

        List<Integer> ids = null;
        if (response.containsKey(idsStructName)) {
            ids = (List<Integer>) response.get(idsStructName);         
        }

        int curIndex = 0;
        for (Integer entityKey : mappedEntities.keySet()) {

            if (!errorsOnly || diagnostics.containsKey(entityKey)) {
                if(ids != null){
                    mappedEntities.get(entityKey).setId(ids.get(curIndex++));
                }
                
                responses.add(new Response<T>(mappedEntities.get(entityKey), diagnostics.get(entityKey)));
            }
        }

        return responses;
    }

}
