/*
 * Author - Jan Dufek, dufeja@gmail.com
 * Copying and using only with permission of the author.
 */
package cz.sortivo.sklikapi.bean;

import java.util.List;

import org.joda.time.DateTime;

import cz.sortivo.sklikapi.SKlikObject;
import cz.sortivo.sklikapi.Status;
import cz.sortivo.sklikapi.exception.InvalidRequestException;
import cz.sortivo.sklikapi.exception.SKlikException;

/**
 *
 * @author Jan Dufek
 */
public class Ad  implements SKlikObject{
    
    
    private Integer id;
    private String creative1;
    private String creative2;
    private String creative3;
    private String clickthruText;
    private String clickthruUrl;
    private boolean deleted;
    private Status status;
    private DateTime createDate;
    private Integer groupId;
    private String premiseMode;
    private Integer premiseId;
    
    public Ad(Ad ad) {
        this.id = ad.getId();
        this.creative1 = ad.getCreative1();
        this.creative2 = ad.getCreative2();
        this.creative3 = ad.getCreative3();
        this.clickthruText = ad.getClickthruText();
        this.clickthruUrl = ad.getClickthruUrl();
        this.groupId = ad.getGroupId();
    }
    
    public Ad(){
        
    }

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



    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Status getStatus() {
        if (status == null){
            return Status.ACTIVE;
        }
        return status;
    }

    public void setStatus(Status status) {
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

    
    
    @Override
    public String toString() {
        return "Ad [id=" + id + ", creative1=" + creative1 + ", creative2="
                + creative2 + ", creative3=" + creative3 + ", clickthruText="
                + clickthruText + ", clickthruUrl=" + clickthruUrl
                + ", deleted=" + deleted + ", status=" + status
                + ", createDate=" + createDate + ", groupId=" + groupId
                + ", premiseMode=" + premiseMode + ", premiseId=" + premiseId
                + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clickthruText == null) ? 0 : clickthruText.hashCode());
        result = prime * result + ((clickthruUrl == null) ? 0 : clickthruUrl.hashCode());
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + ((creative1 == null) ? 0 : creative1.hashCode());
        result = prime * result + ((creative2 == null) ? 0 : creative2.hashCode());
        result = prime * result + ((creative3 == null) ? 0 : creative3.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ad other = (Ad) obj;
        if (clickthruText == null) {
            if (other.clickthruText != null)
                return false;
        } else if (!clickthruText.equals(other.clickthruText))
            return false;
        if (clickthruUrl == null) {
            if (other.clickthruUrl != null)
                return false;
        } else if (!clickthruUrl.equals(other.clickthruUrl))
            return false;
        if (creative1 == null) {
            if (other.creative1 != null)
                return false;
        } else if (!creative1.equals(other.creative1))
            return false;
        if (creative2 == null) {
            if (other.creative2 != null)
                return false;
        } else if (!creative2.equals(other.creative2))
            return false;
        if (creative3 == null) {
            if (other.creative3 != null)
                return false;
        } else if (!creative3.equals(other.creative3))
            return false;
        return true;
    }

    public boolean isRemoved() {
        // TODO Auto-generated method stub
        return false;
    }

    
}
