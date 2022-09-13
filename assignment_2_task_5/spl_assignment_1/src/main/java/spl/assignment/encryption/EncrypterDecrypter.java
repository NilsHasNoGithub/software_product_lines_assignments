package spl.assignment.encryption;

import java.nio.charset.*;

public abstract class EncrypterDecrypter {
    protected EncrypterDecrypter child;

    protected EncrypterDecrypter(EncrypterDecrypter child) {
        this.child = child;
    }

    protected EncrypterDecrypter() {
        this.child = null;
    }

    protected abstract byte[] encryptApply(byte[] bts);

    protected abstract byte[] decryptApply(byte[] bts);

    public byte[] encrypt(byte[] bts) {
        bts = this.encryptApply(bts);

        if (this.child != null) {
            bts = this.child.encrypt(bts);
        }

        return bts;
    }

    public byte[] decrypt(byte[] bts) {
        if (this.child != null) {
            bts = this.child.decrypt(bts);
        }
        
        bts = this.decryptApply(bts);

        return bts;
    }

    public byte[] encryptString(String str) {
        byte[] bts = str.getBytes(StandardCharsets.UTF_8);
        return this.encrypt(bts);
    }

    public String decryptToString(byte[] bts) {
        byte[] decrBytes = this.decrypt(bts);
        return new String(decrBytes, StandardCharsets.UTF_8);
    }
}
