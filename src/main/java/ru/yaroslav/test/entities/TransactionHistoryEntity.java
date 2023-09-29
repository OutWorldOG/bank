package ru.yaroslav.test.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "transaction_history")
public class TransactionHistoryEntity {

    @Id
    private String historyId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private UserAccountEntity accountId;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "operation_type")
    private String operationType;

    @Column(name = "amount")
    private Long amount;


}
