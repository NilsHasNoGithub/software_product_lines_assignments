package encryption; 

import java.nio.charset.*; 

public abstract  class  EncrypterDecrypter {
	
    public abstract byte[] encrypt(byte[] bts);

	
    public abstract byte[] decrypt(byte[] bts);

	

    public byte[] encryptString(String str) {
        byte[] bts = str.getBytes(StandardCharsets.UTF_8);
        return this.encrypt(bts);
    }

	

    public String decryptToString(byte[] bts) {
        byte[] decrBytes = this.decrypt(bts);
        return new String(decrBytes, StandardCharsets.UTF_8);
    }


}
