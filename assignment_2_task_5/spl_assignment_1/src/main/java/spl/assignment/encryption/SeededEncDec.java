package spl.assignment.encryption;

import java.util.Random;
import java.util.Arrays;

public class SeededEncDec extends EncrypterDecrypter{
    private final long seed;

    public SeededEncDec(EncrypterDecrypter parent, long seed) {
        super(parent);
        this.seed = seed;
    }

    public SeededEncDec(long seed) {
        super(null);
        this.seed=seed;
    }

    @Override
    public byte[] decryptApply(byte[] in) {
        return this.apply(in, false);
    }

    @Override
    public byte[] encryptApply(byte[] in) {
        return this.apply(in, true);
    }

    private byte[] apply(byte[] in, boolean encode) {
        Random random = new Random(seed);

        // mutating arguments is bad practice
        byte[] out = Arrays.copyOf(in, in.length);

        byte[] randBytes = new byte[in.length];

        random.nextBytes(randBytes);
        byte factor = (byte) (encode ? 1: -1);

        for (int i = 0; i < in.length; i++) {
            out[i] += factor * randBytes[i];
        }

        
        return out;
    }
    
}
