package com.example.tipid;

public class dataclass_history {
    String name, cost, hdate;

    public dataclass_history(){

    };

    public dataclass_history(String name, String cost, String hdate) {

        this.name = name;
        this.cost = cost;
        this.hdate = hdate;
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
        this.cost= cost;
    }

    public String getHdate() {
        return hdate;
    }

    public void setHdate(String hdate) {
        this.hdate = hdate;
    }
}
