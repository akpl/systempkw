package pkw.models;

public class User {
    private String _login;
    private String _password;
    private String _name;
    private String _surname;

    public User(String _login, String _password, String _name, String _surname) {
        this._login = _login;
        this._password = _password;
        this._name = _name;
        this._surname = _surname;
    }

    public boolean checkPassword(String password)
    {
        //TODO: hash
        return _password.equals(password);
    }
}
