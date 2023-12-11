package com.unipi.msc.raiseupandroid.Model;

import com.google.gson.JsonObject;
import com.unipi.msc.raiseupandroid.Enum.Role;

public class User {
    private Long id;
    private String email;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;
    private String profileURL;
    private int percentage;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String profileURL, int percentage) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileURL = profileURL;
        this.percentage = percentage;
    }

    public User(Long id, String email, String username, String password, String firstName, String lastName, Role role, String profileURL, int percentage) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.profileURL = profileURL;
        this.percentage = percentage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }
    public String getFullName(){
        return firstName + " " + lastName;
    }

    public static User buildFromJSON(JsonObject body) {
        User u = new User();
        u.setProfileURL("");
        try {
            u.setId(body.get("id").getAsLong());
        }catch (Exception ignore){}

        try {
            u.setEmail(body.get("email").getAsString());
        }catch (Exception ignore){}

        try {
            u.setUsername(body.get("username").getAsString());
        }catch (Exception ignore){}

        try {
            u.setFirstName(body.get("firstName").getAsString());
        }catch (Exception ignore){}

        try {
            u.setLastName(body.get("lastName").getAsString());
        }catch (Exception ignore){}

        try {
            u.setRole(Role.valueOf(body.get("role").getAsString()));
        }catch (Exception ignore){}

        return u;
    }

    public static String getTokenFromJSON(JsonObject body) {
        try {
            return body.get("token").getAsString();
        }catch (Exception ignore){}
        return null;
    }
}
