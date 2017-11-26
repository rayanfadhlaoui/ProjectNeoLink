package com.controler.account;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.controler.user.Generator;
import com.controler.utils.DateUtils;
import com.model.entities.Account;
import com.model.other.State;
import com.model.other.State.Status;

public class AccountManagementTest {

	private static final String ACCOUNT_NUMBER_1 = "0000000001";
	private static final String ACCOUNT_NUMBER_2 = "0000000002";
	private AccountManagement accountManagement = AccountManagement.getInstance();

	@Before
	public void setUp() {
		accountManagement.reset();
		resetGenerator();
	}

	@Test
	public void testAccountCreationOK() {
		String date = DateUtils.display(new Date());
		accountManagement.createAccount();
		accountManagement.createAccount();
		Account account = accountManagement.getAllAccounts().get(0);
		assertEquals(ACCOUNT_NUMBER_1, account.getAccountNumber());
		assertEquals(date, DateUtils.display(account.getcreationDate()));
		assertEquals(0, account.getBalance().intValue());

		Account account2 = accountManagement.getAllAccounts().get(1);
		assertEquals(ACCOUNT_NUMBER_2, account2.getAccountNumber());
		assertEquals(date, DateUtils.display(account2.getcreationDate()));
		assertEquals(0, account2.getBalance().intValue());
	}

	@Test
	public void testDeleteUser() {
		accountManagement.createAccount();
		State state = accountManagement.deleteAccount(ACCOUNT_NUMBER_1);

		assertEquals(Status.OK, state.getStatus());
		assertEquals(0, accountManagement.getAllAccounts().size());

		state = accountManagement.deleteAccount(ACCOUNT_NUMBER_1);
		assertEquals(Status.KO, state.getStatus());
		assertEquals("Account does not exist", state.getMessages().get(0));
	}

	@Test
	public void testFindAndDisplayUser() {
		accountManagement.createAccount();
		Account account = accountManagement.findAccount(ACCOUNT_NUMBER_1);
		String expectedAccountDisplay = "Account number: " + ACCOUNT_NUMBER_1 + "\n" + "Creation date : " + DateUtils.display(new Date()) + "\n" + "Balance : 0";
		assertEquals(expectedAccountDisplay, account.toString());
	}

	@Test
	public void testWisdrawAndDepositMoney() {
		State state = accountManagement.wisdrawMoney(ACCOUNT_NUMBER_1, 100);
		assertEquals(Status.KO, state.getStatus());
		assertEquals("Account does not exist", state.getMessages().get(0));
		
		state = accountManagement.depositMoney(ACCOUNT_NUMBER_1, 100);
		assertEquals(Status.KO, state.getStatus());
		assertEquals("Account does not exist", state.getMessages().get(0));

		accountManagement.createAccount();
		state = accountManagement.wisdrawMoney(ACCOUNT_NUMBER_1, 100);

		assertEquals(Status.KO, state.getStatus());
		assertEquals("The account did not have enough money to allow the wisdraw", state.getMessages().get(0));

		state = accountManagement.depositMoney(ACCOUNT_NUMBER_1, 100);
		
		assertEquals(100, accountManagement.findAccount(ACCOUNT_NUMBER_1).getBalance().intValue());

		assertEquals(Status.OK, state.getStatus());

		state = accountManagement.wisdrawMoney(ACCOUNT_NUMBER_1, 50);
		assertEquals(Status.OK, state.getStatus());
		assertEquals(50, accountManagement.findAccount(ACCOUNT_NUMBER_1).getBalance().intValue());

	}

	private void resetGenerator() {
		Generator generator = Generator.getInstance();
		Field field;
		try {
			field = Generator.class.getDeclaredField("accountNumber");
			field.setAccessible(true);
			field.set(generator, 1);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.getStackTrace();
		}
	}
}
