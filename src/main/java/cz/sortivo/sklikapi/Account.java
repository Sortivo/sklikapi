/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.sortivo.sklikapi;

/**
 *
 * @author Jan Dufek
 */
public class Account {
    public static final String ACCESS_READ_ONLY = "r";
    public static final String ACCESS_READ_AND_WRITE = "rw";
    public static final String RELATION_STATUS_LIVE = "live";
    public static final String RELATION_STATUS_OFFER = "offer";
    public static final String RELATION_STATUS_REQUEST = "request";
    
    private int userId;
    private String access;
    private String relationStatus;

    public Account(int userId) {
        this.userId = userId;
    }

    public Account(int userId, String access, String relationStatus) {
        this.userId = userId;
        this.access = access;
        this.relationStatus = relationStatus;
    }

    public int getUserId() {
        return userId;
    }

    public String getAccess() {
        return access;
    }

    public String getRelationStatus() {
        return relationStatus;
    }
    
    
    
}
