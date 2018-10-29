package db;

import base.UserProfile;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created by dkotenko on 10/25/18.
 */
public class DAO {
    private Executor executor;

    public DAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public void create() throws Exception {
        executor.execUpdate("create table if not exists clients (id bigint auto_increment, login varchar(256), password varchar(256), nickname varchar(256))");
    }

    public void cleanup() throws Exception {
        executor.execUpdate("drop table clients");
    }

    public void insertClient(UserProfile userProfile) throws Exception {
        executor.execUpdate("insert into clients (login, password, nickname) values ('" +
        userProfile.getLogin() + "','" + userProfile.getPassword() + "','" + userProfile.getNickname() + "')");
    }

    public UserProfile getClient(String login) throws Exception {
        return executor.execQuery("select * from clients where login='" + login + "'", new IResultSet<UserProfile>() {
            @Override
            public UserProfile getResult(ResultSet resultSet) throws Exception {
                if (resultSet.next())
                    return new UserProfile(resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4));
                return null;
            }
        });
    }

    public boolean checkNickname(String nickname) throws Exception {
        return executor.execQuery("select * from clients where nickname='" + nickname + "'", new IResultSet<Boolean>() {
            @Override
            public Boolean getResult(ResultSet resultSet) throws Exception {
                if (resultSet.next())
                    return true;
                return false;
            }
        });
    }


}
