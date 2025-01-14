package com.purplebits.emrd2.util;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RandomString {

	private final Logger logger = LogManager.getLogger(RandomString.class);
	private final String className = RandomString.class.getSimpleName();
	private int randomNumberLength;

	public RandomString() {
		this(10);
	}

	public RandomString(int randomNumberLength) {
		this.randomNumberLength = randomNumberLength;
	}

	public String passcodeGenerator() {

		logger.info(className + " passcodeGenerator() invoked.");

		String passcode = "";
		Random random = new Random();
		int upperbound = 9;
		int i = 0;
		int temp;
		while (i < randomNumberLength) {

			temp = random.nextInt(upperbound);
			if (i == 0 && temp == 0) {
				while (temp == 0) {
					temp = random.nextInt(upperbound);
				}
			}
			passcode = passcode + String.valueOf(temp);
			i++;
		}

		logger.info(className + " passcodeGenerator() returned PD_passcode : " + passcode);

		return passcode;
	}

}