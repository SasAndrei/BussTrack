package Accounts;

public abstract class User {

    protected String username;
    protected String password;
    protected String email;

    public abstract void login();

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
