package client.utility;

import client.App;
import common.exceptions.WrongArgumentException;
import common.utility.UserConsole;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Запрашивает у пользователя логин и пароль.
 */
public class AuthenticationAsker {
    private Scanner userScanner;

    public AuthenticationAsker(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    public String askLogin() {
        String login;
        while (true) {
            try {
                UserConsole.printCommandTextNext("Введите логин:");
                UserConsole.printCommandText(App.PS2);
                login = userScanner.nextLine().trim();
                if (login.equals("")) throw new WrongArgumentException();
                if (login.length() > 10 || login.length() < 4) throw new WrongArgumentException();
                break;
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("Данного логина не существует");
            } catch (WrongArgumentException ex) {
                UserConsole.printCommandError("Логин не может быть пустым и должен состоять от 4 до 10 символов");
            } catch (IllegalArgumentException ex) {
                UserConsole.printCommandError("Ошибка при вводе логина");
                System.exit(0);
            }
        }
        return login;
    }

    public String askPassword() {
        String password;
        while (true) {
            try {
                UserConsole.printCommandTextNext("Введите пароль:");
                UserConsole.printCommandText(App.PS2);
                password = userScanner.nextLine().trim();
                if (password.equals("")) throw new WrongArgumentException();
                if (password.length() > 10 || password.length() < 4) throw new WrongArgumentException();
                if (!password.matches("\\S*")) throw new WrongArgumentException();
                break;
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("Неверный пароль");
            } catch (IllegalArgumentException ex) {
                UserConsole.printCommandError("Ошибка при вводе пароля");
                System.exit(0);
            } catch (WrongArgumentException ex) {
                UserConsole.printCommandError("Пароль не может быть пустым и должен состоять от 4 до 10 символов, также не должен содержать пробелов");
            }
        }
        return password;
    }

    public boolean askQuestion(String question) {
        String finalQuestion = question + "(yes/no):";
        String answer;
        while (true) {
            try {
                UserConsole.printCommandTextNext(finalQuestion);
                UserConsole.printCommandText(App.PS2);
                answer = userScanner.nextLine().trim();
                if (!answer.equals("yes") && !answer.equals("no")) throw new WrongArgumentException();
                break;
            } catch (NoSuchElementException ex) {
                UserConsole.printCommandError("Не распознан ответ");
            } catch (WrongArgumentException ex) {
                UserConsole.printCommandError("Ответ должен быть 'yes' или 'no'.");
            } catch (IllegalArgumentException ex) {
                UserConsole.printCommandError("Возникла ошибка, когда задавали вопрос");
                System.exit(0);
            }
        }
        return answer.equals("yes");
    }
}
