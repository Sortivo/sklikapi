package cz.sortivo.sklikapi.exception;

import java.util.LinkedList;
import java.util.List;

import cz.sortivo.sklikapi.SKlikObject;
import cz.sortivo.sklikapi.bean.Campaign;
import cz.sortivo.sklikapi.bean.Group;
import cz.sortivo.sklikapi.bean.Response;

public class GroupCreationException extends EntityCreationException {



    public GroupCreationException(String message, List<Response<Group>> entityResponses, Throwable cause) {
        super(message, GroupCreationException.mapResponses(entityResponses) , cause);
        // TODO Auto-generated constructor stub
    }
    
    public static List<Response<? extends SKlikObject>> mapResponses(List<Response<Group>> entityResponses) {
        List<Response<? extends SKlikObject>> responsesList = new LinkedList<>();
        responsesList.addAll(entityResponses);

        return responsesList;
    }

    private static final long serialVersionUID = 1L;
}
