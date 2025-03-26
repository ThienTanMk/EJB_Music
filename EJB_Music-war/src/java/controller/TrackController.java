package controller;

import entity.Track;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Date;
import dao.TrackFacadeLocal;
import dao.UserFacadeLocal;

@WebServlet(name = "TrackController", urlPatterns = {"/music", "/upload"})
@MultipartConfig
public class TrackController extends HttpServlet {

    @EJB
    private UserFacadeLocal userFacade;

    @EJB
    private TrackFacadeLocal trackFacade;

    // Xử lý yêu cầu chung
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            // Lấy phần ảnh từ form upload
            Part part = request.getPart("image"); // 'image' là tên input trong form
            String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString(); // Lấy tên file từ phần 'image'

            // Xác định đường dẫn lưu tệp
            String path = getServletContext().getRealPath("/") + "\\assets\\images" + File.separator + fileName;
            System.out.println(path);
            // Kiểm tra xem thư mục có tồn tại không, nếu không thì tạo mới
            File uploadDir = new File(getServletContext().getRealPath("/") + "\\assets\\images");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // Tạo thư mục 'files' nếu chưa tồn tại
            }

            // Đọc dữ liệu từ InputStream và tải lên tệp
            InputStream is = part.getInputStream();
            boolean uploadSuccess = uploadFile(is, path);

            // Thông báo kết quả
            if (uploadSuccess) {
                out.println("<h3>File uploaded successfully: " + fileName + "</h3>");
            } else {
                out.println("<h3>Something went wrong while uploading the file.</h3>");
            }

        }

        // Chuyển hướng người dùng về trang index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    // Hàm tải lên tệp
    public boolean uploadFile(InputStream is, String path) {
        boolean result = false;

        try {
            // Đọc dữ liệu từ InputStream
            byte[] buffer = new byte[is.available()];
            is.read(buffer); // Đọc dữ liệu vào mảng byte

            // Ghi dữ liệu vào FileOutputStream
            try (FileOutputStream fos = new FileOutputStream(path)) {
                fos.write(buffer);
                fos.flush();
                result = true; // Cập nhật trạng thái nếu việc ghi thành công
            }

        } catch (IOException e) {
            result = false;
//            e.printStackTrace(); // In lỗi nếu có lỗi xảy ra
        }

        return result; // Trả về kết quả tải lên (thành công hay thất bại)
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
          String action = request.getServletPath();
   
        if ("/upload".equals(action)) {
            request.getRequestDispatcher("add-music.jsp").forward(request, response);
        }   else {
            processRequest(request, response);
        }
    }

    @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        Part musicPart = request.getPart("musicFile");
        Part imagePart = request.getPart("imageFile");

        // 2️⃣ Xử lý upload file
        String uploadPath = getServletContext().getRealPath("/") + "uploads/";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        // Lấy tên file từ phần upload
        String musicFileName = uploadImage(musicPart);
        String imageFileName = uploadImage(imagePart);

       

        // 3️⃣ Tạo Model `MusicTrack` để lưu dữ liệu
        Track track = new Track(title, description, musicFileName, new Date(), imageFileName);
        System.out.print(track);
        this.trackFacade.create(track);
        // 4️⃣ Gửi model sang JSP để hiển thị
        request.setAttribute("track", track);
        request.getRequestDispatcher("add-music.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet to handle file upload for music tracks";
    }
    
//   private void saveFile(InputStream inputStream, String filePath) throws IOException {
//    File file = new File(filePath);
//    try (FileOutputStream outputStream = new FileOutputStream(file)) {
//        byte[] buffer = new byte[1024];
//        int bytesRead;
//        while ((bytesRead = inputStream.read(buffer)) != -1) {
//            outputStream.write(buffer, 0, bytesRead);
//        }
//        }
//    }
    public String uploadImage(Part part) throws IOException{
            String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
            String uploadDirPath = getServletContext().getRealPath("") + "\\assets\\images";
            String path = uploadDirPath + File.separator + fileName;
            File uploadDir = new File(uploadDirPath);
            if (!uploadDir.exists()) {
                    uploadDir.mkdirs(); // Tạo thư mục 'files' nếu chưa tồn tại
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
}
