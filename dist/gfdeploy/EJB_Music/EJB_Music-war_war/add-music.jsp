<%-- 
    Document   : add-music
    Created on : Mar 26, 2025, 4:32:35 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
    </head>
    <body class="bg-gray-100 py-10">
        <div class="container mx-auto max-w-2xl bg-white p-6 shadow-md rounded-lg">
        <h2 class="text-2xl font-bold mb-2">Add New Music</h2>
        <p class="text-gray-600 mb-4">Upload your music track with details and cover image</p>
        
        <form action="upload" method="post" enctype="multipart/form-data" class="space-y-6">
            <div>
                <label for="title" class="block font-medium">Title</label>
                <input type="text" id="title" name="title" class="w-full p-2 border rounded" placeholder="Enter music title" required>
            </div>
            
            <div>
                <label for="description" class="block font-medium">Description</label>
                <textarea id="description" name="description" class="w-full p-2 border rounded min-h-[100px]" placeholder="Enter a description for your music"></textarea>
            </div>
            
            <div>
                <label for="music-file" class="block font-medium">Music File</label>
                <input type="file" id="music-file" name="musicFile" accept="audio/*" class="w-full p-2 border rounded" required>
            </div>
            
            <div>
                <label for="image-file" class="block font-medium">Cover Image</label>
                <input type="file" id="image-file" name="imageFile" accept="image/*" class="w-full p-2 border rounded">
            </div>
            
            <button type="submit" class="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600">Upload Music</button>
        </form>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


        </div>
    </body>
</html>