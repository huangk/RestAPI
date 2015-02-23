package com.rest.service.mongodb;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.rest.service.mongodb.domain.ServiceAuth;
import com.rest.service.mongodb.domain.User;
import static org.springframework.data.mongodb.core.mapreduce.GroupBy.keyFunction;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/** * Repository for {
@link User}
s */ 


public class UserRepository {
    static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    MongoTemplate mongoTemplate;
    
    public UserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    public void createUserCollection() {
        if (!mongoTemplate.collectionExists(User.class)) {
            mongoTemplate.createCollection(User.class);
        }
        if (!mongoTemplate.collectionExists(ServiceAuth.class)) {
            mongoTemplate.createCollection(ServiceAuth.class);
        }

    }
    
    public void dropUserCollection() {
        if (mongoTemplate.collectionExists(User.class)) {
            mongoTemplate.dropCollection(User.class);
        }
        if (mongoTemplate.collectionExists(ServiceAuth.class)) {
            mongoTemplate.dropCollection(ServiceAuth.class);
        }
    }
    
    public Boolean getMongoStatus() {       
        Boolean isAlive = true;
        try {
            String dbName = mongoTemplate.getDb().getName();
            logger.info("dbName: " + dbName);
        }
        catch (Throwable t) {
            logger.error(t.getMessage());
            isAlive = false;
        }
        return isAlive;
    }
    
    public List<User> getUsers(String field, String value, String group) {
        
        Query query = new Query();
        query.addCriteria(Criteria.where(field).is(value)); 
        query.with(new Sort(Sort.Direction.ASC,group));
        
        return mongoTemplate.find(query, User.class);
    }
    
    public User getUser(String user, String password) {
            
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(user).and("password").is(password));
   
        return mongoTemplate.findOne(query, User.class);
    }
    
    public ServiceAuth getServiceAuth(String user, String serviceKey) {
        Query query = new Query();
        query.addCriteria(Criteria.where("user").is(user).and("key").is(serviceKey));
        
        return mongoTemplate.findOne(query, ServiceAuth.class);
    }
    
    public ServiceAuth getServiceAuth(String serviceKey) {
        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(serviceKey));
        
        return mongoTemplate.findOne(query, ServiceAuth.class);
    }
    
    public void createUser(User user) {
        mongoTemplate.insert(user);
        String key = UUID.randomUUID().toString(); 
        ServiceAuth serviceAuth = new ServiceAuth(key,user.getName());       
        mongoTemplate.insert(serviceAuth);
    }

}
