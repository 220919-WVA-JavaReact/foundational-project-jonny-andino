package com.revature;

public class Main {

    public static void main(String[] args) {
        Prompt userPrompt = new Prompt();
        String uname = userPrompt.ask("Please input a username.");
        System.out.println("Welcome back, " + uname + "!");
        String pass = userPrompt.getPass("Please enter your Password.");
        System.out.println(pass);
    }
}
