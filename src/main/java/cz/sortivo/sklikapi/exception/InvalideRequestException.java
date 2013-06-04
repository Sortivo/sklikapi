package cz.sortivo.sklikapi.exception;

/**
 * 
 * @author Jan Dufek
 */
public class InvalideRequestException extends Exception{

    public InvalideRequestException(Throwable cause) {
        super(cause);
    }

    public InvalideRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalideRequestException(String message) {
        super(message);
    }
    
    
    
}
