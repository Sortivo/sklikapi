/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.sortivo.sklikapi;

import org.joda.time.DateTime;

/**
 *
 * @author Jan Dufek
 */
public class Ad {
    
    private Integer id;
    private String creative1;
    private String creative2;
    private String creative3;
    private String clickthruText;
    private String clickthruUrl;
    private boolean removed;
    private String status;
    private DateTime createDate;
    private Integer groupId;
    private String premiseMode;
    private Integer premiseId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getPremiseId() {
        return premiseId;
    }

    public void setPremiseId(Integer premiseId) {
        this.premiseId = premiseId;
    }

    

    

    public String getCreative1() {
        return creative1;
    }

    public void setCreative1(String creative1) {
        this.creative1 = creative1;
    }

    public String getCreative2() {
        return creative2;
    }

    public void setCreative2(String creative2) {
        this.creative2 = creative2;
    }

    public String getCreative3() {
        return creative3;
    }

    public void setCreative3(String creative3) {
        this.creative3 = creative3;
    }

    public String getClickthruText() {
        return clickthruText;
    }

    public void setClickthruText(String clickthruText) {
        this.clickthruText = clickthruText;
    }

    public String getClickthruUrl() {
        return clickthruUrl;
    }

    public void setClickthruUrl(String clickthruUrl) {
        this.clickthruUrl = clickthruUrl;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }

    public String getPremiseMode() {
        return premiseMode;
    }

    public void setPremiseMode(String premiseMode) {
        this.premiseMode = premiseMode;
    }

    
    
    
}
