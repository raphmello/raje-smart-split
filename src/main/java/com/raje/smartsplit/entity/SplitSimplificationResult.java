package com.raje.smartsplit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SplitSimplificationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private SplitExpensesGroup splitExpensesGroup;

    @ManyToOne
    private AppUser debtor;

    @ManyToOne
    private AppUser creditor;

    @NotNull
    private Double amount;
}
