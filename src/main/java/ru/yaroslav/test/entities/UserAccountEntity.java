package ru.yaroslav.test.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_account")
public class UserAccountEntity {

    @Id
    private String account_id;

    @Column(name = "pin")
    private String pin;

    @Column(name = "account_number")
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserBankEntity userId;

    @Column(name = "money")
    private Long money;

    @PrePersist
    public void setDefaults() {
        if (money == null) {
            money = 0L;
        }
    }

    @Override
    public String toString() {
        return "UserAccountEntity{" +
                "account_id='" + account_id + '\'' +
                ", pin='" + pin + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", userId=" + userId +
                ", money=" + money +
                '}';
    }
}
