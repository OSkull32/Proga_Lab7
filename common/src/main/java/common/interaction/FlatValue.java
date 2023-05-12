package common.interaction;

import common.data.Coordinates;
import common.data.Furnish;
import common.data.House;
import common.data.View;

import java.io.Serializable;
import java.util.Objects;


/**
 * Класс для получения значения квартиры
 */
public class FlatValue implements Serializable {

    private String name;
    private Coordinates coordinates;
    private int area;
    private long numberOfRooms;
    private long numberOfBathrooms;
    private Furnish furnish;
    private View view;
    private House house;

    public FlatValue(String name, Coordinates coordinates, int area, long numberOfRooms, long numberOfBathrooms, Furnish furnish, View view, House house) {
        this.name = name;
        this.coordinates = coordinates;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.furnish = furnish;
        this.view = view;
        this.house = house;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getArea() {
        return area;
    }

    public long getNumberOfRooms() {
        return numberOfRooms;
    }

    public long getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public Furnish getFurnish() {
        return furnish;
    }

    public View getView() {
        return view;
    }

    public House getHouse() {
        return house;
    }

    @Override
    public String toString() {
        return "FlatValue{" +
                "name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", area=" + area +
                ", numberOfRooms=" + numberOfRooms +
                ", numberOfBathrooms=" + numberOfBathrooms +
                ", furnish=" + furnish +
                ", view=" + view +
                ", house=" + house +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FlatValue flatValue)) return false;
        return getArea() == flatValue.getArea() && getNumberOfRooms() == flatValue.getNumberOfRooms()
                && getNumberOfBathrooms() == flatValue.getNumberOfBathrooms() &&
                Objects.equals(getName(), flatValue.getName()) &&
                Objects.equals(getCoordinates(), flatValue.getCoordinates()) &&
                getFurnish() == flatValue.getFurnish() && getView() == flatValue.getView() &&
                Objects.equals(getHouse(), flatValue.getHouse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getCoordinates(), getArea(), getNumberOfRooms(), getNumberOfBathrooms(), getFurnish(), getView(), getHouse());
    }
}
