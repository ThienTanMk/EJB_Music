<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Upload Hình ảnh & Âm thanh</title>
</head>
<body>
    <h2>Upload File</h2>
    <form action="music" method="post" enctype="multipart/form-data">
        Chọn hình ảnh: <input type="file" name="image"><br><br>
        <input type="submit" value="Upload">
    </form>
</body>
</html>
