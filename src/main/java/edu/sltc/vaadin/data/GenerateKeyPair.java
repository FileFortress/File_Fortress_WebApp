package edu.sltc.vaadin.data;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 02/01/2024
 * @package edu.sltc.vaadin.data
 * @project_Name File_Fortress_WebApp
 */
public class GenerateKeyPair {
    private static KeyPair keyPair;
    private static String base64EncodedPublicKey;
    private static KeyFactory keyFactory;
    private static final GenerateKeyPair instance = new GenerateKeyPair();
    private GenerateKeyPair() {
        // Private constructor to prevent instantiation
        try {
            String ecdhCurvenameString = "secp256r1";
            // standard curvennames
            // secp256r1 [NIST P-256, X9.62 prime256v1]
            // secp384r1 [NIST P-384]
            // secp521r1 [NIST P-521]
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "SunEC");
            keyFactory = KeyFactory.getInstance("EC");
            ECGenParameterSpec ecParameterSpec = new ECGenParameterSpec(ecdhCurvenameString);
            keyPairGenerator.initialize(ecParameterSpec);
            KeyPair ecdhKeyPair = keyPairGenerator.genKeyPair();
            keyPair = ecdhKeyPair;
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            byte[] rawPublicKey = publicKey.getEncoded();
            base64EncodedPublicKey = Base64.getEncoder().encodeToString(rawPublicKey);
        } catch (InvalidAlgorithmParameterException | NoSuchProviderException | NoSuchAlgorithmException e) {
            // Print the stack trace for debugging purposes
            e.printStackTrace();
            throw new RuntimeException("Error generating key pair", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
    public static KeyPair getInstanceKeyPair() {
        return keyPair;
    }
    public static String getBase64EncodedPublicKey(){return base64EncodedPublicKey;}
    public static SecretKey generateSharedSecret(String based64EncodedString) {
        SecretKey sharedSecret = null;
        byte[] publicKeyBytes = Base64.getDecoder().decode(based64EncodedString);
        // Create an X509EncodedKeySpec from the decoded bytes
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        try {
            // Get the public key from the key specification
            PublicKey clientPublicKey =  keyFactory.generatePublic(keySpec);

            // Create KeyAgreement instance using the Diffie-Hellman algorithm
            KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");

            // Initialize with the server's private key
            keyAgreement.init(keyPair.getPrivate());

            // Generate the shared secret
            keyAgreement.doPhase(clientPublicKey, true);
            byte[] sharedSecretBytes = keyAgreement.generateSecret();

            // Ensure that the shared secret is 256 bits (32 bytes) for AES/GCM-256
            if (sharedSecretBytes.length != 32) {
                throw new RuntimeException("Invalid shared secret length");
            }
            // Convert the shared secret to a SecretKey
            sharedSecret = new SecretKeySpec(sharedSecretBytes, 0, 32, "AES");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException |InvalidKeyException e) {
            // Handle exceptions
            e.printStackTrace();
        }
        return sharedSecret;
    }
    public static void main(String[] args) {
        for (Provider provider : Security.getProviders()) {
            System.out.println("Provider: " + provider.getName() + " version: " + provider.getVersionStr());
        }
    }
}

