package com.beat.restaurantapp.models;

public class UserDetails {

    public String email;
    public String name;
    public String address;
    public int age;

    public UserDetails() {
        email = "";
        name = "";
        address = "";
        age = 0;
    }

    public UserDetails(String email, String name, String address, int age) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
