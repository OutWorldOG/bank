package ru.yaroslav.test.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.yaroslav.test.entities.UserAccountEntity;

import java.util.Optional;

@Repository
public interface UserAccountRep extends CrudRepository<UserAccountEntity, String> {

    @Modifying
    @Query("delete FROM UserAccountEntity uae WHERE uae.accountNumber = ?1 and uae.pin=?2")
    void deleteByAccount_numberAndPin(String account_number, String pin);

    @Modifying
    @Query("update UserAccountEntity uae set uae.money = ?2 where uae.accountNumber = ?1")
    void depositOrWithdraw(String account_number, Long money);

    @Query("select uae from UserAccountEntity uae where uae.accountNumber=?1")
    Optional<UserAccountEntity> findByAccountNumber(String accountNumber);

}
