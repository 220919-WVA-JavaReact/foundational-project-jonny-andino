package com.revature.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.controller.Prompt;
import com.revature.dao.UserDAO;
import com.revature.dao.UserDAOImpl;
import com.revature.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashMap;


public class UserServiceAPI {

    private static final Prompt prompt = Prompt.getPrompt();
    private static final ObjectMapper mapper = Prompt.mapper;

    public User login(String username, String password) {
        UserDAO userDao = new UserDAOImpl();
        User u = userDao.getByUsername(username);

        String salt = retrieveUserSalt(username);
        String encodedPass = getHashedPassword(password, salt);

        System.out.println(encodedPass);

        if (u != null && u.getPassword().equals(encodedPass)) {
            return u;
        }

        return null;
    }

    public User register(String username, String password) {
        UserDAO userDao = new UserDAOImpl();

        User foundUser = userDao.getByUsername(username);

        if (foundUser == null){
            String salt = createSalt();
            saveUserSalt(username, salt);
            String encodedPass = getHashedPassword(password, salt);
            return userDao.registerNewUser(username, encodedPass);
        }
        return null;
    }

    private String getHashedPassword(String pass, String salt){

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(pass.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return pass;
    }

    private static String createSalt() {
        // Always use a SecureRandom generator
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }

        // Create array for salt
        byte[] salt = new byte[16];

        // Get a random salt
        sr.nextBytes(salt);

        // return salt
        return salt.toString();
    }

    private void saveUserSalt(String username, String salt){
        Path path = Paths.get("src/main/resources/user-keys.json");
        String fileContent = null;
        try {
            fileContent = new String(Files.readAllBytes(path));
            HashMap<String, Object> contentMap = mapper.readValue(fileContent, HashMap.class);

            contentMap.put(username, salt);
            mapper.writeValue(path.toFile(), contentMap);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String retrieveUserSalt(String username){
        Path path = Paths.get("src/main/resources/user-keys.json");
        String fileContent = null;
        try {
            fileContent = new String(Files.readAllBytes(path));
            HashMap<String, Object> contentMap = mapper.readValue(fileContent, HashMap.class);

            return (String) contentMap.get(username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
