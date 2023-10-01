package ru.yaroslav.test.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.yaroslav.test.entities.UserBankAccountEntity;

import java.util.Optional;

@Repository
public interface UserBankAccountRep extends CrudRepository<UserBankAccountEntity, String> {

    Optional<UserBankAccountEntity> findBankUserEntitiesByName(String name);

}
