package ru.yaroslav.test.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JoinColumn(name = "card_id")
    @JsonIgnore
    private UserCardEntity cardId;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "operation_type")
    private String operationType;

    @Column(name = "amount")
    private Long amount;

}
