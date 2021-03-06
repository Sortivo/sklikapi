package cz.sortivo.sklikapi.exception;

import java.util.LinkedList;
import java.util.List;

import cz.sortivo.sklikapi.SKlikObject;
import cz.sortivo.sklikapi.bean.Ad;
import cz.sortivo.sklikapi.bean.Response;

public class AdCreationException extends EntityCreationException {

    public AdCreationException(String message, List<Response<Ad>> entityResponses, Throwable cause) {
        super(message, AdCreationException.mapResponses(entityResponses), cause);

    }

    public static List<Response<? extends SKlikObject>> mapResponses(List<Response<Ad>> entityResponses) {
        List<Response<? extends SKlikObject>> responsesList = new LinkedList<>();
        responsesList.addAll(entityResponses);
        return responsesList;
    }

    private static final long serialVersionUID = 1L;

}
