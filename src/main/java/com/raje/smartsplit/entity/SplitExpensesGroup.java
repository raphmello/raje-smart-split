package com.raje.smartsplit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SplitExpensesGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private LocalDate creationDate;

    @ManyToOne
    private AppUser creator;

    @OneToMany(mappedBy = "splitExpensesGroup")
    private List<Participant> participants;

    @OneToMany(mappedBy = "splitExpensesGroup")
    private List<Bill> bills;
}