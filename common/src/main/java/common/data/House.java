package common.data;

import java.io.Serializable;

/**
 * Класс - дом объекта класса Flat
 */
public class House implements Comparable<House>, Serializable {
    /*
     * Имя объекта класса. Поле не может быть null
     */
    private String name;
    /*
     * Возраст объекта класса. Значение поля должно быть больше 0
     */
    private int year;
    /*
     * Количество этажей объекта класса. Максимальное значение поля: 39, Значение поля должно быть больше 0
     */
    private Long numberOfFloors;
    /*
     * Количество квартир на этаже объекта класса. Значение поля должно быть больше 0
     */
    private long numberOfFlatsOnFloor;
    /*
     * Количество лифтов объекта класса. Значение поля должно быть больше 0
     */
    private Long numberOfLifts;

    /**
     * Конструктор класса
     *
     * @param name                 имя объекта класса
     * @param year                 возраст объекта класса
     * @param numberOfFloors       количество этажей объекта класса
     * @param numberOfFlatsOnFloor количество квартир на этаже объекта класса
     * @param numberOfLifts        количество лифтов объекта класса
     */
    public House(String name, int year, Long numberOfFloors, long numberOfFlatsOnFloor, Long numberOfLifts) {
        this.name = name;
        this.year = year;
        this.numberOfFloors = numberOfFloors;
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
        this.numberOfLifts = numberOfLifts;
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
     * Метод, присваивающий имя объекта класса
     */
    public void setHouseName(String name) {
        this.name = name;
    }

    /**
     * Метод, возвращающий возраст объекта класса
     *
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * Метод, присваивающий возраст объекта класса
     *
     * @param year количество лет
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Метод, возвращающий количество этажей объекта класса
     *
     * @return numberOfFloors
     */
    public Long getNumberOfFloors() {
        return numberOfFloors;
    }

    /**
     * Метод, присваивающий количество этажей объекта класса
     *
     * @param numberOfFloors количество этажей
     */
    public void setNumberOfFloors(Long numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    /**
     * Метод, возвращающий количество квартир на этаже объекта класса
     *
     * @return numberOfFlatsOnFloor
     */
    public long getNumberOfFlatsOnFloor() {
        return numberOfFlatsOnFloor;
    }

    /**
     * Метод, присваивающий количество квартир на этаже объекта класса
     *
     * @param numberOfFlatsOnFloor количество квартир на этаже
     */
    public void setNumberOfFlatsOnFloor(long numberOfFlatsOnFloor) {
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }

    /**
     * Метод, возвращающий количество лифтов объекта класса
     *
     * @return numberOfLifts
     */
    public Long getNumberOfLifts() {
        return numberOfLifts;
    }

    /**
     * Метод, присваивающий количество лифтов объекта класса
     *
     * @param numberOfLifts количество лифтов
     */
    public void setNumberOfLifts(Long numberOfLifts) {
        this.numberOfLifts = numberOfLifts;
    }

    /**
     * Метод, возвращающий отформатированный вывод полей объекта класса
     *
     * @return поля объекта класса
     */
    @Override
    public String toString() {
        return "House{" +
                "name='" + name + '\'' +
                ", year=" + year +
                ", numberOfFloors=" + numberOfFloors +
                ", numberOfFlatsOnFloor=" + numberOfFlatsOnFloor +
                ", numberOfLifts=" + numberOfLifts +
                '}';
    }

    /**
     * Метод сравнивает 2 дома по количеству этажей, если оно одинаковое, то сравнивает по
     * количеству квартир на этаже.
     *
     * @param o объект для сравнения.
     * @return результат сравнения полей.
     */
    @Override
    public int compareTo(House o) {
        if (this.getNumberOfFloors() == null) return -1;
        if (o.getNumberOfFloors() == null) return 1;
        if ((long) this.numberOfFloors == o.getNumberOfFloors()){
            return Long.compare(this.numberOfFlatsOnFloor, o.getNumberOfFlatsOnFloor());
        }
        return Long.compare(this.numberOfFloors, o.getNumberOfFloors());
    }
}
