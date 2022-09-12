package spl.assignment.encryption;


import java.nio.charset.*;

public interface EncrypterDecrypter {
    public byte[] encrypt(byte[] bts);
    public byte[] decrypt(byte[] bts);

    default public byte[] encryptString(String str) {
        byte[] bts = str.getBytes(StandardCharsets.UTF_8);
        return this.encrypt(bts);
    }

    default public String decryptToString(byte[] bts) {
        byte[] decrBytes = this.decrypt(bts);
        return new String(decrBytes, StandardCharsets.UTF_8);
    }
}
