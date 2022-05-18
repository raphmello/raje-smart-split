package com.raje.smartsplit.entity;

import com.raje.smartsplit.exception.UserAlreadyParticipantException;
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
public class SplitGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private final LocalDate creationDate = LocalDate.now();

    @ManyToOne
    private User creator;

    @OneToMany(mappedBy = "splitGroup",fetch = FetchType.EAGER)
    private List<Participant> participants = new ArrayList<>();

    public void addParticipant(Participant participant) {
        Optional<Participant> first = this.participants.stream()
                .filter(p -> p.equals(participant))
                .findFirst();
        if(first.isPresent())
            throw new UserAlreadyParticipantException();

        this.participants.add(participant);
    }

    public void removeParticipant(User user) {
        Participant participant = null;
        for (Participant p: participants) {
            if(p.getUser().equals(user))
                participant = p;
        }
        participants.remove(participant);
    }
}
