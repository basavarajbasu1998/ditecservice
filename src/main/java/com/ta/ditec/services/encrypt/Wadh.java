package com.ta.ditec.services.encrypt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Wadh {

    public String getWADH(String ts, String ver, String ra, String rc, String lr, String de, String pfr) throws NoSuchAlgorithmException {
        // Concatenate the elements
        String concatenatedString = ts + ver + ts + ra + rc + lr + de + pfr;
        
        // Hash the concatenated string using SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(concatenatedString.getBytes(StandardCharsets.UTF_8));
        
        // Encode the hash using Base64
        String wadh = Base64.getEncoder().encodeToString(hash);
        
        return wadh; // Use this WADH
    }
    
    public static void main(String[] args) {
        try {
            Wadh wadh = new Wadh(); // Create an instance of Wadh
            
            // Get the current timestamp
            long timestamp = System.currentTimeMillis();
            String ts = String.valueOf(timestamp);
            
            // Sample values
            String ver = "2.5";
            String ra = "F";
            String rc = "Y";
            String lr = "N";
            String de = "N";
            String pfr = ""; // Not mentioned in the requirement
            
            // Calculate the WADH hash
            String hashedText = wadh.getWADH(ts, ver, ra, rc, lr, de, pfr);
            
            // Output the hash
            System.out.println("WADH: " + hashedText);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
