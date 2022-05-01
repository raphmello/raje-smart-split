package com.raje.smartsplit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private SplitExpensesGroup splitExpensesGroup;

    @ManyToOne
    private User user;

    private Double splitShare = 1.;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;

        Participant that = (Participant) o;

        return Objects.equals(user.getUsername(), that.user.getUsername());
    }

    @Override
    public int hashCode() {
        int result = splitExpensesGroup != null ? splitExpensesGroup.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
