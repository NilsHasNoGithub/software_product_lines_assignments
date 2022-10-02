package spl_assignment_1.spl_assignment_1;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import spl.assignment.client.Client;
import spl.assignment.encryption.EncrypterDecrypter;
import spl.assignment.encryption.Reverser;
import spl.assignment.main.Main;
import spl.assignment.server.Server;
import spl.assignment.utils.Logger;
import spl.assignment.utils.Message;

/**
 * Unit test for simple App.
 */
public class AppTest {
  @Test
  public void testEncryption() {
	String msg = "abcd";

    byte[] encrypted = Main.ENC_DEC.encryptString(msg);
    String decrypted = Main.ENC_DEC.decryptToString(encrypted);

    assertEquals(msg, decrypted);
  }
}
