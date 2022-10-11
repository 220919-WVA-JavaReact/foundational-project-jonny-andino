package com.revature.controller;

import com.google.gson.Gson;

import java.util.Scanner;

public class Prompt {
    private static Prompt p = null;

    private Prompt(){};

    public static Prompt getPrompt(){
        return (p == null) ? new Prompt() : p;
    }
    private static final Scanner input = new Scanner(System.in);

    private static final Gson json = new Gson();

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

    public String jsonify(Object artifact){
        return json.toJson(artifact);
    }

    public void clear(){
        System.out.print("Everything on the console will cleared");
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
