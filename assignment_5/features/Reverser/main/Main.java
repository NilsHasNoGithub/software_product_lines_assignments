package main;

import java.util.List;

import encryption.EncrypterDecrypter;
import encryption.Reverser;

/**
 * TODO description
 */
public class Main {
	private static List<EncrypterDecrypter> getSubEncDecs() {
		List<EncrypterDecrypter> result = original();
		result.add(new Reverser());
    	return result;
    }
}