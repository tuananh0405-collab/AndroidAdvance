package com.example.ojtaadaassignment04;

public class UserProfile {

    private String avatar;
    private String fullName;
    private String email;
    private String birthday;
    private String gender;

    public UserProfile(String avatar, String fullName, String email, String birthday, String gender) {
        this.avatar = avatar;
        this.fullName = fullName;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
