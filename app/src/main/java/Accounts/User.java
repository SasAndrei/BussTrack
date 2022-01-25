package Accounts;

public abstract class User {

    protected String username;
    protected String password;
    protected String email;

    public abstract void login();

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public User() {
    }
    public User(String user, String pass) {
        username = user;
        password = pass;
    }

    public User(String user, String pass, String email) {
        this(user, pass);
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
