package spl.assignment.authentication;

public class PlainPasswordAuth implements Authenticator{
    public static String PASSWORD = "123456789";

    @Override
    public boolean checkPassword(String passwd) {
        return passwd.equals(PASSWORD);
    }
    
}
