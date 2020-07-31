package poopprojekat.studentska_sluzba;

public class User
{
    public String username;
    public String password;
    public String role;
    public String id;

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
