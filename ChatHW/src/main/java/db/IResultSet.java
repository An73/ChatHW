package db;

import java.sql.ResultSet;

/**
 * Created by dkotenko on 10/25/18.
 */
public interface IResultSet<T> {
    T getResult(ResultSet resultSet) throws Exception;
}
