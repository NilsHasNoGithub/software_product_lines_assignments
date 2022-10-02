package spl.assignment.authentication;

public class PlainPasswordAuth implements Authenticator{
    
    private final String password;

    public PlainPasswordAuth(String password) {
        this.password = password;
    }

    @Override
    public boolean checkAuth(String passwd) {
        return passwd.equals(this.password);
    }

    @Override
    public String getAuthString() {
        return this.password;
    }

    @Override
    public boolean passwordRequired() {
        return true;
    }

    @Override
    public Authenticator newInstance(String authString) {
        return new PlainPasswordAuth(authString);
    }
    
}
