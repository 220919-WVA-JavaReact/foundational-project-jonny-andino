package com.revature.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.controller.Prompt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashMap;

public class AuthUtil {

    private static AuthUtil util = null;

    private AuthUtil(){};

    public static AuthUtil getAuthUtil(){
        return (util == null) ? new AuthUtil() : util;
    }
    private static final ObjectMapper mapper = Prompt.mapper;
    public String getHashedPassword(String pass, String salt){

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

    public static String createSalt() {
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

    public void saveUserSalt(String username, String salt){
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

    public String retrieveUserSalt(String username){
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
