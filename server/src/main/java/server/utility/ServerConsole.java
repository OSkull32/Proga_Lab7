package server.utility;

import common.utility.Console;

import java.util.Scanner;

public class ServerConsole implements Console {

    private final StringBuilder builder = new StringBuilder();
    private static StringBuilder stringBuilder = new StringBuilder();

    @Override
    public String readLine() {
        return null;
    }

    @Override
    public void printPreamble() {
        builder.append(">");
    }

    @Override
    public void printCommandTextNext(String text) {
        builder.append(text).append('\n');
    }

    @Override
    public void printCommandText(String text) {
        builder.append(text);
    }

    @Override
    public void printCommandError(String errorText) {
        builder.append("Ошибка: ").append(errorText).append('\n');
    }

    @Override
    public void turnOnScriptMode(Scanner scanner) {

    }

    @Override
    public void turnOffScriptMode() {

    }

    public static String getAndClear() {
        String toReturn = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());
        return toReturn;
    }

    public String getString() {
        return builder.toString();
    }

    public void clear() {
        builder.delete(0, builder.length());
    }
}
