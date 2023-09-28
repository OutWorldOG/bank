package ru.yaroslav.test.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.yaroslav.test.entities.UserBankEntity;

import java.util.Optional;

@Repository
public interface UserBankRep extends CrudRepository<UserBankEntity, String> {

    Optional<UserBankEntity> findBankUserEntitiesByName(String name);

}
