package com.raje.smartsplit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

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
    private SplitGroup splitGroup;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private List<Bill> bills = new ArrayList<>();

    private Double splitShare = 1.;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<BillCategory> billCategories = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;

        Participant that = (Participant) o;

        return Objects.equals(user.getUsername(), that.user.getUsername());
    }

    @Override
    public int hashCode() {
        int result = splitGroup != null ? splitGroup.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    public void addBill(Bill bill) {
        this.bills.add(bill);
    }

    public void addCategory(BillCategory billCategory) {
        this.billCategories.add(billCategory);
    }

    public Double getTotalSpentByCategory(BillCategory category) {
        Double total = 0.;
        for (Bill bill : this.getBills()) {
            if (bill.getCategory().equals(category)) {
                total += bill.getAmount();
            }
        }
        return total;
    }

    public List<Double> getTotalByCategory(BillCategory category) {
        List<Double> amountList = new ArrayList<>();
        for (Bill bill : this.getBills()) {
            if(bill.getCategory().equals(category)) {
                final Double billAmount = bill.getAmount();
                amountList.add(billAmount);
            }
        }
        return amountList;
    }
}
