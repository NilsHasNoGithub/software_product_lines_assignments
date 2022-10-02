package spl.assignment.authentication;

public interface Authenticator {
    public boolean checkAuth(String authString);
    public String getAuthString();
    public boolean passwordRequired();
    public Authenticator newInstance(String authString);
}
