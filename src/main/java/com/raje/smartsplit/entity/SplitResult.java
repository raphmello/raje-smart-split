package com.raje.smartsplit.entity;

import com.raje.smartsplit.enums.EDebtType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SplitResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
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
