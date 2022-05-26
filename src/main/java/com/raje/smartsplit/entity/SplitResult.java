package com.raje.smartsplit.entity;

import com.raje.smartsplit.enums.EDebtType;
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
public class SplitResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NotNull
    private BillCategory category;

    @ManyToOne
    @NotNull
    private SplitGroup splitGroup;

    @ManyToOne
    @NotNull
    private Participant participant;

    @NotNull
    private Double amount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private EDebtType debtType;


}
