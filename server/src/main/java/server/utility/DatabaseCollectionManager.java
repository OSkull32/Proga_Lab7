package server.utility;

import common.data.*;
import common.exceptions.DatabaseHandlingException;
import common.interaction.User;
import common.utility.UserConsole;
import server.App;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class DatabaseCollectionManager {
    private final String SELECT_ALL_FLAT = "SELECT * FROM " + DatabaseHandler.FLAT_TABLE;
    private final String SELECT_FLAT_ID = SELECT_ALL_FLAT + " WHERE " +
            DatabaseHandler.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_FLAT_ID_AND_USER_ID = SELECT_FLAT_ID + " AND " +
            DatabaseHandler.FLAT_TABLE_USER_ID_COLUMN + " = ?";
    private final String INSERT_FLAT = "INSERT INTO " +
            DatabaseHandler.FLAT_TABLE + " (" +
            DatabaseHandler.FLAT_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.FLAT_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseHandler.FLAT_TABLE_AREA_COLUMN + ", " +
            DatabaseHandler.FLAT_TABLE_NUMBER_OF_ROOMS_COLUMN + ", " +
            DatabaseHandler.FLAT_TABLE_NUMBER_OF_BATHROOMS_COLUMN + ", " +
            DatabaseHandler.FLAT_TABLE_FURNISH_COLUMN + ", " +
            DatabaseHandler.FLAT_TABLE_VIEW_COLUMN + ", " +
            DatabaseHandler.FLAT_TABLE_HOUSE_ID_COLUMN + ", " +
            DatabaseHandler.FLAT_TABLE_USER_ID_COLUMN + ") VALUES (?, ?, ?, ?, ?, ?," +
            "?, ?, ?)";
    private final String DELETE_FLAT_BY_ID = "DELETE FROM " + DatabaseHandler.FLAT_TABLE +
            " WHERE " + DatabaseHandler.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_NAME_BY_ID = "UPDATE " + DatabaseHandler.FLAT_TABLE + " SET " +
            DatabaseHandler.FLAT_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_BY_ID = "UPDATE " + DatabaseHandler.FLAT_TABLE + " SET " +
            DatabaseHandler.FLAT_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_AREA_BY_ID = "UPDATE " + DatabaseHandler.FLAT_TABLE + " SET " +
            DatabaseHandler.FLAT_TABLE_AREA_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_NUMBER_OF_ROOMS_BY_ID = "UPDATE " + DatabaseHandler.FLAT_TABLE + " SET " +
            DatabaseHandler.FLAT_TABLE_NUMBER_OF_ROOMS_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_NUMBER_OF_BATHROOMS_BY_ID = "UPDATE " + DatabaseHandler.FLAT_TABLE + " SET " +
            DatabaseHandler.FLAT_TABLE_NUMBER_OF_BATHROOMS_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_FURNISH_BY_ID = "UPDATE " + DatabaseHandler.FLAT_TABLE + " SET " +
            DatabaseHandler.FLAT_TABLE_FURNISH_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.FLAT_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_FLAT_VIEW_BY_ID = "UPDATE " + DatabaseHandler.FLAT_TABLE + " SET " +
            DatabaseHandler.FLAT_TABLE_VIEW_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.FLAT_TABLE_ID_COLUMN + " = ?";

    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseHandler.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_FLAT_ID = SELECT_ALL_COORDINATES + " WHERE " +
            DatabaseHandler.COORDINATES_TABLE_FLAT_ID_COLUMN + " = ?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseHandler.COORDINATES_TABLE + " (" +
            DatabaseHandler.COORDINATES_TABLE_FLAT_ID_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?, ?, ?)";
    private final String UPDATE_COORDINATES_BY_FLAT_ID = "UPDATE " + DatabaseHandler.COORDINATES_TABLE + " SET " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + " = ?" +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.COORDINATES_TABLE_FLAT_ID_COLUMN + " = ?";
    private final String DELETE_COORDINATES_BY_FLAT_ID = "DELETE FROM " + DatabaseHandler.COORDINATES_TABLE +
            " WHERE " + DatabaseHandler.COORDINATES_TABLE_FLAT_ID_COLUMN + " = ?";

    private final String SELECT_ALL_HOUSE = "SELECT * FROM " + DatabaseHandler.HOUSE_TABLE;
    private final String SELECT_HOUSE_BY_ID = SELECT_ALL_HOUSE + " WHERE " +
            DatabaseHandler.HOUSE_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_HOUSE = "INSERT INTO " +
            DatabaseHandler.HOUSE_TABLE + " (" +
            DatabaseHandler.HOUSE_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.HOUSE_TABLE_YEAR_COLUMN + ", " +
            DatabaseHandler.HOUSE_TABLE_NUMBER_OF_FLOORS_COLUMN + ", " +
            DatabaseHandler.HOUSE_TABLE_NUMBER_OF_FLATS_ON_FLOOR_COLUMN + ", " +
            DatabaseHandler.HOUSE_TABLE_NUMBER_OF_LIFTS_COLUMN +") VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_HOUSE_BY_ID = "UPDATE " + DatabaseHandler.HOUSE_TABLE + " SET " +
            DatabaseHandler.HOUSE_TABLE_NAME_COLUMN + " = ?" +
            DatabaseHandler.HOUSE_TABLE_YEAR_COLUMN + " = ?" +
            DatabaseHandler.HOUSE_TABLE_NUMBER_OF_FLOORS_COLUMN + " = ?" +
            DatabaseHandler.HOUSE_TABLE_NUMBER_OF_FLATS_ON_FLOOR_COLUMN + " = ?" +
            DatabaseHandler.HOUSE_TABLE_NUMBER_OF_LIFTS_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.HOUSE_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_HOUSE_BY_ID = "DELETE FROM " + DatabaseHandler.HOUSE_TABLE +
            " WHERE " + DatabaseHandler.HOUSE_TABLE_ID_COLUMN + " = ?";

    private final DatabaseHandler databaseHandler;
    private final DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseHandler databaseHandler, DatabaseUserManager databaseUserManager) {
        this.databaseHandler = databaseHandler;
        this.databaseUserManager = databaseUserManager;
    }

    private Flat createFlat(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(DatabaseHandler.FLAT_TABLE_ID_COLUMN);
        String name = resultSet.getString(DatabaseHandler.FLAT_TABLE_NAME_COLUMN);
        LocalDateTime creationDate = resultSet.getTimestamp(DatabaseHandler.FLAT_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
        int area = resultSet.getInt(DatabaseHandler.FLAT_TABLE_AREA_COLUMN);
        long numberOfRooms = resultSet.getLong(DatabaseHandler.FLAT_TABLE_NUMBER_OF_ROOMS_COLUMN);
        long numberOfBathrooms = resultSet.getLong(DatabaseHandler.FLAT_TABLE_NUMBER_OF_BATHROOMS_COLUMN);
        Furnish furnish = Furnish.valueOf(resultSet.getString(DatabaseHandler.FLAT_TABLE_FURNISH_COLUMN));
        View view = View.valueOf(resultSet.getString(DatabaseHandler.FLAT_TABLE_VIEW_COLUMN));
        Coordinates coordinates = getCoordinatesByFlatId(id);
        House house = getHouseById(resultSet.getInt(DatabaseHandler.FLAT_TABLE_HOUSE_ID_COLUMN));
        User owner = databaseUserManager.getUserById(resultSet.getLong(DatabaseHandler.FLAT_TABLE_USER_ID_COLUMN));
        return new Flat(id, name, coordinates, creationDate, area, numberOfRooms, numberOfBathrooms,
                furnish, view, house, owner);
    }

    public Hashtable<Integer, Flat> getCollection() throws DatabaseHandlingException {
        Hashtable<Integer, Flat> flatList = new Hashtable<>();
        PreparedStatement preparedSelectAllStatement = null;
        try {
            preparedSelectAllStatement = databaseHandler.getPreparedStatement(SELECT_ALL_FLAT, false);
            ResultSet resultSet = preparedSelectAllStatement.executeQuery();
            while (resultSet.next()) {
                int key = resultSet.getInt(DatabaseHandler.FLAT_TABLE_ID_COLUMN);
                flatList.put(key, createFlat(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectAllStatement);
        }
        return flatList;
    }

    private int getHouseIdByFlatId(int flatId) throws SQLException {
        int houseId;
        PreparedStatement preparedSelectFlatStatement = null;
        try {
            preparedSelectFlatStatement = databaseHandler.getPreparedStatement(SELECT_FLAT_ID, false);
            preparedSelectFlatStatement.setInt(1, flatId);
            ResultSet resultSet = preparedSelectFlatStatement.executeQuery();
            App.logger.info("Выполнен запрос SELECT_FLAT_ID");
            if (resultSet.next()) {
                houseId = resultSet.getInt(DatabaseHandler.FLAT_TABLE_HOUSE_ID_COLUMN);
            } else throw new SQLException();
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при выполнении запроса SELECT_FLAT_ID");
            throw new SQLException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectFlatStatement);
        }
        return houseId;
    }

    private Coordinates getCoordinatesByFlatId(int flatId) throws SQLException {
        Coordinates coordinates;
        PreparedStatement preparedSelectCoordinatesStatement = null;
        try {
            preparedSelectCoordinatesStatement = databaseHandler.getPreparedStatement(SELECT_COORDINATES_BY_FLAT_ID, false);
            preparedSelectCoordinatesStatement.setInt(1, flatId);
            ResultSet resultSet = preparedSelectCoordinatesStatement.executeQuery();
            App.logger.info("Выполнен запрос SELECT_COORDINATES_BY_FLAT_ID");
            if (resultSet.next()) {
                coordinates = new Coordinates(resultSet.getInt(DatabaseHandler.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getInt(DatabaseHandler.COORDINATES_TABLE_Y_COLUMN));
            } else throw new SQLException();
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при выполнении запроса SELECT_COORDINATES_BY_FLAT_ID");
            throw new SQLException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectCoordinatesStatement);
        }
        return coordinates;
    }

    private House getHouseById(int houseId) throws SQLException {
        House house;
        PreparedStatement preparedSelectHouseStatement = null;
        try {
            preparedSelectHouseStatement = databaseHandler.getPreparedStatement(SELECT_HOUSE_BY_ID, false);
            preparedSelectHouseStatement.setInt(1, houseId);
            ResultSet resultSet = preparedSelectHouseStatement.executeQuery();
            //UserConsole.printCommandTextNext(String.valueOf(resultSet.next()));
            App.logger.info("Выполнен запрос SELECT_HOUSE_BY_ID");
            if (resultSet.next()) {
                house = new House(resultSet.getString(DatabaseHandler.HOUSE_TABLE_NAME_COLUMN),
                        resultSet.getInt(DatabaseHandler.HOUSE_TABLE_YEAR_COLUMN),
                        resultSet.getLong(DatabaseHandler.HOUSE_TABLE_NUMBER_OF_FLOORS_COLUMN),
                        resultSet.getLong(DatabaseHandler.HOUSE_TABLE_NUMBER_OF_FLATS_ON_FLOOR_COLUMN),
                        resultSet.getLong(DatabaseHandler.HOUSE_TABLE_NUMBER_OF_LIFTS_COLUMN));
            } else throw new SQLException();
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при выполнении запроса SELECT_HOUSE_BY_ID");
            throw new SQLException(ex);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectHouseStatement);
        }
        return house;
    }

    public Flat insertFlat(Flat flat, User user) throws DatabaseHandlingException {
        Flat flat1;
        PreparedStatement preparedInsertFlatStatement = null;
        PreparedStatement preparedInsertCoordinatesStatement = null;
        PreparedStatement preparedInsertHouseStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            LocalDateTime creationTime = LocalDateTime.now();

            preparedInsertFlatStatement = databaseHandler.getPreparedStatement(INSERT_FLAT, true);
            preparedInsertCoordinatesStatement = databaseHandler.getPreparedStatement(INSERT_COORDINATES, true);
            preparedInsertHouseStatement = databaseHandler.getPreparedStatement(INSERT_HOUSE, true);

            preparedInsertHouseStatement.setString(1, flat.getHouse().getName());
            preparedInsertHouseStatement.setInt(2, flat.getHouse().getYear());
            preparedInsertHouseStatement.setLong(3, flat.getHouse().getNumberOfFloors());
            preparedInsertHouseStatement.setLong(4, flat.getHouse().getNumberOfFlatsOnFloor());
            preparedInsertHouseStatement.setLong(5, flat.getHouse().getNumberOfLifts());
            if (preparedInsertHouseStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generateHouseKeys = preparedInsertHouseStatement.getGeneratedKeys();
            int houseId;
            if (generateHouseKeys.next()) {
                houseId = generateHouseKeys.getInt(1);
            } else throw new SQLException();
            App.logger.info("Выполнен запрос INSERT_HOUSE");

            preparedInsertFlatStatement.setString(1, flat.getName());
            preparedInsertFlatStatement.setTimestamp(2, Timestamp.valueOf(creationTime));
            preparedInsertFlatStatement.setInt(3, flat.getArea());
            preparedInsertFlatStatement.setLong(4, flat.getNumberOfRooms());
            preparedInsertFlatStatement.setLong(5, flat.getNumberOfBathrooms());
            preparedInsertFlatStatement.setString(6, flat.getFurnish().toString());
            preparedInsertFlatStatement.setString(7, flat.getView().toString());
            preparedInsertFlatStatement.setInt(8, houseId);
            preparedInsertFlatStatement.setLong(9, databaseUserManager.getUserIdByUsername(user));
            if (preparedInsertFlatStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generateFlatKeys = preparedInsertFlatStatement.getGeneratedKeys();
            int flatId;
            if (generateFlatKeys.next()) {
                flatId = generateFlatKeys.getInt(1);
            } else throw new SQLException();
            App.logger.info("Выполнен запрос INSERT_FLAT");

            preparedInsertCoordinatesStatement.setInt(1, flatId);
            preparedInsertCoordinatesStatement.setInt(2, flat.getCoordinates().getX());
            preparedInsertCoordinatesStatement.setInt(3, flat.getCoordinates().getY());
            if (preparedInsertCoordinatesStatement.executeUpdate() == 0) throw new SQLException();
            App.logger.info("Выполнен запрос INSERT_COORDINATES");

            flat1 = new Flat(flatId, flat.getName(), flat.getCoordinates(), creationTime,
                    flat.getArea(), flat.getNumberOfRooms(), flat.getNumberOfBathrooms(),
                    flat.getFurnish(), flat.getView(), flat.getHouse(), user);

            databaseHandler.commit();
            return flat1;
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при выполнении группы запросов на добавление нового объекта");
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedInsertFlatStatement);
            databaseHandler.closePreparedStatement(preparedInsertCoordinatesStatement);
            databaseHandler.closePreparedStatement(preparedInsertHouseStatement);
            databaseHandler.setNormalMode();
        }
    }

    public void updateFlatById(int flatId, Flat flat) throws DatabaseHandlingException {
        PreparedStatement preparedUpdateFlatNameStatement = null;
        PreparedStatement preparedUpdateFlatAreaStatement = null;
        PreparedStatement preparedUpdateFlatNumberOfRoomsStatement = null;
        PreparedStatement preparedUpdateFlatNumberOfBathroomsStatement = null;
        PreparedStatement preparedUpdateFlatFurnishStatement = null;
        PreparedStatement preparedUpdateFlatViewStatement = null;
        PreparedStatement preparedUpdateCoordinatesStatement = null;
        PreparedStatement preparedUpdateHouseStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            preparedUpdateFlatNameStatement = databaseHandler.getPreparedStatement(UPDATE_FLAT_NAME_BY_ID, false);
            preparedUpdateFlatAreaStatement = databaseHandler.getPreparedStatement(UPDATE_FLAT_AREA_BY_ID, false);
            preparedUpdateFlatNumberOfRoomsStatement = databaseHandler.getPreparedStatement(UPDATE_FLAT_NUMBER_OF_ROOMS_BY_ID, false);
            preparedUpdateFlatNumberOfBathroomsStatement = databaseHandler.getPreparedStatement(UPDATE_FLAT_NUMBER_OF_BATHROOMS_BY_ID,false);
            preparedUpdateFlatFurnishStatement = databaseHandler.getPreparedStatement(UPDATE_FLAT_FURNISH_BY_ID,false);
            preparedUpdateFlatViewStatement = databaseHandler.getPreparedStatement(UPDATE_FLAT_VIEW_BY_ID,false);
            preparedUpdateCoordinatesStatement = databaseHandler.getPreparedStatement(UPDATE_COORDINATES_BY_FLAT_ID,false);
            preparedUpdateHouseStatement = databaseHandler.getPreparedStatement(UPDATE_HOUSE_BY_ID,false);

            if (flat.getName() != null) {
                preparedUpdateFlatNameStatement.setString(1, flat.getName());
                preparedUpdateFlatNameStatement.setInt(2, flatId);
                if (preparedUpdateFlatNameStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_FLAT_NAME_BY_ID");
            }

            if (flat.getCoordinates() != null) {
                preparedUpdateCoordinatesStatement.setInt(1, flat.getCoordinates().getX());
                preparedUpdateCoordinatesStatement.setInt(2, flat.getCoordinates().getY());
                preparedUpdateCoordinatesStatement.setInt(3, flatId);
                if (preparedUpdateCoordinatesStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_COORDINATES_BY_FLAT_ID");
            }

            if (flat.getArea() != -1) {
                preparedUpdateFlatAreaStatement.setInt(1, flat.getArea());
                preparedUpdateFlatAreaStatement.setInt(2, flatId);
                if (preparedUpdateFlatAreaStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_FLAT_AREA_BY_ID");
            }

            if (flat.getNumberOfRooms() != -1) {
                preparedUpdateFlatNumberOfRoomsStatement.setLong(1, flat.getNumberOfRooms());
                preparedUpdateFlatNumberOfRoomsStatement.setLong(2, flatId);
                if (preparedUpdateFlatNumberOfRoomsStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_FLAT_NUMBER_OF_ROOMS_BY_ID");
            }

            if (flat.getNumberOfBathrooms() != -1) {
                preparedUpdateFlatNumberOfBathroomsStatement.setLong(1, flat.getNumberOfBathrooms());
                preparedUpdateFlatNumberOfBathroomsStatement.setLong(2, flatId);
                if (preparedUpdateFlatNumberOfBathroomsStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_FLAT_NUMBER_OF_BATHROOMS_BY_ID");
            }

            if (flat.getFurnish() != null) {
                preparedUpdateFlatFurnishStatement.setString(1, flat.getFurnish().toString());
                preparedUpdateFlatFurnishStatement.setLong(2, flatId);
                if (preparedUpdateFlatFurnishStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_FLAT_FURNISH_BY_ID");
            }

            if (flat.getView() != null) {
                preparedUpdateFlatViewStatement.setString(1, flat.getView().toString());
                preparedUpdateFlatViewStatement.setLong(2, flatId);
                if (preparedUpdateFlatViewStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_FLAT_VIEW_BY_ID");
            }

            if (flat.getHouse() != null) {
                preparedUpdateHouseStatement.setString(1, flat.getHouse().getName());
                preparedUpdateHouseStatement.setInt(2, flat.getHouse().getYear());
                preparedUpdateHouseStatement.setLong(3, flat.getHouse().getNumberOfFloors());
                preparedUpdateHouseStatement.setLong(4, flat.getHouse().getNumberOfFlatsOnFloor());
                preparedUpdateHouseStatement.setLong(5, flat.getHouse().getNumberOfLifts());
                preparedUpdateHouseStatement.setLong(6, flatId);
                if (preparedUpdateHouseStatement.executeUpdate() == 0) throw new SQLException();
                App.logger.info("Выполнен запрос UPDATE_HOUSE_BY_ID");
            }

            databaseHandler.commit();
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при выполнении группы запросов на обновление объекта");
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedUpdateFlatNameStatement);
            databaseHandler.closePreparedStatement(preparedUpdateFlatAreaStatement);
            databaseHandler.closePreparedStatement(preparedUpdateFlatNumberOfRoomsStatement);
            databaseHandler.closePreparedStatement(preparedUpdateFlatNumberOfBathroomsStatement);
            databaseHandler.closePreparedStatement(preparedUpdateFlatFurnishStatement);
            databaseHandler.closePreparedStatement(preparedUpdateFlatViewStatement);
            databaseHandler.closePreparedStatement(preparedUpdateCoordinatesStatement);
            databaseHandler.closePreparedStatement(preparedUpdateHouseStatement);
            databaseHandler.setNormalMode();
        }
    }

    public void deleteFlatById(int flatId) throws DatabaseHandlingException {
        PreparedStatement preparedDeleteHouseStatement = null;
        PreparedStatement preparedDeleteFlatStatement = null;
        PreparedStatement preparedDeleteCoordinatesStatement = null;
        try {
            preparedDeleteCoordinatesStatement = databaseHandler.getPreparedStatement(DELETE_COORDINATES_BY_FLAT_ID, false);
            preparedDeleteFlatStatement = databaseHandler.getPreparedStatement(DELETE_FLAT_BY_ID, false);
            preparedDeleteHouseStatement = databaseHandler.getPreparedStatement(DELETE_HOUSE_BY_ID, false);

            preparedDeleteCoordinatesStatement.setInt(1, flatId);
            preparedDeleteFlatStatement.setInt(1, flatId);
            preparedDeleteHouseStatement.setInt(1, getHouseIdByFlatId(flatId));

            if (preparedDeleteCoordinatesStatement.executeUpdate() == 0) throw new SQLException();
            App.logger.info("Выполнен запрос DELETE_COORDINATES_BY_FLAT_ID");

            if (preparedDeleteFlatStatement.executeUpdate() == 0) throw new SQLException();
            App.logger.info("Выполнен запрос DELETE_FLAT_BY_ID");

            if (preparedDeleteHouseStatement.executeUpdate() == 0) throw new SQLException();
            App.logger.info("Выполнен запрос DELETE_HOUSE_BY_ID");
        } catch (SQLException ex) {
            ex.printStackTrace();
            App.logger.severe("Произошла ошибка при выполнении запроса DELETE_HOUSE_BY_ID");
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedDeleteHouseStatement);
            databaseHandler.closePreparedStatement(preparedDeleteCoordinatesStatement);
            databaseHandler.closePreparedStatement(preparedDeleteFlatStatement);
        }
    }

    public boolean checkFlatUserId(int flatId, User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectFlatStatement = null;
        try {
            preparedSelectFlatStatement = databaseHandler.getPreparedStatement(SELECT_FLAT_ID_AND_USER_ID, false);
            preparedSelectFlatStatement.setInt(1, flatId);
            preparedSelectFlatStatement.setLong(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedSelectFlatStatement.executeQuery();
            App.logger.info("Выполнен запрос SELECT_FLAT_ID_AND_USER_ID");
            return resultSet.next();
        } catch (SQLException ex) {
            App.logger.severe("Произошла ошибка при выполнении запроса SELECT_FLAT_ID_AND_USER_ID");
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectFlatStatement);
        }
    }

    public void clearCollection() throws DatabaseHandlingException {
        Hashtable<Integer, Flat> flatList = getCollection();
        for (Map.Entry<Integer, Flat> entry : flatList.entrySet()) {
            Flat flat = getCollection().get(entry.getKey());
            deleteFlatById(flat.getId());
        }
    }


}
