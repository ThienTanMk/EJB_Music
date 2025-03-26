package controller;

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

@WebServlet(name = "TrackController", urlPatterns = {"/music"})
@MultipartConfig
public class TrackController extends HttpServlet {

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
            File uploadDir = new File(getServletContext().getRealPath("/") +  "\\assets\\images" );
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
            result =false;
            e.printStackTrace(); // In lỗi nếu có lỗi xảy ra
        }

        return result; // Trả về kết quả tải lên (thành công hay thất bại)
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet to handle file upload for music tracks";
    }
}
