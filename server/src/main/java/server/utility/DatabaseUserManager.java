package server.utility;

import common.exceptions.DatabaseHandlingException;
import common.interaction.User;
import server.App;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserManager {
    private final String SELECT_USER_BY_ID = "SELECT * FROM " + DatabaseHandler.USER_TABLE +
            " WHERE " + DatabaseHandler.USER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME = "SELECT * FROM " + DatabaseHandler.USER_TABLE +
            " WHERE " + DatabaseHandler.USER_TABLE_USERNAME_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME_AND_PASSWORD = SELECT_USER_BY_USERNAME + " AND " +
            DatabaseHandler.USER_TABLE_PASSWORD_COLUMN + " = ?";
    private final String INSERT_USER = "INSERT INTO " +
            DatabaseHandler.USER_TABLE + " (" +
            DatabaseHandler.USER_TABLE_USERNAME_COLUMN + ", " +
            DatabaseHandler.USER_TABLE_PASSWORD_COLUMN + ") VALUES (?, ?)";

    private DatabaseHandler databaseHandler;

    public DatabaseUserManager(DatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public User getUserById(long userId) throws SQLException {
        User user;
        PreparedStatement preparedSelectUserStatement = null;
        try {
            preparedSelectUserStatement = databaseHandler.getPreparedStatement(SELECT_USER_BY_ID, false);
            preparedSelectUserStatement.setLong(1, userId);
            ResultSet resultSet = preparedSelectUserStatement.executeQuery();
            App.logger.info("Выполнен запрос SELECT_USER_BY_ID");
            if (resultSet.next()) {
                user = new User(resultSet.getString(DatabaseHandler.USER_TABLE_USERNAME_COLUMN),
                        resultSet.getString(DatabaseHandler.USER_TABLE_PASSWORD_COLUMN));
            } else throw new SQLException();
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при выполнении запроса SELECT_USER_BY_ID");
            throw new SQLException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectUserStatement);
        }
        return user;
    }

    public boolean checkUserByUsernameAndPassword(User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectUserStatement = null;
        try {
            preparedSelectUserStatement = databaseHandler.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedSelectUserStatement.setString(1, user.getUsername());
            preparedSelectUserStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedSelectUserStatement.executeQuery();
            App.logger.info("Выполнен запрос SELECT_USER_BY_USERNAME_AND_PASSWORD");
            return resultSet.next();
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME_AND_PASSWORD");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectUserStatement);
        }
    }

    public long getUserIdByUsername(User user) throws DatabaseHandlingException {
        long userId;
        PreparedStatement preparedSelectUserStatement = null;
        try {
            preparedSelectUserStatement = databaseHandler.getPreparedStatement(SELECT_USER_BY_USERNAME, false);
            preparedSelectUserStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedSelectUserStatement.executeQuery();
            App.logger.info("Выполнен запрос SELECT_USER_BY_USERNAME");
            if (resultSet.next()) {
                userId = resultSet.getLong(DatabaseHandler.USER_TABLE_ID_COLUMN);
            } else userId = -1;
            return userId;
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при выполнении запроса SELECT_USER_BY_USERNAME");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectUserStatement);
        }
    }

    public boolean insertUser(User user) throws DatabaseHandlingException {
        PreparedStatement preparedInsertUserStatement = null;
        try {
            if (getUserIdByUsername(user) != -1) return false;
            preparedInsertUserStatement = databaseHandler.getPreparedStatement(INSERT_USER, false);
            preparedInsertUserStatement.setString(1, user.getUsername());
            preparedInsertUserStatement.setString(2, user.getPassword());
            if (preparedInsertUserStatement.executeUpdate() == 0) throw new SQLException();
            App.logger.info("Выполнен запрос INSERT_USER");
            return true;
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при выполнении запроса INSERT_USER");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedInsertUserStatement);
        }
    }
}
