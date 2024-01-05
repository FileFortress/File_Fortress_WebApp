package edu.sltc.vaadin.data;

import javax.crypto.KeyAgreement;
import javax.crypto.spec.DHParameterSpec;
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
    private static final GenerateKeyPair instance = new GenerateKeyPair();
    //    private GenerateKeyPair() {
//        // Private constructor to prevent instantiation
//        try {
//            // Create a Diffie-Hellman key pair generator
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DiffieHellman");
//            // Generate the Diffie-Hellman parameters (you may need to adjust the key size)
//            AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DiffieHellman");
//            paramGen.init(2048); // Adjust key size as needed
//            AlgorithmParameters params = paramGen.generateParameters();
//            DHParameterSpec dhSpec = params.getParameterSpec(DHParameterSpec.class);
//
//            // Initialize the key pair generator with the Diffie-Hellman parameters
//            keyPairGenerator.initialize(dhSpec);
//            keyPair = keyPairGenerator.generateKeyPair();
//        } catch (InvalidAlgorithmParameterException | InvalidParameterSpecException | NoSuchAlgorithmException e) {
//            // Print the stack trace for debugging purposes
//            e.printStackTrace();
//            throw new RuntimeException("Error generating key pair", e);
//        }
//    }
    private GenerateKeyPair() {
        // Private constructor to prevent instantiation
        try {
            String ecdhCurvenameString = "secp256r1";
            // standard curvennames
            // secp256r1 [NIST P-256, X9.62 prime256v1]
            // secp384r1 [NIST P-384]
            // secp521r1 [NIST P-521]

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "SunEC");
            ECGenParameterSpec ecParameterSpec = new ECGenParameterSpec(ecdhCurvenameString);
            keyPairGenerator.initialize(ecParameterSpec);
            KeyPair ecdhKeyPair = keyPairGenerator.genKeyPair();
            keyPair = ecdhKeyPair;
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            byte[] rawPublicKey = publicKey.getEncoded();
            base64EncodedPublicKey = Base64.getEncoder().encodeToString(rawPublicKey);

            PrivateKey privateKey = ecdhKeyPair.getPrivate();
            PublicKey publicKeyOld = ecdhKeyPair.getPublic();
            System.out.println("privateKey: " + privateKey);
            System.out.println("publicKey Old: " + publicKeyOld);
            System.out.println("publicKey: " + publicKey);
            System.out.println("Encoded String: " + base64EncodedPublicKey);
            byte[] publicKeyBytes = publicKeyOld.getEncoded();
            String base64EncodedPublicKey1 = Base64.getEncoder().encodeToString(publicKeyBytes);
            System.out.println("Encoded StringOld: " + base64EncodedPublicKey1);
            byte[] publicKeyBytes1 = Base64.getDecoder().decode(base64EncodedPublicKey);
            // Create an X509EncodedKeySpec from the decoded bytes
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes1);
            // Get the public key from the key specification
            System.out.println(keyFactory.generatePublic(keySpec));
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
    public static String generateSharedSecret(PublicKey clientPublicKey) {
        String sharedSecret = "";
        try {
            // Create KeyAgreement instance using the Diffie-Hellman algorithm
            KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH");

            // Initialize with the server's private key
            keyAgreement.init(keyPair.getPrivate());

            // Generate the shared secret
            keyAgreement.doPhase(clientPublicKey, true);
            byte[] sharedSecretBytes = keyAgreement.generateSecret();

            // Convert the shared secret to a base64-encoded string
            sharedSecret = Base64.getEncoder().encodeToString(sharedSecretBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
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

