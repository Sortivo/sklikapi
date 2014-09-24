package cz.sortivo.sklikapi;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.sortivo.sklikapi.bean.Diagnostic;
import cz.sortivo.sklikapi.bean.Response;

public abstract class ResponseUtils {

    private static final String FIELD_DIAGNOSTICS = "diagnostics";
    private static final String FIELD_PROBLEMS = "problems";
    private String idsStructName;

    public ResponseUtils(String idsStructName) {
        this.idsStructName = idsStructName;
    }

    abstract Integer parseRequestId(Object object);

    abstract String getRequestIdFieldName();

    abstract <T> LinkedHashMap<Integer, T> mapEntitiesWithRequestId(List<T> entities);

    @SuppressWarnings("unchecked")
    public Map<Integer, List<Diagnostic>> mapDiagnostics(Map<String, Object> response, boolean errorsOnly) {

        Map<Integer, List<Diagnostic>> diagnostics = new LinkedHashMap<>();

        if (response.containsKey(FIELD_DIAGNOSTICS)) {

            Object[] diagnosticMapsObj = null;
            if (response.get(FIELD_DIAGNOSTICS) instanceof Object[]) {
                diagnosticMapsObj = (Object[]) response.get(FIELD_DIAGNOSTICS);
            } else {
                Map<String, Object> diagnosticsResponse = (Map<String, Object>) response.get(FIELD_DIAGNOSTICS);
                if (diagnosticsResponse.containsKey(FIELD_PROBLEMS)) {
                    diagnosticMapsObj = (Object[]) diagnosticsResponse.get(FIELD_PROBLEMS);
                }
            }

            if (diagnosticMapsObj != null) {
                for (Object obj : diagnosticMapsObj) {

                    Map<String, Object> map = (Map<String, Object>) obj;
                    //TODO mapa někdy neobsahuje requestId
                    if (map.containsKey(getRequestIdFieldName())) {
                        // identified request found
                        List<Diagnostic> curDiagnostic;
                        Integer requestId = (Integer) parseRequestId(map.get(getRequestIdFieldName()));
                        String type = (String) map.get(Diagnostic.FIELD_TYPE);

                        if (!errorsOnly || type == null || Diagnostic.FIELD_TYPE_ERROR.equals(type)) {

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

        }

        return diagnostics;
    }

    /**
     * Builds responses object by connecting response details with corresponding
     * object.
     * 
     * @param entities
     *            - input entities, which was passed to API request
     * @param response
     *            - raw response from API
     * @param errorsOnly
     *            - specifies if to map only failed entities with their
     *            responses
     * @return responses mapped to input entities. They are returned in same
     *         order as the input entities came
     */
    @SuppressWarnings("unchecked")
    public <T extends SKlikObject> List<Response<T>> buildResponses(List<T> entities, Map<String, Object> response,
            boolean errorsOnly) {
        Map<Integer, List<Diagnostic>> diagnostics = mapDiagnostics(response, errorsOnly);

        // it is guaranteed that map keys are in same order as input entities
        Map<Integer, T> mappedEntities = mapEntitiesWithRequestId(entities);

        List<Response<T>> responses = new LinkedList<>();

        List<Integer> ids = null;
        if (response.containsKey(idsStructName)) {
            Object[] idsArrayResponseObject = (Object[]) response.get(idsStructName);
            Integer[] idsArray = Arrays.copyOf(idsArrayResponseObject, idsArrayResponseObject.length, Integer[].class);
            ids = new LinkedList<>(Arrays.asList(idsArray));
        }

        int curIndex = 0;
        for (Integer entityKey : mappedEntities.keySet()) {

            if (!errorsOnly || diagnostics.containsKey(entityKey)) {
                if (ids != null) {
                    mappedEntities.get(entityKey).setId(ids.get(curIndex++));
                }

                responses.add(new Response<T>(mappedEntities.get(entityKey), diagnostics.get(entityKey)));
            }
        }

        return responses;
    }

}
