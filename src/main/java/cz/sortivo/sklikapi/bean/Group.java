package cz.sortivo.sklikapi.bean;

import org.joda.time.DateTime;

import cz.sortivo.sklikapi.SKlikObject;
import cz.sortivo.sklikapi.Status;
/**
 *
 * @author Jan Dufek
 */
public class Group implements SKlikObject {
    
    private Integer id;
    private String name;
    private boolean removed;
    private Integer cpc;
    private Integer cpcContext;
    private Status status;
    private Integer campaignId;
    private DateTime createDate;
    private Integer maxUserDailyImpression;
    private Integer cpm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public Integer getCpc() {
        return cpc;
    }

    public void setCpc(Integer cpc) {
        this.cpc = cpc;
    }

    public Integer getCpcContext() {
        return cpcContext;
    }

    public void setCpcContext(Integer cpcContext) {
        this.cpcContext = cpcContext;
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

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public DateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getMaxUserDailyImpression() {
        return maxUserDailyImpression;
    }

    public void setMaxUserDailyImpression(Integer maxUserDailyImpression) {
        this.maxUserDailyImpression = maxUserDailyImpression;
    }

    public Integer getCpm() {
        return cpm;
    }

    public void setCpm(Integer cpm) {
        this.cpm = cpm;
    }

    @Override
    public String toString() {
        return "Group [id=" + id + ", name=" + name + ", removed=" + removed + ", cpc=" + cpc + ", cpcContext=" + cpcContext + ", status=" + status + ", campaignId=" + campaignId + ", createDate="
                + createDate + ", maxUserDailyImpression=" + maxUserDailyImpression + ", cpm=" + cpm + "]";
    }
    
    
    
    
}
