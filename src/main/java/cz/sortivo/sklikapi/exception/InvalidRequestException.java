package cz.sortivo.sklikapi.exception;

/**
 * 
 * @author Jan Dufek
 */
public class InvalidRequestException extends Exception{

    private static final long serialVersionUID = 1L;

    public InvalidRequestException(Throwable cause) {
        super(cause);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestException(String message) {
        super(message);
    }
    
    
    
}
