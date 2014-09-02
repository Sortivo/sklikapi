package cz.sortivo.sklikapi;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.sortivo.sklikapi.bean.Ad;
import cz.sortivo.sklikapi.bean.AdResponse;
import cz.sortivo.sklikapi.bean.Diagnostic;

public class ResponseUtils {
    private static final String FIELD_DIAGNOSTICS = "diagnostics";
    private static final String FIELD_AD_IDS = "adIds";

    @SuppressWarnings("unchecked")
    public static Map<Integer, List<Diagnostic>> mapDiagnostics(Map<String, Object> response, boolean errorsOnly) {

        Map<Integer, List<Diagnostic>> diagnostics = new LinkedHashMap<>();

        if (response.containsKey(FIELD_DIAGNOSTICS)) {
            Object[] diagnosticMapsObj = (Object[]) response.get(FIELD_DIAGNOSTICS);
            for (Object obj : diagnosticMapsObj) {

                Map<String, Object> map = (Map<String, Object>) obj;
                
                if (map.containsKey(Diagnostic.FIELD_REQUEST_ID)) {
                    // identified request found
                    List<Diagnostic> curDiagnostic;
                    Integer requestId = (Integer) map.get(Diagnostic.FIELD_REQUEST_ID);
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
    public static List<AdResponse> buildsAdResponses(List<Ad> ads, Map<String, Object> response, boolean errorsOnly) {
        Map<Integer, List<Diagnostic>> diagnostics = ResponseUtils.mapDiagnostics(response, errorsOnly);

        List<AdResponse> responses = new LinkedList<>();

        List<Integer> ids = null;
        if (response.containsKey(FIELD_AD_IDS)) {
            ids = (List<Integer>) response.get(FIELD_AD_IDS);
            if(ids.size() != ads.size()){
                throw new IllegalStateException("Count of received ad ids does not match count of sent ads");
            }             
        }

        int curIndex = 0;
        for (Ad ad : ads) {

            if (!errorsOnly || diagnostics.containsKey(ad.hashCode())) {
                if(ids != null){
                    ad.setId(ids.get(curIndex));
                }
                
                responses.add(new AdResponse(ad, diagnostics.get(ad.hashCode())));
            }
        }

        return responses;
    }

}
