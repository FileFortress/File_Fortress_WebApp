package edu.sltc.vaadin.services;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import edu.sltc.vaadin.views.login.LoginView;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public class FileEncryptionService {

//    private static final String ALGORITHM = "AES";
//    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
//    private static final SecretKey SECRET_KEY = AESKeyGenerator.getInstance().getSecretKey();
//    public static void encryptFile(InputStream inputStream, String outputFilePath) throws RuntimeException{
//        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath)) {
//            Key secretKey = new SecretKeySpec(SECRET_KEY.getEncoded(), ALGORITHM);
//            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//            try (CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher)) {
//                System.out.println("Encrypted File Received!!");
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) >= 0) {
//                    cipherOutputStream.write(buffer, 0, bytesRead);
//                }
//            }
//        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
//            e.printStackTrace();
//        }
//    }

    public static void decryptFile(InputStream inputStream, String outputFilePath, SecretKey sharedSecret) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath)) {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, new byte[12]); // Assuming 128-bit tag length and an IV of 12 bytes

            cipher.init(Cipher.DECRYPT_MODE, sharedSecret, gcmParameterSpec);
            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);

            System.out.println("Decrypted File Received!!");
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = cipherInputStream.read(buffer)) >= 0) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("Decrypted File Done!!");
        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }



}

