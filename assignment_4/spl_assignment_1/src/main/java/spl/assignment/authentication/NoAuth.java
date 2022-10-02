package spl.assignment.authentication;

public class NoAuth implements Authenticator {

    @Override
    public boolean checkAuth(String passwd) {
        return true;
    }

    @Override
    public String getAuthString() {
        return "";
    }

    @Override
    public boolean passwordRequired() {
        return false;
    }

    @Override
    public Authenticator newInstance(String authString) {
        return new NoAuth();
    }
    
}
