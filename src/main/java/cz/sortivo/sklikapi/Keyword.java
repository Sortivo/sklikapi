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
public class Keyword implements SKlikObject {
    
    private Integer id;
    private String name;
    private MatchType matchType;
    private boolean removed;
    private Status status;
    private boolean disabled;
    private Integer cpc;
    private String url;
    private DateTime createDate;
    private Integer groupId;
    private Integer minCpc;

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

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
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

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Integer getCpc() {
        return cpc;
    }

    public void setCpc(Integer cpc) {
        this.cpc = cpc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getMinCpc() {
        return minCpc;
    }

    public void setMinCpc(Integer minCpc) {
        this.minCpc = minCpc;
    }

    @Override
    public String toString() {
        return "Keyword [id=" + id + ", name=" + name + ", matchType=" + matchType + ", removed=" + removed
                + ", status=" + status + ", disabled=" + disabled + ", cpc=" + cpc + ", url=" + url + ", createDate="
                + createDate + ", groupId=" + groupId + ", minCpc=" + minCpc + "]";
    }
    
    
}
