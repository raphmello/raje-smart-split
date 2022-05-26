package com.raje.smartsplit.enums;

import lombok.Getter;

@Getter
public enum EDebtType {
    DEBTOR("deve pagar"),
    CREDITOR("deve receber");

    private String description;

    EDebtType(String description) {
        this.description = description;
    }

    public static EDebtType getDebtType(Double valueBalance) {
        if (valueBalance < 0) {
            return DEBTOR;
        } else if (valueBalance > 0) {
            return CREDITOR;
        }
        return null;
    }
}
