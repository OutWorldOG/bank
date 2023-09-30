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
    @Query("delete FROM UserCardEntity uae WHERE uae.cardNumber = ?1 and uae.pin=?2")
    void deleteByAccount_numberAndPin(String account_number, String pin);

    @Modifying
    @Query("update UserCardEntity uae set uae.money = ?2 where uae.cardNumber = ?1")
    void depositOrWithdraw(String account_number, Long money);

    @Query("select uae from UserCardEntity uae where uae.cardNumber=?1")
    Optional<UserCardEntity> findByAccountNumber(String accountNumber);

}
