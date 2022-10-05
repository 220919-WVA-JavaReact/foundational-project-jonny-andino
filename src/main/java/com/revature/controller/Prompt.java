package com.revature.controller;

import java.util.Scanner;

public class Prompt {

    public static Scanner input = new Scanner(System.in);
    public String ask(String question) {

        System.out.println(question);
        return input.nextLine();
    }

    public static String ask(){
        return input.nextLine();
    }
}
