package com.raje.smartsplit.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @OneToMany(mappedBy = "appUser")
    private List<Bill> bills;

    @ManyToMany
    private List<SplitExpensesGroup> splitExpensesGroups;
}
