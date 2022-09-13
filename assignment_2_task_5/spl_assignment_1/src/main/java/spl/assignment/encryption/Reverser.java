package spl.assignment.encryption;

public class Reverser extends EncrypterDecrypter {

    public Reverser(EncrypterDecrypter parent) {
        super(parent);
    }

    public Reverser() {
        super(null);
    }

    @Override
    public byte[] decryptApply(byte[] bts) {
        return this.apply(bts);
    }

    @Override
    public byte[] encryptApply(byte[] bts) {
        return this.apply(bts);
    }

    private byte[] apply(byte[] bts) {
        byte[] result = new byte[bts.length];

        for (int i = 0; i < bts.length; i++) {
            result[i] = bts[bts.length - 1 - i];
        }

        return result;
    }

}
