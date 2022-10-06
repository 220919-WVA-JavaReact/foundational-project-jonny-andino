package com.revature.controller;

import java.util.Scanner;

public class Prompt {
    private static Prompt p = null;
    private Prompt(){};
    public static Prompt getPrompt(){
        if (p == null) {
            p = new Prompt();
        }
        return p;
    }
    private static final Scanner input = new Scanner(System.in);
    public String ask(String question) {

        System.out.println(question);
        return input.nextLine();
    }

    public void say(String msg){
        System.out.println(msg);
    }

    public static String ask(){
        return input.nextLine();
    }
}
