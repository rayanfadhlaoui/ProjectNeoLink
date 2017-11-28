package com.rayanfadhlaoui.domain.services.utils;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Random;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class GeneratorTest {

	@Test
	public void testLoginGeneration() {
		Generator generator = Generator.getInstance();
		int[] randomGeneration = { 4, 20, 5, 5, 6, 12, 5, 9, 0, 5 };
		mockRandomFromGenerator(generator, randomGeneration);

		String generateLogin = generator.generateLogin();

		assertEquals("EU556125905", generateLogin);
	}

	private void mockRandomFromGenerator(Generator generator, int[] randomGeneration) {
		Random random = Mockito.mock(Random.class);

		Answer<Integer> answer = new Answer<Integer>() {
			public int nbCall = 0;
			private int[] valueList = randomGeneration;

			public Integer answer(InvocationOnMock invocation) throws Throwable {
				if (nbCall > valueList.length - 1) {
					throw new Exception("Scope of value too weak");
				}
				int value = valueList[nbCall];
				nbCall++;
				return value;
			}
		};

		Mockito.when(random.nextInt(Mockito.anyInt())).then(answer);

		Field field;
		try {
			field = Generator.class.getDeclaredField("rand");
			field.setAccessible(true);
			field.set(generator, random);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.getStackTrace();
		}
	}
}
