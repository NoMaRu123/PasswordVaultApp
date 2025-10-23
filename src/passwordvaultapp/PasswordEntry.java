/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package passwordvaultapp;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author ASUS
 */
public class PasswordEntry {
    private int entryID;
    private int userID; // Foreign key to User
    private String serviceName;
    private String username; // username for that service
    private byte[] encryptedPassword;
    private Date creationDate;

    public PasswordEntry(int entryID, int userID, String serviceName, String username, byte[] encryptedPassword, Date creationDate) {
        this.entryID = entryID;
        this.userID = userID;
        this.serviceName = serviceName;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.creationDate = creationDate;
    }
    
    public int getEntryID() {
        return entryID;
    }

    public int getUserID() {
        return userID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getEncryptedPassword() {
        return encryptedPassword;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String decryptPassword(EncryptionService encryptionService, byte[] key){
        return encryptionService.decrypt(this.encryptedPassword, key);
    }

    public void encryptedPassword(EncryptionService encryptionService, String plainPassword, byte[] key) {
        this.encryptedPassword = encryptionService.encrypt(plainPassword, key);
    }
    
    // for debugging
    
    @Override
    public String toString() {
        return "PasswordEntry{" +
                ", entryID=" + entryID +
                ", userID=" + userID +
                ", serviceName=" + serviceName +'\'' +
                ", username=" + username + '\'' +
                ", encryptedpassword=" + Arrays.toString(encryptedPassword) +
                ", creationDate=" + creationDate +
                '}';
    }
}
