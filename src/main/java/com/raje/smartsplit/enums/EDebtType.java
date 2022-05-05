package com.raje.smartsplit.enums;

public enum EDebtType {
    DEBTOR("deve pagar"),
    CREDITOR("deve receber");

    private String description;

    EDebtType(String description) {
        this.description = description;
    }
}
