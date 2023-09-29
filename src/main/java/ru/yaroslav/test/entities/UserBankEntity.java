package ru.yaroslav.test.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "bank_user")
public class UserBankEntity {

    @Id
    private String user_id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAccountEntity> userAccounts;

    @Override
    public String toString() {
        return "";
    }
}
