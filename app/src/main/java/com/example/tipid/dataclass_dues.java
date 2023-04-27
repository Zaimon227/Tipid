package com.example.tipid;

public class dataclass_dues {
    String name, cost, user_id, due_id;

    public dataclass_dues() {

    };

    public dataclass_dues(String name, String cost, String user_id, String due_id) {

        this.name = name;
        this.cost = cost;
        this.user_id = user_id;
        this.due_id = due_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDue_id() {
        return due_id;
    }

    public void setDue_id(String due_id) {
        this.due_id = due_id;
    }
}
