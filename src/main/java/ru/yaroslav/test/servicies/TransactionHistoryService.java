package ru.yaroslav.test.servicies;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yaroslav.test.entities.TransactionHistoryEntity;
import ru.yaroslav.test.repositories.TransactionalHistoryRep;

@Service
@Transactional
public class TransactionHistoryService {

    private final TransactionalHistoryRep transactionalHistoryRep;

    public TransactionHistoryService(TransactionalHistoryRep transactionalHistoryRep) {
        this.transactionalHistoryRep = transactionalHistoryRep;
    }

    public void save(TransactionHistoryEntity transactionHistoryEntity) {
        transactionalHistoryRep.save(transactionHistoryEntity);
    }
}
