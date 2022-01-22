package Accounts;

public abstract class User {

    private String username;
    private String password;

    public abstract void login();

    public String getUsername() { return username; }

    public String getPassword() { return password; }
}
