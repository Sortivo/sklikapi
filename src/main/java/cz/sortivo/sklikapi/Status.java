/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.sortivo.sklikapi;

/**
 *
 * @author Jan Dufek
 */
public enum Status {
    ACTIVE("active"),
    SUSPEND("suspend");
    
    private Status(String statusText){
        this.statusText = statusText;
    }
 
    private String statusText;
    
    public String getStatusText(){
        return statusText;
    }
}
