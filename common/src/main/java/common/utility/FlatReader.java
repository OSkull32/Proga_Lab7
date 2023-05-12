package common.utility;

import common.data.*;
import common.exceptions.ErrorInScriptException;
import common.exceptions.InvalidValueException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Класс, необходимы для чтения полей объекта Flat
 */
public class FlatReader implements FlatReaderInterface, CoordinatesReaderInterface, HouseReaderInterface {

    /**
     * Константа, хранящее шаблон (регулярное выражение), которому должны
     * соответствовать все поля {@link Flat} типа String
     */
    public static final String PATTERN_NAMES = "^[a-zA-Z0-9_\\-\\s\u0410-\u044f\u0451\u0401]+$";
    // Поле, хранящее ссылку на объект класса типа Console
    private final Scanner userScanner;
    private boolean fileMode;

    /**
     * Конструктор класса, который присваивает console значение, переданное в конструкторе в качестве параметра
     *
     * @param userScanner хранит ссылку на объект типа Scanner
     */
    public FlatReader(Scanner userScanner) {
        this.userScanner = userScanner;
        fileMode = false;
    }

    public boolean askQuestion(String question) throws ErrorInScriptException {
        String finalQuestion = question + " (yes/no):";
        String answer;

        while (true) {
            try {
                if (!fileMode) UserConsole.printCommandTextNext(finalQuestion);
                if (!fileMode) UserConsole.printCommandText("> ");
                answer = userScanner.nextLine().trim();
                if (!fileMode) UserConsole.printCommandTextNext(answer);
                if (!answer.equals("yes") && !answer.equals("no")) throw new InvalidValueException();
                break;
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("Ответ не распознан");
                if (fileMode) throw new ErrorInScriptException();
            } catch (InvalidValueException ex) {
                UserConsole.printCommandError("Ответом может быть только 'yes' или 'no'");
                if (fileMode) throw new ErrorInScriptException();
            } catch (IllegalStateException ex) {
                UserConsole.printCommandError("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return answer.equals("yes");
    }

    public void setFileMode() {
        fileMode = true;
    }

    /**
     * Метод, выполняющий чтение данных из консоли. Ввод полей в определенном порядке
     *
     * @return объект типа Flat
     */
    @Override
    public Flat read() {
        return new Flat(-1, readName(), readCoordinates(), LocalDateTime.now(), readArea(),
                readNumberOfRooms(), readNumberOfBathrooms(), readFurnish(), readView(), readHouse());
    }

    /**
     * Метод, который читает поле name объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля name, уже проверенное на условия допустимости
     */
    @Override
    public String readName() {
        while (true) {
            if (!fileMode) UserConsole.printCommandText("name (not null): ");
            String str = userScanner.nextLine().trim();
            if (str.equals("")) UserConsole.printCommandError("Значение поля не может быть null или пустой строкой");
            else if (!str.matches(PATTERN_NAMES))
                UserConsole.printCommandError("Введенная строка содержит запрещенные символы");
            else return str;
        }
    }

    /**
     * Метод, который читает координаты x и y
     *
     * @return объект типа Coordinates
     */
    @Override
    public Coordinates readCoordinates() {
        return new Coordinates(readCoordinatesX(), readCoordinatesY());
    }

    /**
     * Метод, который читает поле x объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля x, уже проверенное на условия допустимости
     */
    @Override
    public int readCoordinatesX() {
        int x;
        while (true) {
            try {
                if (!fileMode) UserConsole.printCommandText("coordinate x(int & x <= 713): ");
                x = Integer.parseInt(userScanner.nextLine().trim());
                if (x > 713) throw new InvalidValueException();
                else return x;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandTextNext("Координата x должна быть не более 713");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("Число должно быть типа int");
            }
        }
    }

    /**
     * Метод, который читает поле y объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля y, уже проверенное на условия допустимости
     */
    @Override
    public Integer readCoordinatesY() {
        Integer y;
        while (true) {
            try {
                if (!fileMode) UserConsole.printCommandText("coordinate y(integer & not null & y > -397): ");
                y = Integer.parseInt(userScanner.nextLine().trim());
                if (y <= -397) throw new InvalidValueException();
                else return y;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandTextNext("Координата y должна быть больше -397");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("Число должно быть типа Integer и не null");
            }
        }
    }

    /**
     * Метод, который читает поле area объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля area, уже проверенное на условия допустимости
     */
    @Override
    public int readArea() {
        int area;
        while (true) {
            try {
                if (!fileMode) UserConsole.printCommandText("area(int & area > 0): ");
                area = Integer.parseInt(userScanner.nextLine().trim());
                if (area <= 0) throw new InvalidValueException();
                else return area;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandTextNext("Значение area должно быть больше 0");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("Число должно быть типа int");
            }
        }
    }

    /**
     * Метод, который читает поле numberOfRooms объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля numberOfRooms, уже проверенное на условия допустимости
     */
    @Override
    public long readNumberOfRooms() {
        long numberOfRooms;
        while (true) {
            try {
                if (!fileMode) UserConsole.printCommandText("numberOfRooms(long & 0 < numberOfRooms <=14): ");
                numberOfRooms = Long.parseLong(userScanner.nextLine().trim());
                if (numberOfRooms <= 0 || numberOfRooms > 14) throw new InvalidValueException();
                else return numberOfRooms;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandTextNext("Значение numberOfRooms должно быть больше 0 и не более 14");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("Число должно быть типа long");
            }
        }
    }

    /**
     * Метод, который читает поле numberOfBathrooms объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля numberOfBathrooms, уже проверенное на условия допустимости
     */
    @Override
    public long readNumberOfBathrooms() {
        long numberOfBathrooms;
        while (true) {
            try {
                if (!fileMode) UserConsole.printCommandText("numberOfBathrooms(long & numberOfBathrooms > 0): ");
                numberOfBathrooms = Long.parseLong(userScanner.nextLine().trim());
                if (numberOfBathrooms <= 0) throw new InvalidValueException();
                else return numberOfBathrooms;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandTextNext("Значение numberOfBathrooms должно быть больше 0");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("Число должно быть типа long");
            }
        }
    }

    /**
     * Метод, который читает поле furnish объекта Flat из потока, указанного в поле console.
     * Выводит список допустимых констант
     *
     * @return значение поля furnish, уже проверенное на условия допустимости
     */
    @Override
    public Furnish readFurnish() {
        Furnish furnish;
        while (true) {
            try {
                if (!fileMode) {
                    UserConsole.printCommandText("Допустимые значения furnish:\n");
                    for (Furnish val : Furnish.values()) {
                        UserConsole.printCommandText(val.name() + "\n");
                    }
                    UserConsole.printCommandText("furnish: ");
                }
                furnish = Furnish.valueOf(userScanner.nextLine().toUpperCase().trim());
                return furnish;
            } catch (IllegalArgumentException ex) {
                UserConsole.printCommandError("Введенная константа не представлена в допустимых значения Furnish");
            }
        }
    }

    /**
     * Метод, который читает поле view объекта Flat из потока, указанного в поле console.
     * Выводит список допустимых констант
     *
     * @return значение поля view, уже проверенное на условия допустимости
     */
    @Override
    public View readView() {
        View view;
        while (true) {
            try {
                if (!fileMode) {
                    UserConsole.printCommandText("Допустимые значения view:\n");
                    for (View val : View.values()) {
                        UserConsole.printCommandText(val.name() + "\n");
                    }
                    UserConsole.printCommandText("view: ");
                }
                String str = userScanner.nextLine().trim();
                if (str.equals("")) {
                    view = null;
                } else {
                    view = View.valueOf(str.toUpperCase().trim());
                }
                return view;
            } catch (IllegalArgumentException ex) {
                UserConsole.printCommandError("Введенная константа не представлена в допустимых значения View");
            }
        }
    }

    /**
     * Метод, который читает поля House.
     *
     * @return объект типа House
     */
    @Override
    public House readHouse() {
        while (true) {
            if (!fileMode)
                UserConsole.printCommandText("Нажмите enter, чтобы ввести в поле house null или введите House name (not null):");
            String str = userScanner.nextLine().trim();
            if (str.equals(""))
                return null;
            else {
                if (!str.matches(PATTERN_NAMES))
                    UserConsole.printCommandError("Введенная строка содержит запрещенные символы");
                else return new House(str, readHouseYear(), readHouseNumberOfFloors(),
                        readHouseNumberOfFlatsOnFloor(), readHouseNumberOfLifts());
            }
        }
    }

    /**
     * Метод, который читает поле name объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля area, уже проверенное на условия допустимости
     */
    @Override
    public String readHouseName() {
        String str;

        while (true) {
            if (!fileMode) UserConsole.printCommandText("House name (not null): ");
            str = userScanner.nextLine().trim();
            if (str.equals("")) UserConsole.printCommandError("Значение поля не может быть null");
            else if (!str.matches(PATTERN_NAMES))
                UserConsole.printCommandError("Введенная строка содержит запрещенные символы");
            else return str;
        }
    }

    /**
     * Метод, который читает поле year объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля year, уже проверенное на условия допустимости
     */
    @Override
    public int readHouseYear() {
        int houseYear;
        while (true) {
            try {
                if (!fileMode) UserConsole.printCommandText("houseYear(int & houseYear > 0): ");
                houseYear = Integer.parseInt(userScanner.nextLine().trim());
                if (houseYear <= 0) throw new InvalidValueException();
                else return houseYear;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandTextNext("Значение houseYear должно быть больше 0");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("Число должно быть типа int");
            }
        }
    }

    /**
     * Метод, который читает поле numberOfFloors объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля numberOfFloors, уже проверенное на условия допустимости
     */
    @Override
    public Long readHouseNumberOfFloors() {
        Long numberOfFloors;
        while (true) {
            try {
                if (!fileMode) UserConsole.printCommandText("number of floors(long & 0 < numberOfFloors <= 39): ");
                String str = userScanner.nextLine().trim();
                if (str.equals("")) numberOfFloors = null;
                else {
                    numberOfFloors = Long.parseLong(str);
                    if (numberOfFloors <= 0 || numberOfFloors > 39) throw new InvalidValueException();
                }
                return numberOfFloors;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandTextNext("Значение numberOfFloors должно быть больше 0 и не более 39");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("Число должно быть типа Long");
            }
        }
    }

    /**
     * Метод, который читает поле numberOfFlatsOnFloor объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля numberOfFlatsOnFloor, уже проверенное на условия допустимости
     */
    @Override
    public long readHouseNumberOfFlatsOnFloor() {
        long numberOfFlatsOnFloor;
        while (true) {
            try {
                if (!fileMode)
                    UserConsole.printCommandText("number of flats on floor(long & numberOfFlatsOnFloor > 0): ");
                numberOfFlatsOnFloor = Long.parseLong(userScanner.nextLine().trim());
                if (numberOfFlatsOnFloor <= 0) throw new InvalidValueException();
                else return numberOfFlatsOnFloor;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandTextNext("Значение numberOfFlatsOnFloor должно быть больше 0");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("Число должно быть типа long");
            }
        }
    }

    /**
     * Метод, который читает поле numberOfLifts объекта Flat из потока, указанного в поле console.
     *
     * @return значение поля numberOfLifts, уже проверенное на условия допустимости
     */
    @Override
    public Long readHouseNumberOfLifts() {
        Long numberOfLifts;
        while (true) {
            try {
                if (!fileMode) UserConsole.printCommandText("number of lifts(long & numberOfLifts > 0): ");
                String str = userScanner.nextLine().trim();
                if (str.equals("")) numberOfLifts = null;
                else {
                    numberOfLifts = Long.parseLong(str);
                    if (numberOfLifts <= 0) throw new InvalidValueException();
                }
                return numberOfLifts;
            } catch (InvalidValueException ex) {
                UserConsole.printCommandTextNext("Значение numberOfLifts должно быть больше 0");
            } catch (NumberFormatException ex) {
                UserConsole.printCommandError("Число должно быть типа long");
            }
        }
    }

}
