package ru.yaroslav.test.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.yaroslav.test.entities.TransactionHistoryEntity;

@Repository
public interface TransactionalHistoryRep extends CrudRepository<TransactionHistoryEntity, String> {
}
