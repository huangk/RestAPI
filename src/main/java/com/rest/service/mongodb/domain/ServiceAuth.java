package com.rest.service.mongodb.domain;

public class ServiceAuth {
    String key;
    String user;
    
    public ServiceAuth(String key, String user) {
        this.key = key;
        this.user = user;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void setServiceKey(String key) {
        this.key = key;
    }
    
    public String getUser() {
        return this.user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
}
