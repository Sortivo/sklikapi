package cz.sortivo.sklikapi.exception;

import java.util.List;

import cz.sortivo.sklikapi.bean.Group;
import cz.sortivo.sklikapi.bean.Response;

public class GroupCreationException extends EntityCreationException {

    List<Response<Group>> failedGroups;
    
    public GroupCreationException(String message, List<Response<Group>> entityResponses, Throwable cause) {
        super(message, cause);
        failedGroups = entityResponses;
    }

    
    
    public List<Response<Group>> getFailedGroups() {
        return failedGroups;
    }


    private static final long serialVersionUID = 1L;
}
