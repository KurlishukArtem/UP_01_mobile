package com.example.wsr_pool;

public class Users {
    public Integer id;
    public String fullName;
    public String password;
    public String email;
    public String phone;
    public String token;
    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return this.id;
    }
    public void setLogin(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }
    public void setFullName(String fullName){
        this.fullName = fullName;
    }
    public String getFullName(){
        return this.fullName;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhone(){
        return this.phone;
    }
    public void setToken(String token){
        this.token = token;
    }
    public String getToken(){
        return this.token;
    }
}
