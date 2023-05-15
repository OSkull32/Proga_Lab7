package server.utility;

import common.utility.UserConsole;
import server.App;

import java.sql.*;

public class DatabaseHandler {
    public static final String FLAT_TABLE = "flat";
    public static final String USER_TABLE = "user";
    public static final String COORDINATES_TABLE = "coordinates";
    public static final String HOUSE_TABLE = "house";

    public static final String FLAT_TABLE_ID_COLUMN = "id";
    public static final String FLAT_TABLE_NAME_COLUMN = "name";
    public static final String FLAT_TABLE_CREATION_DATE_COLUMN = "creation_date";
    public static final String FLAT_TABLE_AREA_COLUMN = "area";
    public static final String FLAT_TABLE_NUMBER_OF_ROOMS_COLUMN = "number_of_rooms";
    public static final String FLAT_TABLE_NUMBER_OF_BATHROOMS_COLUMN = "number_of_bathrooms";
    public static final String FLAT_TABLE_FURNISH_COLUMN = "furnish";
    public static final String FLAT_TABLE_VIEW_COLUMN = "view";
    public static final String FLAT_TABLE_HOUSE_ID_COLUMN = "house_id";
    public static final String FLAT_TABLE_USER_ID_COLUMN = "user_id";

    public static final String USER_TABLE_ID_COLUMN = "id";
    public static final String USER_TABLE_USERNAME_COLUMN = "username";
    public static final String USER_TABLE_PASSWORD_COLUMN = "password";

    public static final String COORDINATES_TABLE_ID_COLUMN = "id";
    public static final String COORDINATES_TABLE_FLAT_ID_COLUMN = "flat_id";
    public static final String COORDINATES_TABLE_X_COLUMN = "x";
    public static final String COORDINATES_TABLE_Y_COLUMN = "y";

    public static final String HOUSE_TABLE_ID_COLUMN = "id";
    public static final String HOUSE_TABLE_NAME_COLUMN = "name";
    public static final String HOUSE_TABLE_YEAR_COLUMN = "year";
    public static final String HOUSE_TABLE_NUMBER_OF_FLOORS_COLUMN = "number_of_floors";
    public static final String HOUSE_TABLE_NUMBER_OF_FLATS_ON_FLOOR_COLUMN = "number_of_flats_on_floor";
    public static final String HOUSE_TABLE_NUMBER_OF_LIFTS_COLUMN = "number_of_lifts";

    private final String JDBC_DRIVER = "org.postgresql.Driver";

    private String url;
    private String user;
    private String password;
    private Connection connection;

    public DatabaseHandler(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;

        connectToDataBase();
    }

    private void connectToDataBase() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url, user, password);
            UserConsole.printCommandTextNext("Соединение с базой данных установлено");
            App.logger.info("Соединение с базой данных установлено");
        } catch (SQLException ex) {
            ex.printStackTrace();
            UserConsole.printCommandError("Произошла ошибка при подключении к базе данных");
            App.logger.severe("Произошла ошибка при подключении к базе данных");
        } catch (ClassNotFoundException ex) {
            UserConsole.printCommandError("Драйвер для управления базой данных не найден");
            App.logger.severe("Драйвер для управления базой данных не найден");
        }
    }

    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generateKeys) throws SQLException {
        PreparedStatement preparedStatement;
        try {
            if (connection == null) throw new SQLException();
            int autoGenerateKeys = generateKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
            preparedStatement = connection.prepareStatement(sqlStatement, autoGenerateKeys);
            App.logger.info("Подготовлен SQL запрос '" + sqlStatement + "'.");
            return preparedStatement;
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при подготовке SQL запроса '" + sqlStatement + "'.");
            if (connection == null) App.logger.severe("Соединение с базой данных не установлено");
            throw new SQLException(ex);
        }
    }

    public void closePreparedStatement(PreparedStatement sqlStatement) {
        if (sqlStatement == null) return;
        try {
            sqlStatement.close();
            App.logger.info("Закрыт SQL запрос '" + sqlStatement + "'.");
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при закрытии" +
                    " SQL запроса '" + sqlStatement + "'.");
        }
    }

    public void closeConnection() {
        if (connection == null) return;
        try {
            connection.close();
            UserConsole.printCommandTextNext("Соединение с базой данных разорвано");
            App.logger.info("Соединение с базой данных разорвано");
        } catch (SQLException ex) {
            UserConsole.printCommandError("Произошла ошибка при разрыве соединения с базой данных");
            App.logger.severe("Произошла ошибка при разрыве соединения с базой данных");
        }
    }

    public void setCommitMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при установлении режима транзакции базы данных");
        }
    }

    public void setNormalMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при установлении нормального режима базы данных");
        }
    }

    public void commit() {
        try {
            if (connection == null) throw new SQLException();
            connection.commit();
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при подтверждении нового состояния базы данных");
        }
    }

    public void rollback() {
        try {
            if (connection == null) throw new SQLException();
            connection.rollback();
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при возврате исходного состояния базы данных");
        }
    }

    public void setSavepoint() {
        try {
            if (connection == null) throw new SQLException();
            connection.setSavepoint();
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при сохранении состояния базы данных");
        }
    }
}
