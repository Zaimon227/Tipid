package com.example.tipid;

public class dataclass_fixeddues {
    String name, cost, user_id, fixeddue_id;

    public dataclass_fixeddues() {

    };

    public dataclass_fixeddues(String name, String cost, String user_id, String fixeddue_id) {

        this.name = name;
        this.cost = cost;
        this.user_id = user_id;
        this.fixeddue_id = fixeddue_id;
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

    public String getFixeddue_id() {
        return fixeddue_id;
    }

    public void setFixeddue_id(String due_id) {
        this.fixeddue_id = fixeddue_id;
    }
}
