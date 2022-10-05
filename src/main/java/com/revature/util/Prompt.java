package com.revature.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Prompt {

    public static Scanner input = new Scanner(System.in);
    public String ask(String question) {

        System.out.println(question);
        return input.nextLine();
    }

    public String ask(){
        return input.nextLine();
    }

    public static String readPassword (String question) {
        EraserThread et = new EraserThread(question);
        Thread mask = new Thread(et);
        mask.start();

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String password = "";

        try {
            password = in.readLine();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        // stop masking
        et.stopMasking();
        // return the password entered by the user
        return password;
    }
}
