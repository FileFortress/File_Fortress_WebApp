package edu.sltc.vaadin.services;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileEncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    private static final String SECRET_KEY = "yourSecretKey"; // Change this with a secure secret key

    public static void encryptFile(MultipartFile inputFile, String outputFilePath) throws Exception {
        try (FileInputStream fileInputStream = (FileInputStream) inputFile.getInputStream();
             FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath)) {

            Key secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            try (CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) >= 0) {
                    cipherOutputStream.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}

