package edu.sltc.vaadin.views.setupexam;

import com.vaadin.flow.component.upload.Receiver;
import edu.sltc.vaadin.services.FileEncryptionService;
import org.springframework.stereotype.Service;

import javax.crypto.CipherOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;


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

}