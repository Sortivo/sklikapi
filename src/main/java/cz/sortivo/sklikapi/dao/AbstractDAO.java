package cz.sortivo.sklikapi.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.sortivo.sklikapi.Client;
import cz.sortivo.sklikapi.ResponseUtils;
import cz.sortivo.sklikapi.SKlikObject;
import cz.sortivo.sklikapi.bean.Response;
import cz.sortivo.sklikapi.exception.EntityCreationException;
import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

public abstract class AbstractDAO<T extends SKlikObject> {

    abstract protected Map<String, Object> transformFromObject(T entity, final Set<String> ALOWED_FIELDS);

    abstract protected boolean supportsRequestId();

    abstract protected ResponseUtils getResponseUtils();

    abstract protected EntityCreationException getCreationException(String message, List<Response<T>> entityResponses,
            Throwable cause);

    abstract public List<Response<T>> create(List<T> entities) throws InvalidRequestException, EntityCreationException,
            SKlikException;

    abstract public List<Response<T>> update(List<T> entities) throws InvalidRequestException, EntityCreationException,
            SKlikException;

    protected Client client;

    private static final String FIELD_REQUEST_ID = "requestId";

    /**
     * Proceed specified operation (usually create/update) on specified list of
     * ads
     * 
     * @param entities
     * @param ALLOWED_FIELDS
     *            - fields that can be send via API
     * @param METHOD_NAME
     *            - specifies method that will be called
     * @return Response object, may contain diagnostics if it was not processed
     *         correctly but was written to SKlik. Also may contain objects with
     *         updated id;
     * @throws InvalidRequestException
     *             if request or response is syntacticly incorrect for
     *             processing with XMLRPC client
     * @throws CreationException
     *             - May be thrown if some of ads contains fatal error due to
     *             the whole transaction is rollbacked. Contains additional
     *             information with error cause and list of ads that caused
     *             errors.
     * @throws SKlikException
     * @throws EntityCreationException
     */
    protected List<Response<T>> save(List<T> entities, final Set<String> ALLOWED_FIELDS, final String METHOD_NAME,
            final Integer REQUEST_SIZE_LIMIT) throws InvalidRequestException, SKlikException, EntityCreationException {
        List<Map<String, Object>> entityMaps = new ArrayList<>();

        if (entities == null) {
            throw new IllegalArgumentException("Ads cannot be null");
        }

        if (entities.size() > REQUEST_SIZE_LIMIT) {
            throw new IllegalArgumentException(
                    "Count of ads to create exceeds allowed API limit, see api.limits for current details");
        }

        if (entities.size() == 0) {
            return new LinkedList<Response<T>>();
        }

        Map<String, Object> entityMap;
        for (T entity : entities) {
            entityMap = transformFromObject(entity, ALLOWED_FIELDS);
            if (supportsRequestId()) {
                entityMap.put(FIELD_REQUEST_ID, entity.hashCode());
            }
            entityMaps.add(entityMap);
        }

        Map<String, Object> response;
        List<Response<T>> entityResponses;

        ResponseUtils responseUtils = getResponseUtils();

        try {
            response = client.sendRequest(METHOD_NAME, new Object[] { entityMaps });
            entityResponses = responseUtils.buildResponses(entities, response, false);
        } catch (SKlikException e) {
            // try to find some diagnostics
            if (e.getStatus() == 400 || e.getStatus() == 406) {
                entityResponses = responseUtils.buildResponses(entities, e.getResponse(), true);
                if (entityResponses.size() > 0) {
                    throw getCreationException("Could not create " + entities.size()
                            + " entities! Requested batch contains errors.", entityResponses, e);
                }
            }
            throw e;
        }

        return entityResponses;
    }

}
