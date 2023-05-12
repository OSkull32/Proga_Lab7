package common.data;

import java.io.Serializable;

/**
 * Класс, содержащий координаты элемента коллекции
 */
public class Coordinates implements Serializable, Comparable<Coordinates> {
    /**
     * Поле координаты x. Максимальное значение поля: 713
     */
    private int x;

    /**
     * Поле координаты y. Значение поля должно быть больше -397, Поле не может быть null
     */
    private Integer y;

    /**
     * Конструктор класса
     *
     * @param x значение координаты x
     * @param y значение координаты y
     */
    public Coordinates(int x, Integer y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Метод, устанавливающий значение координаты x
     *
     * @param x значение координаты x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Метод, устанавливающий значение координаты y
     *
     * @param y значение координаты y
     */
    public void setY(Integer y) {
        this.y = y;
    }

    /**
     * Метод, возвращающий значение координаты x
     *
     * @return значение координаты x
     */
    public int getX() {
        return x;
    }

    /**
     * Метод, возвращающий значение координаты y
     *
     * @return значение координаты y
     */
    public Integer getY() {
        return y;
    }

    /**
     * Метод, возвращающий отформатированный вывод полей объекта класса
     *
     * @return поля объекта класса
     */

    @Override
    public String toString() {
        return "(x,y) = (" + x + "," + y + ")";
    }

    @Override
    public int compareTo(Coordinates o) {
        return x * x + y * y - o.x * o.x - o.y * o.y; //сравнивает по удаленности от (0,0)
    }
}
