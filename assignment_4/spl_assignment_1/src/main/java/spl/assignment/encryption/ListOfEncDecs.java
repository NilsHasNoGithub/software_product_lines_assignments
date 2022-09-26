package spl.assignment.encryption;

import java.util.Arrays;

public class ListOfEncDecs implements EncrypterDecrypter {
    private final EncrypterDecrypter[] encDecs;

    public ListOfEncDecs(EncrypterDecrypter[] encDecs) {
        this.encDecs = encDecs;
    }

    @Override
    public byte[] decrypt(byte[] bts) {
        byte[] result = Arrays.copyOf(bts, bts.length);

        for (int i = encDecs.length -1; i >= 0; i--) {
            result = encDecs[i].decrypt(result);
        }

        return result;
    }

    @Override
    public byte[] encrypt(byte[] bts) {
        byte[] result = Arrays.copyOf(bts, bts.length);

        for (int i = 0; i < encDecs.length; i++) {
            result = encDecs[i].encrypt(result);
        }

        return result;
    }

    
    
}
