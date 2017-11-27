package com.rayanfadhlaoui.domain.services.account;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.rayanfadhlaoui.domain.model.entities.Account;
import com.rayanfadhlaoui.domain.model.entities.User;
import com.rayanfadhlaoui.domain.model.other.State;
import com.rayanfadhlaoui.domain.model.other.State.Status;
import com.rayanfadhlaoui.domain.services.account.AccountManagement;
import com.rayanfadhlaoui.domain.services.account.InMemoryAccountRepository;
import com.rayanfadhlaoui.domain.services.user.InMemoryUserRepository;
import com.rayanfadhlaoui.domain.services.user.UserManagement;
import com.rayanfadhlaoui.domain.services.utils.Generator;

public class AccountManagementTest {

	private static final String ACCOUNT_NUMBER_1 = "0000000001";
	private static final String ACCOUNT_NUMBER_2 = "0000000002";
	private static final String ACCOUNT_NUMBER_3 = "0000000003";
	private static final String ACCOUNT_NUMBER_4 = "0000000004";
	private static final String ACCOUNT_NUMBER_5 = "0000000005";
	
	final DateTimeFormatter MY_PATTERN = DateTimeFormatter.ofPattern("dd/MM/yyyy");	
	
	private AccountManagement accountManagement;
	private UserManagement userManagement;

	@Before
	public void setUp() {
		Generator generator = resetGenerator();
		userManagement = new UserManagement(new InMemoryUserRepository(), generator);
		accountManagement = new AccountManagement(userManagement, generator, new InMemoryAccountRepository());
	}

	@Test
	public void testAccountCreationOK() {
		String date = LocalDate.now().format(MY_PATTERN);
		accountManagement.createAccount();
		accountManagement.createAccount();
		Account account = accountManagement.getAllAccounts().get(0);
		assertEquals(ACCOUNT_NUMBER_1, account.getAccountNumber());
		assertEquals(date, account.getcreationDate().format(MY_PATTERN));
		assertEquals(0, account.getBalance().intValue());

		Account account2 = accountManagement.getAllAccounts().get(1);
		assertEquals(ACCOUNT_NUMBER_2, account2.getAccountNumber());
		assertEquals(date, account2.getcreationDate().format(MY_PATTERN));
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
		String expectedAccountDisplay = "Account number: " + ACCOUNT_NUMBER_1 + "\n" + "Creation date : " + LocalDate.now().format(MY_PATTERN) + "\n" + "Balance : 0";
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

		state = accountManagement.depositMoney(ACCOUNT_NUMBER_1, -100);
		assertEquals(Status.KO, state.getStatus());
		assertEquals("Impossible to deposit a negative amount", state.getMessages().get(0));
		
		state = accountManagement.depositMoney(ACCOUNT_NUMBER_1, 100);

		assertEquals(Status.OK, state.getStatus());
		assertEquals(100, accountManagement.findAccount(ACCOUNT_NUMBER_1).getBalance().intValue());

		assertEquals(Status.OK, state.getStatus());

		state = accountManagement.wisdrawMoney(ACCOUNT_NUMBER_1, 50);
		assertEquals(Status.OK, state.getStatus());
		assertEquals(50, accountManagement.findAccount(ACCOUNT_NUMBER_1).getBalance().intValue());

	}

	@Test
	public void testLinkAccountToUser() {
		State state = accountManagement.linkAccountToUser(ACCOUNT_NUMBER_1, "");
		assertEquals(Status.KO, state.getStatus());
		assertEquals("Missing user", state.getMessages().get(0));
		assertEquals("Missing account", state.getMessages().get(1));

		accountManagement.createAccount();

		User rayanUser = createUserRayan();
		assertEquals(0, rayanUser.getAccounts().size());

		state = accountManagement.linkAccountToUser(ACCOUNT_NUMBER_1, rayanUser.getLogin());
		assertEquals(Status.OK, state.getStatus());
		assertEquals(1, rayanUser.getAccounts().size());
		assertEquals(ACCOUNT_NUMBER_1, rayanUser.getAccounts().get(0).getAccountNumber());

	}

	/** Test case 1: Right after linking an account to a user, we try to delete it. A error should be raised.
	/** Test case 2: We dissociate the account from the user then we try to delete it again, should work.
	 * */
	@Test
	public void testDissociateAccountLinkedToAUser() {
		accountManagement.createAccount();
		User rayanUser = createUserRayan();
		accountManagement.linkAccountToUser(ACCOUNT_NUMBER_1, rayanUser.getLogin());

		State state = accountManagement.deleteAccount(ACCOUNT_NUMBER_1);
		assertEquals(Status.KO, state.getStatus());
		assertEquals("Impossible to delete accounts link to a user", state.getMessages().get(0));
		
		accountManagement.dissociateAccountFromUser(ACCOUNT_NUMBER_1, rayanUser.getLogin());
		assertEquals(0, rayanUser.getAccounts().size());
		state = accountManagement.deleteAccount(ACCOUNT_NUMBER_1);
		assertEquals(Status.OK, state.getStatus());

	}
	
	@Test
	public void testFindAllAccountsLinkedToAUser() {
		accountManagement.createAccount();
		accountManagement.createAccount();
		accountManagement.createAccount();
		accountManagement.createAccount();
		accountManagement.createAccount();
		
		List<Account> accountList = userManagement.getAllAccountsAssociatedToUser("");
		assertEquals(null, accountList);

		
		User rayanUser = createUserRayan();
		String login = rayanUser.getLogin();
		
		accountList = userManagement.getAllAccountsAssociatedToUser(login);
		assertEquals(0, accountList.size());
		
		accountManagement.linkAccountToUser(ACCOUNT_NUMBER_1, login);
		accountManagement.linkAccountToUser(ACCOUNT_NUMBER_2, login);
		accountManagement.linkAccountToUser(ACCOUNT_NUMBER_3, login);
		accountManagement.linkAccountToUser(ACCOUNT_NUMBER_4, login);
		accountManagement.linkAccountToUser(ACCOUNT_NUMBER_5, login);

		accountList = userManagement.getAllAccountsAssociatedToUser(login);
		assertEquals(5, accountList.size());
		
	}
	
	@Test
	public void testFindWealth() {
		accountManagement.createAccount();
		accountManagement.createAccount();
		accountManagement.createAccount();
		accountManagement.createAccount();
		accountManagement.createAccount();
				
		User rayanUser = createUserRayan();
		String login = rayanUser.getLogin();
		
		accountManagement.linkAccountToUser(ACCOUNT_NUMBER_1, login);
		accountManagement.linkAccountToUser(ACCOUNT_NUMBER_2, login);
		accountManagement.linkAccountToUser(ACCOUNT_NUMBER_3, login);
		accountManagement.linkAccountToUser(ACCOUNT_NUMBER_4, login);
		accountManagement.linkAccountToUser(ACCOUNT_NUMBER_5, login);

		accountManagement.depositMoney(ACCOUNT_NUMBER_1, 150);
		accountManagement.depositMoney(ACCOUNT_NUMBER_1, 150);
		accountManagement.depositMoney(ACCOUNT_NUMBER_2, 300);
		accountManagement.depositMoney(ACCOUNT_NUMBER_3, 600);
		accountManagement.depositMoney(ACCOUNT_NUMBER_2, 100);
		accountManagement.depositMoney(ACCOUNT_NUMBER_5, 150);
		accountManagement.depositMoney(ACCOUNT_NUMBER_4, 50);
		
		int wealth = userManagement.getUserWealth(login);
		assertEquals(1500, wealth);
		
	}
		
	private User createUserRayan() {
		userManagement.createUser("Rayan", "Fadhlaoui", LocalDate.parse("19/09/1989", MY_PATTERN), "16 B Avenue Albert 1ER 94210", "0664197893");
		User user = userManagement.getAllUsers().get(0);
		return user;
	}

	private Generator resetGenerator() {
		Generator generator = Generator.getInstance();
		Field field;
		try {
			field = Generator.class.getDeclaredField("accountNumber");
			field.setAccessible(true);
			field.set(generator, 1);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.getStackTrace();
		}
		
		return generator;
	}
}
