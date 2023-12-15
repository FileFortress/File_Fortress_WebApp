package edu.sltc.vaadin.views.setupexam;

import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.UploadI18N;
import edu.sltc.vaadin.services.FileEncryptionService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.*;


/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 22/10/2023
 * @package edu.sltc.vaadin.views.admindashboard
 * @project_Name File_Fortress_WebApp
 */
public class FileReceiver implements Receiver {
    private String key;
    @Override
    public OutputStream receiveUpload(String fileName, String MIMEType) {
        // Define the file where the uploaded file will be stored
        if (MIMEType.equals("application/pdf")){
            File file = new File(fileName);
            // Create an output stream to write the uploaded file to the file system
            try {
                return new FileOutputStream(encryptFile(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        else if (MIMEType.equals("text/plain")){
            File file = new File(fileName);
            // Create an output stream to write the uploaded file to the file system
            try {
                return new FileOutputStream(encryptFile(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private File encryptFile(File file) { // file encrypt using rsa
        try {
            FileEncryptionService.encryptFile((MultipartFile) file,"classpath:/enc.txt");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file;
    }

}