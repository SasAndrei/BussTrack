package Accounts;

public abstract class User {

    protected String username;
    protected String password;

    public abstract void login();

    public String getUsername() { return username; }

    public String getPassword() { return password; }
}
