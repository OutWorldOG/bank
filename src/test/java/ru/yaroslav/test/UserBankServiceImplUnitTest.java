package ru.yaroslav.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import ru.yaroslav.test.dto.BankAccountRequest;
import ru.yaroslav.test.dto.UserBankAccount;
import ru.yaroslav.test.entities.UserBankAccountEntity;
import ru.yaroslav.test.entities.UserCardEntity;
import ru.yaroslav.test.exceptions_handling.user_bank_exception.IncorrectNameOrPinException;
import ru.yaroslav.test.exceptions_handling.user_bank_exception.UserBankNotFound;
import ru.yaroslav.test.repositories.UserBankAccountRep;
import ru.yaroslav.test.servicies.UserCardService;
import ru.yaroslav.test.servicies.UserBankService;
import ru.yaroslav.test.servicies.UserBankServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserBankServiceImplUnitTest {
    private UserBankService userBankService;
    private UserBankAccountRep userBankAccountRep;
    private UserCardService userCardService;

    @BeforeEach
    void setup() {
        userBankAccountRep = mock(UserBankAccountRep.class);
        userCardService = mock(UserCardService.class);
        userBankService = new UserBankServiceImpl(userBankAccountRep, userCardService);
    }

    @Test
    public void testSaveNewBankAccountWhenUserExists() {
        BankAccountRequest bankAccountRequest = new BankAccountRequest();
        bankAccountRequest.setName("John");
        bankAccountRequest.setPin("1234");
        BindingResult bindingResult = mock(BindingResult.class);

        UserBankAccountEntity existingUser = new UserBankAccountEntity();
        existingUser.setName("John");

        UserCardEntity userCardEntity = new UserCardEntity();
        userCardEntity.setCardNumber("213123123");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userBankAccountRep.findBankUserEntitiesByName("John")).thenReturn(Optional.of(existingUser));
        when(userCardService.saveNewUserCard(any(), any())).thenReturn(userCardEntity);

        String result = userBankService.saveNewUserBankAccount(bankAccountRequest, bindingResult);

        assertNotNull(result);
        assertTrue(result.contains("Номер вашей карты"));

        verify(userBankAccountRep, never()).save(any());
    }

    @Test
    public void testSaveNewBankAccountWhenUserDoesNotExist() {
        BankAccountRequest bankAccountRequest = new BankAccountRequest();
        bankAccountRequest.setName("John");
        bankAccountRequest.setPin("1234");
        BindingResult bindingResult = mock(BindingResult.class);

        UserBankAccountEntity existingUser = new UserBankAccountEntity();
        existingUser.setName("John");

        UserCardEntity userCardEntity = new UserCardEntity();
        userCardEntity.setCardNumber("213123123");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userBankAccountRep.findBankUserEntitiesByName("John")).thenReturn(Optional.empty());
        when(userCardService.saveNewUserCard(any(), any())).thenReturn(userCardEntity);

        String result = userBankService.saveNewUserBankAccount(bankAccountRequest, bindingResult);

        assertNotNull(result);
        assertTrue(result.contains("Номер вашей карты"));

        verify(userBankAccountRep, times(1)).save(any());
        verify(userCardService, times(1)).saveNewUserCard(any(), any());
    }

    @Test
    public void testSaveNewBankAccountWithErrors() {
        BankAccountRequest bankAccountRequest = new BankAccountRequest();
        bankAccountRequest.setName("InvalidName");
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(IncorrectNameOrPinException.class, () -> userBankService.saveNewUserBankAccount(bankAccountRequest, bindingResult));

        verify(userBankAccountRep, never()).save(any());
        verify(userCardService, never()).saveNewUserCard(any(), any());
    }

    @Test
    public void testDeleteBankAccountWithError() {
        UserBankAccount bankAccount = new UserBankAccount();
        bankAccount.setName("InvalidName");
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userBankAccountRep.findBankUserEntitiesByName("John")).thenReturn(Optional.empty());

        assertThrows(UserBankNotFound.class, () -> userBankService.deleteUserBankAccount(bankAccount, bindingResult));
    }

    @Test
    public void testGetUserBankAccount() {
        UserBankAccount bankAccountRequest = new UserBankAccount();
        bankAccountRequest.setName("John");
        BindingResult bindingResult = mock(BindingResult.class);

        UserBankAccountEntity existingUser = new UserBankAccountEntity();
        existingUser.setName("John");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userBankAccountRep.findBankUserEntitiesByName("John")).thenReturn(Optional.of(existingUser));

        UserBankAccount userBankAccount = userBankService.getUserBankAccount(bankAccountRequest, bindingResult);

        assertNotNull(userBankAccount);
    }

    @Test
    public void testGetUserBankAccountWithError() {
        UserBankAccount bankAccountRequest = new UserBankAccount();
        bankAccountRequest.setName("InvalidName");
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userBankAccountRep.findBankUserEntitiesByName("John")).thenReturn(Optional.empty());

        assertThrows(UserBankNotFound.class, () -> userBankService.getUserBankAccount(bankAccountRequest, bindingResult));
    }
}

