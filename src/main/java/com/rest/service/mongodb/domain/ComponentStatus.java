package com.rest.service.mongodb.domain;

public class ComponentStatus {
    String component;
    String status;
    
    public ComponentStatus(String component, String status) {
        this.component=component;
        this.status = status;
    }
    public String getComponent() {
        return this.component;
    }
    
    public void setComponent(String component) {
        this.component = component;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
