/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package service;

import jakarta.ejb.Local;
import jakarta.servlet.http.Part;
import java.io.IOException;

/**
 *
 * @author admin
 */
@Local
public interface FileServiceLocal {

    public String saveFile(Part part,String uploadDir) throws IOException;
    boolean deleteFile(String uploadDir, String fileName);
}
