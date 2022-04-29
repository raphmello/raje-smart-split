package com.raje.smartsplit.enums;

public enum DebtTypeEnum {
    DEBTOR("deve pagar"),
    CREDITOR("deve receber");

    private String description;

    DebtTypeEnum(String description) {
        this.description = description;
    }
}
