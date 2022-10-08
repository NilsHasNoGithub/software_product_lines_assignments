package main;

import java.util.List;

import encryption.EncrypterDecrypter;
import encryption.SeededEncDec;
/**
 * TODO description
 */
public class Main {
	private static List<EncrypterDecrypter> getSubEncDecs() {
		List<EncrypterDecrypter> result = original();
		result.add(new SeededEncDec(42));
    	return result;
    }
}