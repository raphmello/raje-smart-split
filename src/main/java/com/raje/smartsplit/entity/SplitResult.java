package com.raje.smartsplit.entity;

import com.raje.smartsplit.enums.ECategory;
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

    @Enumerated(EnumType.STRING)
    private ECategory category;

    @ManyToOne
    private SplitGroup splitGroup;

    @ManyToOne
    private User splitParticipant;

    @NotNull
    private Double amount;

    @Enumerated(EnumType.STRING)
    private EDebtType debtType;


}
