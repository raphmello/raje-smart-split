package com.raje.smartsplit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final LocalDate creationDate = LocalDate.now();

    @ManyToOne
    private User creator;

    @OneToMany(mappedBy = "splitExpensesGroup",fetch = FetchType.EAGER)
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "splitExpensesGroup")
    private List<Bill> bills = new ArrayList<>();

    public void addParticipant(Participant participant) {
        Optional<Participant> first = this.participants.stream()
                .filter(p -> p.getUser().getUsername().equals(participant.getUser().getUsername()))
                .findFirst();
        if(first.isPresent())
            throw new RuntimeException("User is already in this group.");

        this.participants.add(participant);
    }

    public void addBill(Bill bill) {
        this.bills.add(bill);
    }
}
