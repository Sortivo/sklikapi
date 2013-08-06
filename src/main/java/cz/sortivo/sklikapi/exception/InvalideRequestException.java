package cz.sortivo.sklikapi.exception;

/**
 * 
 * @author Jan Dufek
 */
public class InvalideRequestException extends Exception{

    private static final long serialVersionUID = 1L;

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
