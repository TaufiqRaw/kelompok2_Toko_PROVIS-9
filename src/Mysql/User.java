/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysql;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Taufiq
 */
public class User {
    private int id;
    private String username;
    public User(){
        this.id = 0;
    }
    public User(int _id, String _username){
        this.id = _id;
        this.username = _username;
    }
    public String getUsername(){
        return username;
    }
    public int getId(){
        return id;
    }
    public boolean isAuth(){
        if(this.id == 0)
            return false;
        return true;
    }
    
    public static User login(String username, String password){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db10120764Toko", "root", "");
            
            Statement st = conn.createStatement();
            
            String sql = "SELECT * FROM users WHERE username=\""+username+"\"";
            
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                Object[] obj = new Object[1];
                obj[0] = rs.getString("password");
                if(User.compare(password, (String)obj[0])){
                    int loggedId = Integer.parseInt(rs.getString("id"));
                    String loggedUsername = rs.getString("username");
                    return new User(loggedId, loggedUsername);
                }
            }
            rs.close();
            st.close();
            conn.close();
            return new User();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        return new User();
    }
    
    public static String getHashed(String pass){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
            
            return myHash;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        return "";
    }
    public static boolean compare(String pass, String encrypted){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        
            return myHash.equals(encrypted);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
