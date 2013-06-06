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
public class Campaign {
    private Integer id;
    private String name;
    private boolean removed;
    private String status;
    private Integer dayBudget;
    private Integer exhaustedDayBudget;
    private String adSelection;
    private DateTime createDate;
    private Integer totalBudget;
    private Integer exhaustedTotalBudget;
    private Integer totalClicks;
    private Integer exhaustedTotalClicks;
    private Integer premiseId;
    private DateTime startDate;
    private DateTime endDate;
    
    public final static String STATUS_ACTIVE = "active";
    public final static String STATUS_SUSPEND = "suspend";

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDayBudget() {
        return dayBudget;
    }

    public void setDayBudget(Integer dayBudget) {
        this.dayBudget = dayBudget;
    }

    public Integer getExhaustedDayBudget() {
        return exhaustedDayBudget;
    }

    protected void setExhaustedDayBudget(Integer exhaustedDayBudget) {
        this.exhaustedDayBudget = exhaustedDayBudget;
    }

    public String getAdSelection() {
        return adSelection;
    }

    public void setAdSelection(String adSelection) {
        this.adSelection = adSelection;
    }

    public DateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(Integer totalBudget) {
        this.totalBudget = totalBudget;
    }

    public Integer getExhaustedTotalBudget() {
        return exhaustedTotalBudget;
    }

    protected void setExhaustedTotalBudget(Integer exhaustedTotalBudget) {
        this.exhaustedTotalBudget = exhaustedTotalBudget;
    }

    public Integer getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(Integer totalClicks) {
        this.totalClicks = totalClicks;
    }

    public Integer getExhaustedTotalClicks() {
        return exhaustedTotalClicks;
    }

    protected void setExhaustedTotalClicks(Integer exhaustedTotalClicks) {
        this.exhaustedTotalClicks = exhaustedTotalClicks;
    }

    public Integer getPremiseId() {
        return premiseId;
    }

    public void setPremiseId(Integer premiseId) {
        this.premiseId = premiseId;
    }

   

   

    protected DateTime getStartDate() {
        return startDate;
    }

    protected DateTime getEndDate() {
        return endDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    

    

    
    
    
}
