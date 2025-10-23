/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package passwordvaultapp;

import java.util.Arrays;

/**
 *
 * @author ASUS
 */
public class User {
    private int userID;
    private String username;
    private byte[] hashedPassword;
    private byte[] salt;
    
    public User(int userID, String username, byte[] hashedPassword, byte[] salt){
        this.salt = salt;
        this.userID = userID;
        this.username = username;
        this.hashedPassword = hashedPassword;
    }
    
    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    

    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(byte[] hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

   // for debugging
    public String toString() {
        return "User{" + 
                "userID=" + userID +
                ", username=" + username + '\'' +
                ", hashedPassword=" + Arrays.toString(hashedPassword) +
                ", salt=" + Arrays.toString(salt) + '}';
    }
    
    
}

