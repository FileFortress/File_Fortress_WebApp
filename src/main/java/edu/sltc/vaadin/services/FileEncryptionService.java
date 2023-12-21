package edu.sltc.vaadin.services;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import edu.sltc.vaadin.views.login.LoginView;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public class FileEncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final SecretKey SECRET_KEY = AESKeyGenerator.getInstance().getSecretKey();
    public static void encryptFile(InputStream inputStream, String outputFilePath) throws RuntimeException{
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath)) {
            Key secretKey = new SecretKeySpec(SECRET_KEY.getEncoded(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            try (CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    cipherOutputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }
    public static void decryptFile(){

    }

}

