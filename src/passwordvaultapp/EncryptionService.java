/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package passwordvaultapp;

// This class will be uses by PasswordEntry Class(behind the scene)

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionService {
   
    private SecretKey secretKey; // The Encryption Key, 
                              // -> which derrived from the user's master Key
    
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    
    public SecretKey  generateKey (String masterPassword) {
        try {
            byte[] salt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);
            
            KeySpec spec = new PBEKeySpec(masterPassword.toCharArray(), salt, 65536, 128);
            
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            
            byte[] keyBytes = keyFactory.generateSecret(spec).getEncoded();
            this.secretKey = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
            
            return this.secretKey;
            
        } catch(Exception e) {
            throw new RuntimeException("Error generating a secure encryption key: " + e.getMessage());
        }   
    }
    
    public byte[] encrypt(String plainText, SecretKey key){
        try {
            // "Give me a cipher that uses AES in CBC mode with PKCS5 padding"
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] iv = new byte[16]; //initialization vector for CBC mode
            new SecureRandom().nextBytes(iv);
            
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            byte[] combined = new byte[iv.length + encrypted.length];
            
            System.arraycopy(iv, 0, key, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
            
            return combined;
            
        }catch(Exception e){
            throw new RuntimeException("Error while trying Encrption: " + e.getLocalizedMessage());
        }
    }
            
    // This decrypt class will return Byte back to plaintext
    public String decrypt(byte[] cipherText, SecretKey key){
        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] iv = new byte[16];
            System.arraycopy(cipherText, 0, iv, 0, iv.length); //Extract the IV that has been stored in the encrypt
            
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            byte[] actualCipher = new byte[cipherText.length - iv.length];
            
            System.arraycopy(cipher, 0, actualCipher, 0, actualCipher.length);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            
            byte[] decrypted = cipher.doFinal(actualCipher);
            return new String (decrypted, StandardCharsets.UTF_8);
        
            
        }catch(Exception e){
            throw new RuntimeException("Error while trying to decrypt: " + e.getMessage());
        }
    }
}
