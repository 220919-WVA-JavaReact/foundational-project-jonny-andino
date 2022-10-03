package com.revature;

import java.io.Console;
import java.util.Scanner;

public class Prompt {

    public static Scanner input = new Scanner(System.in);
    public String ask(String question) {

        System.out.println(question);
        return input.nextLine();
    }

    public String getPass(String question){
        Console cons;

        if ((cons = System.console()) != null){
            char[] pass = cons.readPassword(question);
            return new String(pass);
        }

        return "";
    }
}
