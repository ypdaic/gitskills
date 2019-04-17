package com.daiyanping.cms.scheduledTask;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName SessionOverview
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-17
 * @Version 0.1
 */
public class SessionOverview {

    private Integer sessionTotal;

    private Integer quitQueueCount;

    private Integer allocatedSeatsCount;

    private Integer leaveSessionCount;

    private Integer channel;

    private Integer skillGroups;

    private Integer leaveMessageCount;

    private String skillGroupsName;

    public Integer getSessionTotal() {
        return sessionTotal == null ? 0 : sessionTotal;
    }

    public void setSessionTotal(Integer sessionTotal) {
        this.sessionTotal = sessionTotal == null ? 0 : sessionTotal;
    }

    public Integer getQuitQueueCount() {
        return quitQueueCount == null ? 0 : quitQueueCount;
    }

    public void setQuitQueueCount(Integer quitQueueCount) {
        this.quitQueueCount = quitQueueCount == null ? 0 : quitQueueCount;
    }

    public Integer getAllocatedSeatsCount() {
        return allocatedSeatsCount == null ? 0 : allocatedSeatsCount;
    }

    public void setAllocatedSeatsCount(Integer allocatedSeatsCount) {
        this.allocatedSeatsCount = allocatedSeatsCount == null ? 0 : allocatedSeatsCount;
    }

    public Integer getLeaveSessionCount() {
        return leaveSessionCount == null ? 0 : leaveSessionCount;
    }

    public void setLeaveSessionCount(Integer leaveSessionCount) {
        this.leaveSessionCount = leaveSessionCount == null ? 0 : leaveSessionCount;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getSkillGroups() {
        return skillGroups;
    }

    public void setSkillGroups(Integer skillGroups) {
        this.skillGroups = skillGroups;
    }

    public Integer getLeaveMessageCount() {
        return leaveMessageCount == null ? 0 : leaveMessageCount;
    }

    public void setLeaveMessageCount(Integer leaveMessageCount) {
        this.leaveMessageCount = leaveMessageCount == null ? 0: leaveMessageCount;
    }

    public String getSkillGroupsName() {
        return skillGroupsName;
    }

    public void setSkillGroupsName(String skillGroupsName) {
        this.skillGroupsName = skillGroupsName;
    }
}
