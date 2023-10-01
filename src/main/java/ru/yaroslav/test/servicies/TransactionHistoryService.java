package ru.yaroslav.test.servicies;

import ru.yaroslav.test.entities.TransactionHistoryEntity;

public interface TransactionHistoryService {
    void save(TransactionHistoryEntity transactionHistoryEntity);
}
