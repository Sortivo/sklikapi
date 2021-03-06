/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.sortivo.sklikapi;

/**
 *
 * @author Jan Dufek
 */
public class Attributes {
    
    private Status status;
    private Integer cpc;
    
    public Attributes(){
        
    }
    
    public Attributes(Status status){
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getCpc() {
        return cpc;
    }

    public void setCpc(Integer cpc) {
        this.cpc = cpc;
    }
    
    
}
