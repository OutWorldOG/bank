package ru.yaroslav.test.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.yaroslav.test.entities.UserCardEntity;

import java.util.Optional;

@Repository
public interface UserCardRep extends CrudRepository<UserCardEntity, String> {
    @Modifying
    @Query("update UserCardEntity uae set uae.money = ?2 where uae.cardNumber = ?1")
    void depositOrWithdraw(String account_number, Long money);

    @Query("select uae from UserCardEntity uae where uae.cardNumber=?1")
    Optional<UserCardEntity> findByCardNumber(String accountNumber);

}
