package base;

/**
 * Created by dkotenko on 10/25/18.
 */
public interface IDBService {
    void addUser(UserProfile userProfile) throws Exception;

    UserProfile getUser(String login) throws Exception;

    void create() throws Exception;

    void cleanUp() throws Exception;

    boolean checkNick(String nickname) throws Exception;
}
