package com.edischool.pojo;

import java.util.List;

public class FinanceListWrapper {
    private String studentId;
    private List<Finance> transactions;

    public FinanceListWrapper(){

    }
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<Finance> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Finance> transactions) {
        this.transactions = transactions;
    }
}
