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
import entity.User;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import service.FileServiceLocal;

@WebServlet(name = "TrackController", urlPatterns = {"/home", "/add", "/update", "/delete"})
@MultipartConfig
public class TrackController extends HttpServlet {

    @EJB
    private FileServiceLocal fileService;

    @EJB
    private UserFacadeLocal userFacade;

    @EJB
    private TrackFacadeLocal trackFacade;

//    private String uploadDir = getServletContext().getRealPath("") + "\\assets\\images";
    // Xử lý yêu cầu chung
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        System.out.println(action);
        if (action.equals("/home")) {
            // Lấy danh sách nhạc từ cơ sở dữ liệu
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession(false);
            User user = (User) session.getAttribute("user");
            List<Track> trackList = trackFacade.findByUserId(user.getId());
            request.setAttribute("trackList", trackList);

            // Chuyển hướng sang trang playlist.jsp
            request.getRequestDispatcher("playlist.jsp").forward(request, response);
        } else if ("/add".equals(action)) {
            request.getRequestDispatcher("add-music.jsp").forward(request, response);
        } else if ("/update".equals(action)) {
            getUpdatePage(request, response);
        }
    }

    private void addTrack(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        Part musicPart = request.getPart("musicFile");
        Part imagePart = request.getPart("imageFile");
        String uploadDir = getServletContext().getRealPath("");
        // Lấy tên file từ phần upload
        String musicFileName = fileService.saveFile(musicPart, uploadDir + "\\assets\\track");
        String imageFileName = fileService.saveFile(imagePart, uploadDir + "\\assets\\images");

        // 3️⃣ Tạo Model `MusicTrack` để lưu dữ liệu
        Track track = new Track(title, description, musicFileName, new Date(), imageFileName);
        System.out.print(track);
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new IllegalStateException("User with ID 'user1' not found.");
        }
        track.setUserid(user);
        this.trackFacade.create(track);
        // 4️⃣ Gửi model sang JSP để hiển thị
        request.setAttribute("track", track);
        request.getRequestDispatcher("update_music.jsp").forward(request, response);
    }

    private void getUpdatePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        if (id != null && !id.isEmpty()) {
            try {
                Track track = trackFacade.find(id); // Tìm track theo id

                if (track != null) {
                    request.setAttribute("track", track);
                } else {
                    request.setAttribute("error", "Không tìm thấy track.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID không hợp lệ!");
            }
        } else {
            request.setAttribute("error", "Lỗi không có track id");
        }

        request.getRequestDispatcher("update_music.jsp").forward(request, response);
    }

    private void updateTrack(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        Part musicPart = request.getPart("musicFile");
        Part imagePart = request.getPart("imageFile");
        String uploadDir = getServletContext().getRealPath("");
        Track savedTrack = this.trackFacade.find(id);
        if (savedTrack == null) {
            return;
        }

        savedTrack.setTitle(title);
        savedTrack.setDescription(description);
        savedTrack.setCreatedat(new Date());

        // Lấy tên file từ phần upload
        if (musicPart != null && musicPart.getSize() > 0) {  // Kiểm tra dung lượng file
            System.out.println("Music file: " + musicPart.getSubmittedFileName());
            String musicFileName = fileService.saveFile(musicPart, uploadDir + "\\assets\\track");
            fileService.deleteFile(uploadDir + "\\assets\\track", savedTrack.getFilename());
            savedTrack.setFilename(musicFileName);
        } else {
            System.out.println("No music file uploaded.");
        }

        if (imagePart != null && imagePart.getSize() > 0) {  // Kiểm tra dung lượng file
            System.out.println("Image file: " + imagePart.getSubmittedFileName());
            String imageFileName = fileService.saveFile(imagePart, uploadDir + "\\assets\\images");
            fileService.deleteFile(uploadDir + "\\assets\\images", savedTrack.getImagename());

            savedTrack.setImagename(imageFileName);
        } else {
            System.out.println("No image file uploaded.");
        }

        System.out.print(savedTrack);
        this.trackFacade.edit(savedTrack);
        // 4️⃣ Gửi model sang JSP để hiển thị
        request.setAttribute("track", savedTrack);
        request.getRequestDispatcher("update_music.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        if ("/add".equals(action)) {
            addTrack(request, response);
        } else if ("/delete".equals(action)) {
            deleteTrack(request, response);
        } else if ("/update".equals(action)) {
            updateTrack(request, response);
        }
    }

    private void deleteTrack(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        if (id != null && !id.isEmpty()) {
            Track track = trackFacade.find(id);
            System.out.println(track.getFilename());
            if (track != null) {
                trackFacade.remove(track);
                request.setAttribute("message", "Xóa bài hát thành công!");
                String uploadDir = getServletContext().getRealPath("");
                fileService.deleteFile(uploadDir + "\\assets\\images", track.getImagename());
                fileService.deleteFile(uploadDir + "\\assets\\track", track.getFilename());

            } else {
                request.setAttribute("error", "Không tìm thấy bài hát!");
            }
        } else {
            request.setAttribute("error", "ID bài hát không hợp lệ!");
        }

        response.sendRedirect("home");
    }

    @Override
    public String getServletInfo() {
        return "Servlet to handle file upload for music tracks";
    }

//    public String uploadImage(Part part) throws IOException{
//            String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
//            String uploadDirPath = getServletContext().getRealPath("") + "\\assets\\images";
//            String path = uploadDirPath + File.separator + fileName;
//            File uploadDir = new File(uploadDirPath);
//            if (!uploadDir.exists()) {
//                    uploadDir.mkdirs(); // Tạo thư mục 'files' nếu chưa tồn tại
//            }
//            InputStream is = part.getInputStream();
//            byte[] buffer = new byte[is.available()];
//            is.read(buffer); // Đọc dữ liệu vào mảng byte
//
//            // Ghi dữ liệu vào FileOutputStream
//            try (FileOutputStream fos = new FileOutputStream(path)) {
//                fos.write(buffer);
//                fos.flush();
//            }
//
//            return fileName;
//        }
}
