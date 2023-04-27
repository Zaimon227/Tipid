package com.example.tipid;

public class dataclass_expenses {
    String name, category, cost, expense_id, user_id;

    public dataclass_expenses(){

    };

    public dataclass_expenses(String name, String category, String cost, String expense_id, String user_id) {

        this.name = name;
        this.category = category;
        this.cost = cost;
        this.expense_id = expense_id;
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(String expense_id) {
        this.expense_id = expense_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.name = user_id;
    }
}
