/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package passwordvaultapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:PasswordVault.db";
    private Connection connection;
    
    //Constructure
    public DatabaseManager (){
        connect();
        createTable();
    }
    
    //Connect To SQLite
    private void connect() {
        try{
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to SQLite Database!");
            
        }catch(SQLException e){
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
    
    
    //Create tables if they havent been created already..
    private void createTable() {
        String createUserTable = """
                                 CREATE TABLE IF NOT EXISTS Users (
                                    userID INTEGER PRIMARY KEY AUTOINCREMENT,
                                    username TEXT UNIQUE NOT NULL,
                                    hashedPassword BLOB NOT NULL,
                                    salt BLOB NOT NULL
                                 );
                                 """;
        
        String createPasswordEntriesTable = """
                                            CREATE TABLE IF NOT EXISTS PasswordEntries,
                                                entryID INTEGER PRIMARY KEY AUTOINCREMENT,
                                                userID INTEGER NOT NULL,
                                                serviceName TEXT NOT NULL,
                                                username TEXT NOT NULL,
                                                encryptedPassword BLOB NOT NULL,
                                                creationDate TEXT,
                                                FOREIGN KEY (userID) REFERENCES Users(userID)
                                            );
                                            """;
        
        try(Statement stmt = connection.createStatement()) {
            stmt.execute(createUserTable);
            stmt.execute(createPasswordEntriesTable);
            System.out.println("Table verified or created successfully.");
            
        }catch(SQLException e){
            System.out.println("Error while creating Tables! " + e.getMessage());
        }
    }
        
    //For saving new User
    public boolean saveUser(User user) {
        String sql = "INSERT INTO Users (username, hashedPassword, salt) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, user.getUsername());
            pstmt.setBytes(2, user.getHashedPassword());
            pstmt.setBytes(3, user.getSalt());
            pstmt.executeUpdate();
            System.out.println("User saved!: " + user.getUsername());
            return true;
        }catch(SQLException e) {
            System.out.println("Error saving User: " + e.getMessage());
            return false;
        }
    }
    
    //Fetch User by username
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int userID = rs.getInt("userID");
                String uname = rs.getString("username");
                byte[] hashedPassword = rs.getBytes("hashedPassword");
                byte[] salt = rs.getBytes("salt");
                return new User(userID, uname, hashedPassword, salt);
            }
            
        }catch (SQLException e) {
            System.out.println("Error while trying to fetch user: " + e.getMessage());
        }
        return null;
    }
    
    // Save a password Entry
    public boolean savePassword(PasswordEntry entry) {
        String sql = "INSERT INTO PasswordEntries (userID, serviceName, username, encryptedPassword, creationDate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, entry.getUserID());
            pstmt.setString(2, entry.getServiceName());
            pstmt.setString(3, entry.getUsername());
            pstmt.setBytes(4, entry.getEncryptedPassword());
            pstmt.setString(5, entry.getCreationDate().toString());
            pstmt.executeUpdate();
            System.out.println("Password entry has been saved for service: " + entry.getServiceName());
            return true;
        }catch(SQLException e){
            System.err.println("Error saving password: " + e.getMessage());
            return false;
        }
    }
        
        
    //Fetch all password Entries for a given userID
    public List<PasswordEntry> getPasswordByUserID(int userID) {
        List<PasswordEntry> entries = new ArrayList<>();
        
        String sql = "SELECT * FROM PasswordEntries WHERE userID = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int entryID = rs.getInt("entryID");
                String serviceName = rs.getString("serviceName");
                String username = rs.getString("serviceName");
                byte[] encryptedPassword = rs.getBytes("encryptedPassword");
                String dataStr = rs.getString("creationDate");
                
                PasswordEntry entry = new PasswordEntry(entryID, userID, serviceName, username, encryptedPassword, Date.valueOf(dataStr));
                entries.add(entry);
            }
            
        }catch(SQLException e){
            System.err.println("Error fetching password: " + e.getMessage());
        }
        
        return entries;
    }
    
    // Close connection
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("");
            }
        } catch (SQLException e){
            System.err.println("Error closing database: " + e.getMessage());
        }
    }
    
}
