package server.utility;

import common.data.Coordinates;
import common.data.Flat;
import common.data.Furnish;
import common.data.House;
import common.exceptions.CollectionException;
import common.utility.FlatReader;
import server.App;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class CollectionChecker {

    /**
     * Основной метод проверки коллекции.
     *
     * @param collection коллекция на проверку.
     */
    public void checkCollection(Hashtable<Integer, Flat> collection) throws CollectionException {
        if (collection == null) return;
        Set<Integer> keySet = Set.copyOf(collection.keySet());

        for (Integer key : keySet) {
            Flat flat = collection.get(key);

            //проверка полей
            if (!FlatChecker.checkId(flat.getId())) {
                throw new CollectionException("Ошибка в поле ID у объекта " + flat.getName());
            }
            if (!FlatChecker.checkName(flat.getName())) {
                throw new CollectionException("Ошибка в поле Name у объекта " + flat.getName());
            }
            if (!FlatChecker.checkCoordinates(flat.getCoordinates())) {
                throw new CollectionException("Ошибка в поле Coordinates у объекта " + flat.getName());
            }
            if (!FlatChecker.checkCreationDate(flat.getCreationDate())) {
                throw new CollectionException("Ошибка в поле CreationDate у объекта " + flat.getName());
            }
            if (!FlatChecker.checkArea(flat.getArea())) {
                throw new CollectionException("Ошибка в поле Area у объекта " + flat.getName());
            }
            if (!FlatChecker.checkNumberOfRooms(flat.getNumberOfRooms())) {
                throw new CollectionException("Ошибка в поле NumberOfRooms у объекта " + flat.getName());
            }
            if (!FlatChecker.checkNumberOfBathrooms(flat.getNumberOfBathrooms())) {
                throw new CollectionException("Ошибка в поле NumberOfBathrooms у объекта " + flat.getName());
            }
            if (!FlatChecker.checkFurnish(flat.getFurnish())) {
                throw new CollectionException("Ошибка в поле Furnish у объекта " + flat.getName());
            }
            if (!FlatChecker.checkHouse(flat.getHouse())) {
                throw new CollectionException("Ошибка в поле House у объекта " + flat.getName());
            }
        }
    }


    private static class FlatChecker {
        private static final HashSet<Integer> ID_LIST = new HashSet<>();

        private static boolean checkId(int id) {
            if (ID_LIST.contains(id) || id < 1 || id > CollectionManager.MAX_ID) return false;
            ID_LIST.add(id);
            return true;
        }

        private static boolean checkName(String name) {
            if (name != null) return name.matches(FlatReader.PATTERN_NAMES);
            return false;
        }

        private static boolean checkCoordinates(Coordinates coordinates) {
            if (coordinates != null) return CoordinatesChecker.checkCoordinateX(coordinates.getX()) &&
                    CoordinatesChecker.checkCoordinateY(coordinates.getY());
            return false;
        }

        private static boolean checkCreationDate(LocalDateTime date) {
            if (date != null) return date.isBefore(LocalDateTime.now());
            return false;
        }

        private static boolean checkArea(int area) {
            return (area > 0);
        }

        private static boolean checkNumberOfRooms(long numberOfRooms) {
            return (numberOfRooms <= 14 && numberOfRooms > 0);
        }

        private static boolean checkNumberOfBathrooms(long numberOfBathrooms) {
            return (numberOfBathrooms > 0);
        }

        private static boolean checkFurnish(Furnish furnish) {
            return (furnish != null);
        }

        private static boolean checkHouse(House house) {
            if (house != null) return HouseChecker.checkName(house.getName()) &&
                    HouseChecker.checkYear(house.getYear()) &&
                    HouseChecker.checkNumberOfFloors(house.getNumberOfFloors()) &&
                    HouseChecker.checkNumberOfFlatsOnFloor(house.getNumberOfFlatsOnFloor()) &&
                    HouseChecker.checkNumberOfLifts(house.getNumberOfLifts());
            return true;
        }
    }

    private static class CoordinatesChecker {
        private static boolean checkCoordinateX(int x) {
            return (x <= 713);
        }

        private static boolean checkCoordinateY(Integer y) {
            if (y != null) return (y >= -397);
            return false;
        }
    }

    private static class HouseChecker {
        private static boolean checkName(String name) {
            if (name != null) return name.matches(FlatReader.PATTERN_NAMES);
            return false;
        }

        private static boolean checkYear(int year) {
            return (year > 0);
        }

        private static boolean checkNumberOfFloors(Long numberOfFloors) {
            if (numberOfFloors != null) return (numberOfFloors > 0 && numberOfFloors <= 39);
            return true;
        }

        private static boolean checkNumberOfFlatsOnFloor(long numberOfFlatsOnFloor) {
            return (numberOfFlatsOnFloor > 0);
        }

        private static boolean checkNumberOfLifts(Long numberOfLifts) {
            if (numberOfLifts != null) return (numberOfLifts > 0);
            return true;
        }
    }

}
