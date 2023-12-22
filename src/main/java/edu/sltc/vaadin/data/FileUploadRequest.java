package edu.sltc.vaadin.data;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 21/12/2023
 * @package edu.sltc.vaadin.data
 * @project_Name File_Fortress_WebApp
 */
public class FileUploadRequest {
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
