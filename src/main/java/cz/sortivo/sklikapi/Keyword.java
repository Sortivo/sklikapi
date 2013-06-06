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
public class Keyword {
    
    private Integer id;
    private String name;
    private String matchType;
    private boolean removed;
    private String status;
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

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
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
    
    
}
