package poopprojekat.studentska_sluzba;

public class User
{
    String username;
    String password;
    String role;
    String id;

    public User(String username, String pass, String role, String uniqueId)
    {
        this(username, role, uniqueId);
        password = pass;
    }

    public User(String username, String role, String uniqueId)
    {
        this.username = username;
        this.role = role;
        id = uniqueId;
    }
}
