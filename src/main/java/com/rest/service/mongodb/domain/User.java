package com.rest.service.mongodb.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** * A simple POJO representing a Person **/
@Document
public class User {
    @Id
    private String personId;

    private String name;

    private String password;
    
    private String city;

    private int age;
    
    private String profession;
    
    public User(String name, String password, String city, int age, String profession) {
        this.password = password;
        this.name = name;
        this.age = age;
        this.city = city;
        this.profession = profession;
    }
    
    public String getPersonId() {
        return personId;
    }
    
    public void setPersonId(final String personId) {
        this.personId = personId;
    }
    
    public String getName() {
        return name;
    }
 
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(final int age) {
        this.age = age;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(final String profession) {
        this.profession = profession;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
    
    @Override 
    public String toString() {
        return "Person [id=" + personId + ", name=" + name + ", age=" + age + ", home town=" + city + "]";
    }


 }
