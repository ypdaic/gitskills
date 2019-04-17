package com.daiyanping.cms.scheduledTask;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @ClassName SessionOverviewEvent
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-04-17
 * @Version 0.1
 */
public class SessionOverviewEvent extends ApplicationEvent {

    private SessionOverview sessionOverview;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public SessionOverviewEvent(Object source, SessionOverview sessionOverview) {
        super(source);
        this.sessionOverview = sessionOverview;
    }

    public SessionOverview getSessionOverview() {
        return sessionOverview;
    }

}
