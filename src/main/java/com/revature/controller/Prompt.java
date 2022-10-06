package com.revature.controller;

import java.util.Scanner;

public class Prompt {
    private static Prompt p = null;

    private Prompt(){};

    public static Prompt getPrompt(){
        return (p == null) ? new Prompt() : p;
    }
    private static final Scanner input = new Scanner(System.in);

    public static String ask(){
        return input.nextLine();
    }

    public String ask(String question) {
        System.out.println(question);
        return input.nextLine();
    }

    public void say(String msg){
        System.out.println(msg);
    }

    public void label(String label){
        System.out.println("---------" + label + "---------");
    }
}
