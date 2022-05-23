package com.raje.smartsplit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String description;

    @NotNull
    @ManyToOne
    private BillCategory category;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Participant participant;

    @NotNull
    private Double amount;
}
