package Mysql;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.bind.DatatypeConverter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Taufiq
 */
public class Auth {
    public static boolean login(String username, String password){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db10120764Toko", "root", "");
            
            Statement st = conn.createStatement();
            
            String sql = "SELECT username FROM users where username="+username;
            
            ResultSet rs = st.executeQuery(sql);
            
            while(rs.next()){
                Object[] obj = new Object[1];
                obj[0] = rs.getString("password");
                if(Auth.compare(password, (String)obj[0]))
                    return true;
            }
            rs.close();
            st.close();
            conn.close();
        }catch(Exception e){
            return false;
        }
        return false;
    }
    
    public static String getHashed(String pass) throws NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(pass.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
            .printHexBinary(digest).toUpperCase();
        
        return myHash;
    }
    
    private static boolean compare(String pass, String encrypted){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        
            return myHash.equals(encrypted);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Auth.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
