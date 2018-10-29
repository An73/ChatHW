package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by dkotenko on 10/25/18.
 */
public class Executor {
    private final Connection connection;

    public Executor(Connection connection){
        this.connection = connection;
    }

    public void execUpdate(String update) throws Exception {
        Statement statement = connection.createStatement();
        statement.execute(update);
        statement.close();
    }

    public <T> T execQuery(String query, IResultSet<T> iResultSet) throws Exception{
        Statement statement = connection.createStatement();
        statement.execute(query);
        ResultSet result = statement.getResultSet();
        T value = iResultSet.getResult(result);
        result.close();
        statement.close();
        return value;
    }
}
