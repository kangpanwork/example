package com.sanri.test.web.servlet3;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ConfigChangeEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ConfigChangeEvent(Object source,String groupId,String dataId) {
        super(source);
        this.groupId = groupId;
        this.dataId = dataId;
    }
    private String groupId;
    private String dataId;


}
