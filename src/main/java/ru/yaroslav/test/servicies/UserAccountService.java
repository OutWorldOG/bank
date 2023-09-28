package ru.yaroslav.test.servicies;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.entities.UserAccountEntity;
import ru.yaroslav.test.entities.UserBankEntity;
import ru.yaroslav.test.repositories.UserAccountRep;
import utilities.BankCardGenerator;

import java.util.UUID;

@Service
public class UserAccountService {

    private final UserAccountRep userAccountRep;

    public UserAccountService(UserAccountRep userAccountRep) {
        this.userAccountRep = userAccountRep;
    }

    @Transactional
    public UserAccountEntity saveNewUserAccount(String pin, UserBankEntity userBankEntity) {
        UserAccountEntity userAccountEntity = new UserAccountEntity();
        userAccountEntity.setUserId(userBankEntity);
        userAccountEntity.setAccount_id(UUID.randomUUID().toString());
        userAccountEntity.setPin(pin);
        userAccountEntity.setAccountNumber(BankCardGenerator.generateUniqueBankCardNumber());
        userAccountRep.save(userAccountEntity);
        return userAccountEntity;
    }

    @Transactional
    public void deleteUserBankAccount(Card card) {
        userAccountRep.deleteByAccount_numberAndPin(card.getCardNumber(), card.getPin());
    }

    @Transactional
    public String deposit(Card card) {
        UserAccountEntity userAccountEntity = userAccountRep.findByPinAndAccountId(card.getPin(), card.getCardNumber());
        Long OperationResult = card.getMoney() + userAccountEntity.getMoney();
        userAccountRep.depositOrWithdraw(card.getCardNumber(), OperationResult);
        return String.format("Вы занесли %s, текущий баланс: %s", card.getMoney(), OperationResult);
    }

    @Transactional
    public String withdraw(Card card) {
        UserAccountEntity userAccountEntity = userAccountRep.findByPinAndAccountId(card.getPin(), card.getCardNumber());
        Long OperationResult = userAccountEntity.getMoney() - card.getMoney();
        userAccountRep.depositOrWithdraw(card.getCardNumber(), OperationResult);
        return String.format("Вы сняли %s, текущий баланс: %s", card.getMoney(), OperationResult);
    }
}
