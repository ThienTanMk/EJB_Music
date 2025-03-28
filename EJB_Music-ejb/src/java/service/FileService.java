/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package service;

import jakarta.ejb.Stateless;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.UUID;


@Stateless
public class FileService implements FileServiceLocal {
       public static String generateID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String saveFile(Part part, String uploadDir) throws IOException {
            String fileName =generateID()+ Paths.get(part.getSubmittedFileName()).getFileName().toString();
            
            String path = uploadDir + File.separator + fileName;
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs(); // Tạo thư mục 'files' nếu chưa tồn tại
            }
            InputStream is = part.getInputStream();
            byte[] buffer = new byte[is.available()];
            is.read(buffer); // Đọc dữ liệu vào mảng byte

            // Ghi dữ liệu vào FileOutputStream
            try (FileOutputStream fos = new FileOutputStream(path)) {
                fos.write(buffer);
                fos.flush();
            }

            return fileName;
    }
    @Override
    public boolean deleteFile(String uploadDir, String fileName) {
        try {
           
            File fileToDelete = new File(uploadDir + File.separator + fileName);
            if (fileToDelete.exists()) {
                boolean deleted = fileToDelete.delete();
                if (!deleted) {
                    System.err.println("Could not delete file: " + fileName);
                }
                return deleted;
            } else {
                System.err.println("File not found: " + fileName);
                return false;
            }
        } catch (SecurityException e) {
            // Xử lý trường hợp lỗi bảo mật (không có quyền xóa)
            System.err.println("Security error deleting file: " + fileName);
            e.printStackTrace();
            return false;
        }
    }

}
