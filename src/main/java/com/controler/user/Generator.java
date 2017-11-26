package com.controler.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {
	private final static char[] ALPHABET = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	private static Generator INSTANCE;
	private Integer accountNumber;
	List<String> generatedLogin;
	private Random rand;

	public static Generator getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Generator();
		}
		return INSTANCE;
	}

	private Generator() {
		rand = new Random();
		generatedLogin = new ArrayList<>();
		accountNumber = 1;
	}

	public String generateLogin() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 2; i++) {
			sb.append(ALPHABET[rand(0, 25)]);
		}

		for (int i = 0; i < 8; i++) {
			sb.append(rand(0, 9));
		}
		String login = sb.toString();

		if (generatedLogin.contains(login)) {
			login = generateLogin();
		} else {
			generatedLogin.add(login);
		}

		return login;

	}

	private int rand(int min, int max) {

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		return rand.nextInt((max - min) + 1) + min;
	}

	public String generateAccountNumber() {
		String strigifiedAccountNumber = String.format("%010d", accountNumber);
		accountNumber++;
		return strigifiedAccountNumber;
	}
}
