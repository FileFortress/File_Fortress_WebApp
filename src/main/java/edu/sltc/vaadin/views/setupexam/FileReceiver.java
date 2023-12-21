package edu.sltc.vaadin.views.setupexam;

import com.vaadin.flow.component.upload.Receiver;
import edu.sltc.vaadin.services.FileEncryptionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.CipherOutputStream;
import java.io.*;


/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 22/10/2023
 * @package edu.sltc.vaadin.views.admindashboard
 * @project_Name File_Fortress_WebApp
 */

@Service
public class FileReceiver implements Receiver {

    @Override
    public OutputStream receiveUpload(String fileName, String MIMEType) {
        // Define the file where the uploaded file will be stored
        if (MIMEType.equals("application/pdf")){
            File file = new File(fileName);
            // Create an output stream to write the uploaded file to the file system
            return encryptFile(file);
        }
        else if (MIMEType.equals("text/plain")){
            File file = new File(fileName);
            // Create an output stream to write the uploaded file to the file system
//            return new FileOutputStream(file);
            return encryptFile(file);
        }
        return null;
    }

    private OutputStream encryptFile(File file) { // file encrypt using rsa
        try {
            FileOutputStream out = new FileOutputStream(file);
            return out;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String save(File file) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/examFile.pdf");
             FileInputStream inputStream = new FileInputStream( file)){
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) >= 0) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            return file.getName();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}