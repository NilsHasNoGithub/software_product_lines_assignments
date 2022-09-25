package spl.assignment.encryption;

public class NoEncryption implements EncrypterDecrypter {
    @Override
    public byte[] decrypt(byte[] bts) {
        return bts;
    }

    @Override
    public byte[] encrypt(byte[] bts) {
        return bts;
    }
}
