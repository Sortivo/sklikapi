package cz.sortivo.sklikapi;

import org.joda.time.DateTime;
/**
 *
 * @author Jan Dufek
 */
public class Group {
    
    private Integer id;
    private String name;
    private boolean removed;
    private Integer cpc;
    private Integer cpcContext;
    private Status status;
    private Integer campaignId;
    private DateTime createDate;

    public Integer getId() {
        return id;
    }

    protected void setId(Integer id) {
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

    protected void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public DateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }
    
    
    
}
