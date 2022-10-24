package main.explodingkittens.io;

import main.explodingkittens.util.message.IMessage;
import main.explodingkittens.util.option.Options;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Console {

    private static Console instance = null;
    Scanner scanner;

    /**
     * Create or get the singleton instance of the Console
     *
     * @return the console
     */
    public static Console getInstance() {
        if (instance == null)
            instance = new Console();

        return instance;
    }

    private Console() {
        scanner = new Scanner(System.in);
    }

    /**
     * The print method
     *
     * @param msg the msg to print
     */
    public void print(String msg) {
        System.out.println(msg);
    }

    /**
     * The print method
     *
     * @param msg the msg to print
     */
    public void print(IMessage msg) {
        print(msg.toString());
    }

    /**
     * Get a string input from a list of options
     *
     * @param options the possible input options
     * @return the string input
     */
    public String getString(Options options) {
        String input = scanner.nextLine();
        if (options.isKeyInOptions(input)) {
            return input;
        }
        if (!input.equals("")) System.out.println("Unknown input [" + input + "], please try again");
        return getString(options);
    }

    /**
     * Get the next input
     *
     * @return the input string
     */
    public String getString() {
        return scanner.nextLine();
    }

    /**
     * Get any integer input
     *
     * @return the integer
     */
    public int getInt() {
        return getIntMaxMin(Integer.MAX_VALUE, Integer.MIN_VALUE);
    }


    /**
     * Get any integer input with a max
     *
     * @return the integer
     */
    public int getIntMax(int max) {
        return getIntMaxMin(max, Integer.MIN_VALUE);
    }


    /**
     * Get any integer input with a min
     *
     * @return the integer
     */
    public int getIntMin(int min) {
        return getIntMaxMin(Integer.MAX_VALUE, min);
    }


    /**
     * Get an integer input from min to max
     *
     * @return the integer
     */
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
