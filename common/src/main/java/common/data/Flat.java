package common.data;

import com.google.gson.annotations.JsonAdapter;
import common.utility.LocalDateTimeAdapter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс объектов коллекции
 */
public class Flat implements Comparable<Flat>, Serializable {

    // Идентификатор коллекции. Значение поля должно быть больше 0,
    // Значение этого поля должно быть уникальным,
    // Значение этого поля должно генерироваться автоматически
    private int id;

    // Имя объекта класса. Поле не может быть null, Строка не может быть пустой
    private String name;

    // Координаты объекта класса. Поле не может быть null
    private Coordinates coordinates;

    // Время создания объекта класса. Поле не может быть null,
    // Значение этого поля должно генерироваться автоматически
    @JsonAdapter(LocalDateTimeAdapter.class) //адаптировать для сериализации
    private LocalDateTime creationDate;

    //Площадь объекта класса. Значение поля должно быть больше 0
    private int area;

    // Количество комнат объекта класса. Максимальное значение поля: 14,
    // Значение поля должно быть больше 0
    private long numberOfRooms;

    // Количество уборных объекта класса. Значение поля должно быть больше 0
    private long numberOfBathrooms;

    // Отделка объекта класса. Поле не может быть null
    private Furnish furnish;

    // Вид из объекта класса. Поле может быть null
    private View view;

    // Дом объекта класса. Поле может быть null
    private House house;

    /**
     * Конструктор объекта класса
     *
     * @param name              Имя объекта класса
     * @param coordinates       Координаты объекта класса
     * @param creationDate      Время создания объекта класса
     * @param area              Площадь объекта класса
     * @param numberOfRooms     Количество комнат объекта класса
     * @param numberOfBathrooms Количество уборных объекта класса
     * @param furnish           Отделка объекта класса
     * @param view              Вид из объекта класса
     * @param house             Дом объекта класса
     */
    public Flat(int id, String name, Coordinates coordinates, LocalDateTime creationDate, int area,
                long numberOfRooms, long numberOfBathrooms, Furnish furnish, View view, House house) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.furnish = furnish;
        this.view = view;
        this.house = house;
    }

    public Flat(String name, Coordinates coordinates, int area, long numberOfRooms, long numberOfBathrooms, Furnish furnish, View view, House house) {
        this.name = name;
        this.coordinates = coordinates;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.furnish = furnish;
        this.view = view;
        this.house = house;
        this.creationDate = LocalDateTime.now();
    }

    /**
     * Метод, возвращающий имя объекта класса
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Метод, возвращающий координаты объекта класса
     *
     * @return coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Метод, возвращающий время создания объекта класса
     *
     * @return creationDate
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Метод, возвращающий площадь объекта класса
     *
     * @return area
     */
    public int getArea() {
        return area;
    }

    /**
     * Метод, возвращающий число комнат объекта класса
     *
     * @return numberOfRooms
     */
    public long getNumberOfRooms() {
        return numberOfRooms;
    }

    /**
     * Метод, возвращающий число уборных объекта класса
     *
     * @return numberOfBathrooms
     */
    public long getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    /**
     * Метод, возвращающий отделку объекта класса
     *
     * @return furnish
     */
    public Furnish getFurnish() {
        return furnish;
    }

    /**
     * Метод, возвращающий вид из объекта класса
     *
     * @return view
     */
    public View getView() {
        return view;
    }

    /**
     * Метод, возвращающий дом объекта класса
     *
     * @return house
     */
    public House getHouse() {
        return house;
    }

    /**
     * Метод, возвращающий идентификатор объекта класса
     *
     * @return id идентификатор
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Метод, присваивающий координаты объекту класса
     *
     * @param coordinates координаты
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Метод, присваивающий координату x объекту класса
     *
     * @param x координата х
     */
    public void setCoordinateX(int x) {
        this.getCoordinates().setX(x);
    }

    /**
     * Метод, присваивающий координату y объекту класса
     *
     * @param y координата у
     */
    public void setCoordinateY(Integer y) {
        this.getCoordinates().setY(y);
    }

    /**
     * Метод, изменяющий дату создания объекта на текущий момент
     */
    public void updateCreationDateToNow() {
        this.creationDate = LocalDateTime.now();
    }

    /**
     * Метод, присваивающий площадь объекту класса
     *
     * @param area значение площади
     */
    public void setArea(int area) {
        this.area = area;
    }

    /**
     * Метод, присваивающий количество комнат объекту класса
     *
     * @param numberOfRooms количество комнат
     */
    public void setNumberOfRooms(long numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    /**
     * Метод, присваивающий количество уборных объекту класса
     *
     * @param numberOfBathrooms количество уборных
     */
    public void setNumberOfBathrooms(long numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    /**
     * Метод, присваивающий отделку объекту класса
     *
     * @param furnish вид отделки
     */
    public void setFurnish(Furnish furnish) {
        this.furnish = furnish;
    }

    /**
     * Метод, присваивающий вид из окна объекту класса
     *
     * @param view тип вида из окна
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Метод, присваивающий дом объекту класса
     *
     * @param house тип вида из окна
     */
    public void setHouse(House house) {
        this.house = house;
    }

    /**
     * Метод, присваивающий имя дому объекта класса
     *
     * @param name название дома
     */
    public void setHouseName(String name) {
        this.house.setHouseName(name);
    }

    /**
     * Метод, присваивающий год постройки дому объекта класса
     *
     * @param year год постройки дома
     */
    public void setHouseYear(int year) {
        this.house.setYear(year);
    }

    /**
     * Метод, присваивающий количество этажей дому объекта класса
     *
     * @param numberOfFloors количество этажей
     */
    public void setHouseNumberOfFloors(Long numberOfFloors) {
        this.house.setNumberOfFloors(numberOfFloors);
    }

    /**
     * Метод, присваивающий количество квартир на одном этаже дому объекта класса
     *
     * @param numberOfFlatsOnFloor количество квартир на одном этаже
     */
    public void setHouseNumberOfFlatsOnFloor(long numberOfFlatsOnFloor) {
        this.house.setNumberOfFlatsOnFloor(numberOfFlatsOnFloor);
    }

    /**
     * Метод, присваивающий количество лифтов дому объекта класса
     *
     * @param numberOfLifts количество лифтов
     */
    public void setHouseNumberOfLifts(long numberOfLifts) {
        this.house.setNumberOfLifts(numberOfLifts);
    }

    /**
     * Метод, возвращающий отформатированный вывод полей объекта класса
     *
     * @return поля объекта класса
     */
    @Override
    public String toString() {
        return "Flat{" +
                "\nid=" + id +
                ", \nname='" + name + '\'' +
                ", \ncoordinates=" + coordinates +
                ", \ncreationDate=" +
                creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) +
                ", \narea=" + area +
                ", \nnumberOfRooms=" + numberOfRooms +
                ", \nnumberOfBathrooms=" + numberOfBathrooms +
                ", \nfurnish=" + furnish +
                ", \nview=" + view +
                ", \nhouse=" + house +
                '}';
    }

    @Override
    public int compareTo(Flat o) {
        return this.getId() - o.getId();
    }
}
