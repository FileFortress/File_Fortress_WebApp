package edu.sltc.vaadin.controller;

import edu.sltc.vaadin.views.setupexam.FileReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 20/12/2023
 * @package edu.sltc.vaadin.controller
 * @project_Name File_Fortress_WebApp
 */
@RestController
@RequestMapping(value = "/fortress/")
public class FileUploadController {

    private final FileReceiver fileReceiver;

    @Autowired
    public FileUploadController(FileReceiver fileReceiver) {
        this.fileReceiver = fileReceiver;
    }

    @GetMapping("/vaadin")
    public String init(){
        return "Hello World!!!!!!!";
    }

//    @PostMapping("/file_upload")
//    public ResponseEntity<String> uploadByteArray(@RequestBody byte[] fileBytes) {
//        // Provide a path where you want to save the file
//        String filePath = "src/main/resources/testFile.txt";  // Replace with your desired file path and extension
//
//        try (FileOutputStream fos = new FileOutputStream(filePath)) {
//            fos.write(fileBytes);
//            fos.flush();
//        } catch (IOException e) {
//            // Handle IOException, e.g., log the error or return an error response
//            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>("File upload successful!", HttpStatus.OK);
//    }

    @PostMapping("/file_upload")
    public ResponseEntity<String> uploadFile(@RequestBody @RequestPart("file") MultipartFile file) {
        try {
            String fileName = fileReceiver.save(file.getResource().getFile());
            System.out.println("File received: " + fileName);
            // You can add more processing logic here if needed
            return new ResponseEntity<>("File upload successful!", HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions, e.g., file processing errors
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
