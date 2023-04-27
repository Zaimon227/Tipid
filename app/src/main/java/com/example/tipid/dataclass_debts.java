package com.example.tipid;

public class dataclass_debts {
    String name, paid_amount, total_debt, user_id, debt_id;

    public dataclass_debts() {

    };

    public dataclass_debts(String name, String paid_amount, String total_debt, String user_id, String debt_id) {

        this.name = name;
        this.paid_amount = paid_amount;
        this.total_debt = total_debt;
        this.user_id = user_id;
        this.debt_id = debt_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }

    public String getTotal_debt() {
        return total_debt;
    }

    public void setTotal_debt(String total_debt) {
        this.total_debt = total_debt;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.name = user_id;
    }

    public String getDebt_id() {
        return debt_id;
    }

    public void setDebt_id(String debt_id) {
        this.name = debt_id;
    }

}
