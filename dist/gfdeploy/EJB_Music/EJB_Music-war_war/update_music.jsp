<%-- 
    Document   : add-music
    Created on : Mar 26, 2025, 4:32:35 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
       

    </head>
    <body class="bg-gray-100 py-10">
        
        <div class="container mx-auto max-w-2xl bg-white p-6 shadow-md rounded-lg">
        <c:if test="${not empty error}">
            <div 
                class="flex justify-between items-center"
            >
                <p class="text-red-500 text-sm text-center mb-4 text-[40px]">${error}</p>
                <a href = "home" class="bg-black px-6 py-2 text-white rounded-lg">Back</a>
            </div>
        </c:if>
        <c:if test="${not empty track}">
             <div class="flex justify-between">
                <h2 class="text-2xl font-bold mb-2">Update Music</h2>
                <a href = "home" class="bg-black px-6 py-2 text-white rounded-lg">Back</a>
            </div>
            <p class="text-gray-600 mb-4">Upload your music track with details and cover image</p>
            
            <form action="update" method="post" enctype="multipart/form-data" class="space-y-6">
                <input type="hidden" name="id" value="${track.id}">
                <div>
                    <label for="title" class="block font-medium">Title</label>
                    <input type="text" id="title" name="title" value="${track.title}" class="w-full p-2 border rounded" placeholder="Enter music title" required>
                </div>

                <div>
                    <label for="description" class="block font-medium">Description</label>
                    <textarea id="description" name="description" class="w-full p-2 border rounded min-h-[100px]" placeholder="Enter a description for your music">${track.description}</textarea>
                </div>
                <div class="flex items-center">
                    <div class="flex flex-col m-2 gap-3">
                        <label for="music-file" class="block font-medium">Music File</label>
                        <input type="file" id="music-file" name="musicFile" accept="audio/*" class="w-full p-2 border rounded" >
                        
                        <audio id="preview-audio" controls>
                           <source src="assets/track/${track.filename}" type="audio/mpeg">
                            Trình duyệt của bạn không hỗ trợ phát âm thanh.
                        </audio>
                    </div>

                    <div class="mx-8">
                        <div class="w-full flex items-center justify-center my-2">
                            <img id="preview-image" src="assets/images/${track.imagename}" class="h-[200px] w-full"/>
                            
                        </div>
                        
                        <input type="file" id="image-file" name="imageFile"  accept="image/*" placeholder="Cover image" class="w-full p-2 border rounded">
                    </div>
                </div>

                <button type="submit" class="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600">Update Music</button>
            </form>
        </c:if>
        </div>
    </body>
    <script>
        document.getElementById("image-file").addEventListener("change", function (event) {
            var file = event.target.files[0]; // Lấy file ảnh đã chọn
            if (file) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    document.querySelector("img").src = e.target.result; // Cập nhật ảnh
                };
                reader.readAsDataURL(file); // Đọc file dưới dạng URL
            }
        });
        document.getElementById("music-file").addEventListener("change", function (event) {
            var file = event.target.files[0];
            if (file) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    var audio = document.getElementById("preview-audio");
                    audio.src = e.target.result;
//                    audio.play(); // Tự động phát nhạc sau khi chọn
                };
                reader.readAsDataURL(file);
            }
        });
    </script>
        
</html>