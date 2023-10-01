package ru.yaroslav.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import ru.yaroslav.test.dto.Card;
import ru.yaroslav.test.dto.CardTransaction;
import ru.yaroslav.test.entities.UserCardEntity;
import ru.yaroslav.test.exceptions_handling.user_card_exception.IncorrectPinOrCardNumberException;
import ru.yaroslav.test.exceptions_handling.user_card_exception.IncorrectTransferCardsException;
import ru.yaroslav.test.exceptions_handling.user_card_exception.UserCardNotFoundException;
import ru.yaroslav.test.repositories.UserCardRep;
import ru.yaroslav.test.servicies.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserCardServiceImplUnitTest {
    private UserCardService userCardService;
    private UserCardRep userCardRep;
    private PasswordEncoder allMightyEncoder;

    @BeforeEach
    void setup() {
        userCardRep = mock(UserCardRep.class);
        TransactionHistoryServiceImpl transactionHistoryService = mock(TransactionHistoryServiceImpl.class);
        allMightyEncoder = mock(PasswordEncoder.class);
        userCardService = new UserCardServiceImpl(userCardRep, transactionHistoryService, allMightyEncoder, mock(UserCardServiceImpl.class));
    }

    @Test
    public void testDeleteUserAccountCardWithIncorrectData() {
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(IncorrectPinOrCardNumberException.class, () -> userCardService.deleteUserCard(new Card(), bindingResult));
    }

    @Test
    public void testDeleteUserAccountCardWithIncorrectPin() {
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        UserCardEntity userCardEntity = new UserCardEntity();
        userCardEntity.setMoney(100L);
        userCardEntity.setPin("1313");

        Card card = new Card();
        card.setCardNumber("13421321312");

        when(userCardRep.findByCardNumber("13421321312")).thenReturn(Optional.of(userCardEntity));

        String message = assertThrows(UserCardNotFoundException.class, () -> userCardService.deleteUserCard(card, bindingResult)).getMessage();
        assertTrue(message.contains("введён неверный pin"));
    }

    @Test
    public void testDeleteUserAccountCardWithNotFound() {
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        when(userCardRep.findByCardNumber("3213")).thenReturn(Optional.empty());

        String message = assertThrows(UserCardNotFoundException.class, () -> userCardService.deleteUserCard(new Card(), bindingResult)).getMessage();
        assertTrue(message.contains("карта не найдена"));
    }

    @Test
    public void testDeposit() {
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        UserCardEntity userCardEntity = new UserCardEntity();
        userCardEntity.setMoney(100L);
        userCardEntity.setPin("1313");

        CardTransaction cardTransaction = new CardTransaction();
        cardTransaction.setAmountOfMoney(200L);
        cardTransaction.setCardNumber("13421321312");

        when(userCardRep.findByCardNumber("13421321312")).thenReturn(Optional.of(userCardEntity));
        when(allMightyEncoder.matches(cardTransaction.getPin(), userCardEntity.getPin())).thenReturn(true);

        String result = userCardService.deposit(cardTransaction, bindingResult);

        assertTrue(result.contains("Вы занесли"));
    }

    @Test
    public void testWithdraw() {
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        UserCardEntity userCardEntity = new UserCardEntity();
        userCardEntity.setMoney(300L);
        userCardEntity.setPin("1313");

        CardTransaction cardTransaction = new CardTransaction();
        cardTransaction.setAmountOfMoney(200L);
        cardTransaction.setCardNumber("13421321312");

        when(userCardRep.findByCardNumber("13421321312")).thenReturn(Optional.of(userCardEntity));
        when(allMightyEncoder.matches(cardTransaction.getPin(), userCardEntity.getPin())).thenReturn(true);

        String result = userCardService.withdraw(cardTransaction, bindingResult);

        assertTrue(result.contains("Вы сняли"));
    }

    @Test
    public void testWithdrawWithNotEnoughMoney() {
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        UserCardEntity userCardEntity = new UserCardEntity();
        userCardEntity.setMoney(100L);
        userCardEntity.setPin("1313");

        CardTransaction cardTransaction = new CardTransaction();
        cardTransaction.setAmountOfMoney(200L);
        cardTransaction.setCardNumber("13421321312");

        when(userCardRep.findByCardNumber("13421321312")).thenReturn(Optional.of(userCardEntity));
        when(allMightyEncoder.matches(cardTransaction.getPin(), userCardEntity.getPin())).thenReturn(true);

        String result = assertThrows(UserCardNotFoundException.class, () -> userCardService.withdraw(cardTransaction, bindingResult)).getMessage();

        assertTrue(result.contains("Недостаточно средств"));
    }

    @Test
    public void testTransferError() {
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        CardTransaction[] cardTransactions = new CardTransaction[]{new CardTransaction(100L), new CardTransaction(200L)};
        assertThrows(IncorrectTransferCardsException.class, () -> userCardService.transfer(cardTransactions, bindingResult));
    }
}
