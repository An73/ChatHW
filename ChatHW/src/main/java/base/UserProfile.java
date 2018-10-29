package base;

/**
 * Created by dkotenko on 10/25/18.
 */
public class UserProfile {
    private final String login;
    private final String password;
    private final String nickname;

    public UserProfile(String login, String password, String nickname) {
        this.login = login;
        this.password = password;
        this.nickname = nickname;
    }

    public String getLogin(){
        return login;
    }

    public String getPassword(){
        return password;
    }

    public String getNickname(){
        return nickname;
    }
}
