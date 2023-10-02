package ru.yaroslav.test.entities;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_card")
public class UserCardEntity {

    @Id
    private String accountId;

    @Column(name = "pin")
    private String pin;

    @Column(name = "account_number")
    private String cardNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserBankAccountEntity userId;

    @Column(name = "money")
    private Long money;

    @OneToMany(mappedBy = "cardId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionHistoryEntity> transactionHistoryEntities;

    @PrePersist
    public void setDefaults() {
        if (money == null) {
            money = 0L;
        }
    }

    @Override
    public String toString() {
        return "";
    }
}
