/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package passwordvaultapp;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 *
 * @author ASUS
 */
public class AuthService {
    private DatabaseManager dbManager;
    
    //Contructor
    public AuthService(Databasemanager dbManager){
        this.dbManager = dbManager;
    }
    
    public byte[] generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
    
    public byte[] hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            return md.digest(password.getBytes());
        }catch(Exception e){
            throw new RuntimeException("Error hasing the password: " + e.getMessage());
        }
    }
    
    public boolean register(String username, String password){
        try {
            byte[] salt = generateSalt();
            byte[] hashedPassword = hashPassword(password, salt);
            
            User user = new User(0, username, hashedPassword, salt);
            return dbManager.saveUser(user); //true if saved suceesfully!
            
        }catch(Exception e){
            System.out.println("Registeration Failed: " + e.getMessage());
        return false;
        }
    }
    
    public boolean verifyPassword(String rawPassword, byte[] storedHash, byte[] salt){
        byte[] testHash = hashPassword(rawPassword, salt);
        return Arrays.equals(storedHash, testHash);
    }
    
    public User login (String username, String password) {
        try {
            User storedUser = dbManager.getUserByUsername(username);
            
            if(storedUser == null){
                System.out.println("User not found.");
                return null;
            }
            
            boolean valid = verifyPassword(password, storedUser.getHashedPassword(), storedUser.getSalt());
            if(valid){
                System.out.println("Login Sucessfully!");
                return storedUser;
            }else {
                System.out.println("Invalid password.");
                return null;
            }
            
        }catch(Exception e) {
            System.out.println("Login Falied: " + e.getMessage());
            return null;
        }
    }
    
}
