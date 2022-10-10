package utils; 

import java.io.IOException; 
import java.io.DataInputStream; 
import java.nio.BufferUnderflowException; 

import encryption.EncrypterDecrypter; 

import java.io.DataOutputStream; 


public  class  Communication {
	


    private Communication() {}

	

    public static byte[] retrieveBytes(DataInputStream is) throws IOException, BufferUnderflowException {
        int contentLen = is.readInt();
        byte[] msgBytes = is.readNBytes(contentLen);
        return msgBytes;
    }

	

    public static void sendBytes(byte[] bts, DataOutputStream os) throws IOException {
        os.writeInt(bts.length);
        os.write(bts);
    }

	

    public static String retrieveAndDecrypt(EncrypterDecrypter encDec, DataInputStream is) throws IOException {
		return encDec.decryptToString(Communication.retrieveBytes(is));
	}

	

	public static void encryptAndSend(String content, EncrypterDecrypter encDec, DataOutputStream os) throws IOException {
		Communication.sendBytes(encDec.encryptString(content), os);
	}


}
