package db;

import base.IDBService;
import base.UserProfile;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by dkotenko on 10/25/18.
 */
public class DBService implements IDBService {

    private Connection connection;
    private DAO dao;

    public DBService() throws Exception {
        String url = "jdbc:h2:./h2db";
        String username = "admin";
        String password = "admin";


        connection = DriverManager.getConnection(url, username, password);
        dao = new DAO(connection);
    }

    @Override
    public void addUser(UserProfile userProfile) throws Exception {
        dao.insertClient(userProfile);
    }

    @Override
    public UserProfile getUser(String login) throws Exception {
        return dao.getClient(login);
    }

    @Override
    public void create() throws Exception {
        dao.create();
    }

    @Override
    public void cleanUp() throws Exception {
        dao.cleanup();
    }

    @Override
    public boolean checkNick(String nickname) throws Exception {
        return (dao.checkNickname(nickname));
    }
}
