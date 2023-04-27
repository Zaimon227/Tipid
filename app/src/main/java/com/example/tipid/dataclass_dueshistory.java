package com.example.tipid;

public class dataclass_dueshistory {
    String name, cost, dhdate;

    public dataclass_dueshistory(){

    };

    public dataclass_dueshistory(String name, String cost, String dhdate) {

        this.name = name;
        this.cost = cost;
        this.dhdate = dhdate;
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

    public String getDhdate() {
        return dhdate;
    }

    public void setDhdate(String dhdate) {
        this.dhdate = dhdate;
    }
}