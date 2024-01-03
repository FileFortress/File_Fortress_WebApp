package edu.sltc.vaadin.data;

import javax.crypto.spec.DHParameterSpec;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
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
            for (Provider provider : Security.getProviders()) {
                System.out.println("Provider: " + provider.getName() + " version: " + provider.getVersionStr());
            }
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "SunEC");
            ECGenParameterSpec ecParameterSpec = new ECGenParameterSpec(ecdhCurvenameString);
            keyPairGenerator.initialize(ecParameterSpec);
            KeyPair ecdhKeyPair = keyPairGenerator.genKeyPair();
            keyPair = ecdhKeyPair;
            PrivateKey privateKey = ecdhKeyPair.getPrivate();
            PublicKey publicKey = ecdhKeyPair.getPublic();
            System.out.println("privateKey: " + privateKey);
            System.out.println("publicKey: " + publicKey);

        } catch (InvalidAlgorithmParameterException | NoSuchProviderException | NoSuchAlgorithmException e) {
            // Print the stack trace for debugging purposes
            e.printStackTrace();
            throw new RuntimeException("Error generating key pair", e);
        }
    }
    public static KeyPair getInstanceKeyPair() {
        return keyPair;
    }
}

