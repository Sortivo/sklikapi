/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.sortivo.sklikapi;

/**
 *
 * @author Jan Dufek
 */
public class ForeignAccount {
    public static final String ACCESS_READ_ONLY = "r";
    public static final String ACCESS_READ_AND_WRITE = "rw";
    public static final String RELATION_STATUS_LIVE = "live";
    public static final String RELATION_STATUS_OFFER = "offer";
    public static final String RELATION_STATUS_REQUEST = "request";
    
    private int userId;
    private String access;
    private String relationStatus;
    private String relationName;
    private String username;

    public ForeignAccount(int userId, String access, String relationStatus, String relationName, String username) {
        this.userId = userId;
        this.access = access;
        this.relationStatus = relationStatus;
        this.relationName = relationName;
        this.username = username;
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

    public String getRelationName() {
        return relationName;
    }

    public String getUsername() {
        return username;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public void setRelationStatus(String relationStatus) {
        this.relationStatus = relationStatus;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ForeignAccount() {
    }

    
   
    
    
    
}
