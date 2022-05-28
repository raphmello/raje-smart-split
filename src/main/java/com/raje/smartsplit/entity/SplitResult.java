package com.raje.smartsplit.entity;

import com.raje.smartsplit.enums.EDebtType;
import lombok.*;

import javax.persistence.*;
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
