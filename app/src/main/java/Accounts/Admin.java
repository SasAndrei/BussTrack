package Accounts;

public class Admin extends User{

    public Admin(String user, String pass) {
        super(user,pass);
    }
    public Admin() {

    }
    public Admin(String user, String pass, String email) {
        super(user,pass, email);
    }
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void login() {

    }

    public void editRoute()
    {

    }

    public void editBus()
    {

    }

    public void editStation()
    {

    }

    public void createDriverAccount()
    {

    }

    public void createStandardUserAccount()
    {

    }
}
