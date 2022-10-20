package main.explodingkittens.io;

import main.explodingkittens.io.message.IMessage;
import main.explodingkittens.io.option.Options;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Console {

    private static Console instance = null;
    Scanner scanner;

    public static Console getInstance()
    {
        if (instance == null)
            instance = new Console();

        return instance;
    }
    private Console() {
        scanner = new Scanner(System.in);
    }

    public void print(String msg) {
        System.out.println(msg);
    }

    public void print(IMessage msg) {
        print(msg.toString());
    }

    public String getString(Options opt) {
        String input = scanner.nextLine();
        if (opt.isKeyInOptions(input)) {
            return input;
        }
        System.out.println("Unknown input [" + input + "], please try again");
        return getString(opt);
    }

    public String getString() {
        return scanner.nextLine();
    }

    public int getInt() {
        return getIntMaxMin(Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    public int getIntMax(int max) {
        return getIntMaxMin(max, Integer.MIN_VALUE);
    }

    public int getIntMin(int min){
        return getIntMaxMin(Integer.MAX_VALUE, min);
    }

    public int getIntMaxMin(int max, int min) {
        int i;
        do {
            try {
                i = scanner.nextInt();
                if (i > max || i < min) {
                    printError();
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                printError();
            }
            scanner.nextLine();
        } while (true);
        return i;
    }

    private void printError() {
        System.out.println("Invalid input, please try again");
    }
}
