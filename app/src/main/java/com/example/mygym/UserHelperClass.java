package com.example.mygym;

public class UserHelperClass {
    String email, name, uri;

    public UserHelperClass(){

    }

    public UserHelperClass(String email, String name, String uri) {
        this.email = email;
        this.name = name;
        this.uri = uri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
