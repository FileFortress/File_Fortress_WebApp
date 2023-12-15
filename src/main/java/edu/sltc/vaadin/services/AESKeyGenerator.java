package edu.sltc.vaadin.services;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 15/12/2023
 * @package edu.sltc.vaadin.services
 * @project_Name File_Fortress_WebApp
 */
public class AESKeyGenerator {

        private static final AESKeyGenerator instance = new AESKeyGenerator();
        private final SecretKey secretKey;

        private AESKeyGenerator() {
            // Initialize the secret key during construction
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(192);
                this.secretKey = keyGenerator.generateKey();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Error initializing key generator", e);
            }
        }

        public static AESKeyGenerator getInstance() {
            return instance;
        }

        public SecretKey getSecretKey() {
            return secretKey;
        }

        // Helper method to convert bytes to a hex string
        private static String bytesToHex(byte[] bytes) {
            StringBuilder hexStringBuilder = new StringBuilder(2 * bytes.length);
            for (byte aByte : bytes) {
                hexStringBuilder.append(String.format("%02X", aByte));
            }
            return hexStringBuilder.toString();
        }

        public static void main(String[] args) {
            // Access the singleton instance and get the secret key
            AESKeyGenerator keyGeneratorSingleton = AESKeyGenerator.getInstance();
            SecretKey secretKey = keyGeneratorSingleton.getSecretKey();

            // Print the key bytes (you may convert them to a hex string or use as needed)
            byte[] keyBytes = secretKey.getEncoded();
            System.out.println("Generated 24-byte key: " + bytesToHex(keyBytes));
        }

}
