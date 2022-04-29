package com.raje.smartsplit.entity;

import com.raje.smartsplit.enums.CategoryEnum;
import com.raje.smartsplit.enums.DebtTypeEnum;
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

    @Enumerated(EnumType.STRING)
    private CategoryEnum category;

    @ManyToOne
    private SplitExpensesGroup splitExpensesGroup;

    @ManyToOne
    private AppUser splitParticipant;

    @NotNull
    private Double amount;

    @Enumerated(EnumType.STRING)
    private DebtTypeEnum debtType;


}
