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
    SUSPEND("suspend"),
    NOACTIVE("noactive");
    
    private Status(String statusText){
        this.statusText = statusText;
    }
 
    private String statusText;
    
    public String getStatusText(){
        return statusText;
    }
    
    public static Status getStatus(String statusText){
        if (statusText.equalsIgnoreCase("active")){
            return Status.ACTIVE;
        } 
        if (statusText.equalsIgnoreCase("suspend")){
            return Status.SUSPEND;
        } 
        if (statusText.equalsIgnoreCase("noactive")){
            return Status.SUSPEND;
        } 

        return null;
    }
}
