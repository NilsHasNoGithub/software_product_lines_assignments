package spl.assignment.encryption;



//#if REVERSER_ENCRYPTION
//@public class Reverser implements EncrypterDecrypter {
//@
//@    @Override
//@    public byte[] decrypt(byte[] bts) {
//@        return this.apply(bts);
//@    }
//@
//@    @Override
//@    public byte[] encrypt(byte[] bts) {
//@        return this.apply(bts);
//@    }
//@
//@    private byte[] apply(byte[] bts) {
//@        byte[] result = new byte[bts.length];
//@
//@        for (int i = 0; i < bts.length; i++) {
//@            result[i] = bts[bts.length - 1 - i];
//@        }
//@
//@        return result;
//@    }
//@
//@}
//#else
import java.util.Arrays;
public class Reverser implements EncrypterDecrypter {

    @Override
    public byte[] decrypt(byte[] bts) {
        return Arrays.copyOf(bts, bts.length);
    }

    @Override
    public byte[] encrypt(byte[] bts) {
    	return Arrays.copyOf(bts, bts.length);
    }

}
//#endif
