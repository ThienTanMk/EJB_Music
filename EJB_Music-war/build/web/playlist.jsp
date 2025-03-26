<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh Sách Nhạc</title>
        <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
    </head>
    <body class="bg-gray-100 text-black">
        <div class="max-w-4xl mx-auto mt-10 bg-white shadow-lg rounded-lg">
            <!-- Header -->
            <div class="p-4 border-b border-gray-200 flex items-center gap-2">
                <i class="fa-solid fa-music"></i>
                <h1 class="text-2xl font-semibold">Danh Sách Nhạc</h1>
            </div>

            <!-- Hiển thị bài hát đang phát -->
            <div class="p-6 bg-gray-50">
                <div class="flex items-center gap-4">
                    <img id="song-cover" src="assets/images/default.png" alt="Cover"
                         class="h-16 w-16 rounded-md object-cover">
                    <div>
                        <h3 id="song-title" class="text-lg font-semibold">Chọn bài hát để phát</h3>
                        <p id="song-artist" class="text-sm text-gray-600"></p>
                    </div>
                </div>

                <!-- Thanh trượt thời gian -->
                <div class="mt-4 relative">
                    <input type="range" id="progress-bar" value="0" max="100"
                           class="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer 
                           accent-blue-500 [&::-webkit-slider-runnable-track]:bg-gray-300 
                           [&::-webkit-slider-thumb]:bg-blue-500 [&::-moz-range-thumb]:bg-blue-500">
                    <div id="progress-fill" class="absolute top-0 left-0 h-2 bg-blue-500 rounded-lg" style="width: 0%;"></div>
                </div>

                <!-- Nút điều khiển -->
                <div class="mt-4 flex items-center justify-between">
                    <button onclick="playPrevious()" class="text-gray-600 hover:text-gray-900 p-2">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                             stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-skip-back h-5 w-5">
                        <polygon points="19 20 9 12 19 4 19 20"></polygon>
                        <line x1="5" x2="5" y1="19" y2="5"></line>
                        </svg>
                        ️</button>
                    <button onclick="togglePlay()" id="play-btn" class="text-gray-600 hover:text-gray-900 text-2xl p-2">▶️</button>
                    <button onclick="playNext()" class="text-gray-600 hover:text-gray-900 p-2">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                             stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-skip-forward h-5 w-5">
                        <polygon points="5 4 15 12 5 20 5 4"></polygon>
                        <line x1="19" x2="19" y1="5" y2="19"></line>
                        </svg>️
                    </button>
                </div>
            </div>

            <!-- Danh sách bài hát -->
            <div class="p-4">
                <c:if test="${empty trackList}">
                    <p class="text-center text-gray-600">Không có bài hát nào trong danh sách.</p>
                </c:if>
                <c:if test="${not empty trackList}">
                    <table class="w-full text-left text-gray-600">
                        <thead>
                            <tr class="border-b border-gray-200">
                                <th class="p-2">#</th>
                                <th class="p-2">Bài hát</th>
                                <th class="p-2">Mô tả</th>
                                <th class="p-2">Ngày tạo</th>
                                <th class="p-2">Hiệu chỉnh</th>
                                <th class="p-2">Xóa</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="track" items="${trackList}" varStatus="loop">
                                <tr class="border-b border-gray-200 hover:bg-gray-100 cursor-pointer"
                                    onclick="playSong('${track.id}', '${pageContext.request.contextPath}/assets/track/${track.filename}', '${track.title}', '${pageContext.request.contextPath}/assets/images/${track.imagename}')">
                                    <td class="p-2">${loop.index + 1}</td>
                                    <td class="p-2 flex items-center gap-3">
                                        <img id="preview-image" src="assets/images/${track.imagename}" class="w-10 h-10 rounded-md object-cover"/>
                                        <div>
                                            <p class="text-black">${track.title}</p>
                                        </div>
                                    </td>
                                    <td class="p-2">
                                        <p class="text-black">${track.description}</p>
                                    </td>
                                    <td class="p-2">
                                        <fmt:formatDate value="${track.createdat}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td class="p-2">
                                        <button onclick="editSong('${track.id}')" class="text-blue-500 hover:text-blue-700">
                                            📝 Sửa
                                        </button>
                                    </td>
                                    <td class="p-2">
                                        <form action="delete" method="post" onsubmit="return confirm('Bạn có chắc chắn muốn xóa không?');">
                                            <input type="hidden" name="id" value="${track.id}">
                                            <button type="submit">🗑</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
            </div>
        </div>

        <!-- Audio Player -->
        <audio id="audio-player">
            <source id="audio-source" src="" type="audio/mpeg">
            Trình duyệt của bạn không hỗ trợ phát âm thanh.
        </audio>

        <script>
            let currentIndex = -1;
            let isPlaying = false;
            let tracks = [];

            // Load danh sách nhạc từ JSP
            <c:forEach var="track" items="${trackList}" varStatus="loop">
            tracks.push({
                id: "${track.id}",
                src: "${pageContext.request.contextPath}/assets/track/${track.filename}",
                        title: "${track.title}",
                        cover: "${pageContext.request.contextPath}/assets/images/${track.imagename}"
                            });
            </c:forEach>
            const audio = document.getElementById("audio-player");
            const source = document.getElementById("audio-source");
            const playBtn = document.getElementById("play-btn");
            const progressBar = document.getElementById("progress-bar");
            const songTitle = document.getElementById("song-title");
            const songCover = document.getElementById("song-cover");

            function playSong(id, src, title, cover) {
                currentIndex = tracks.findIndex(track => track.id === id);
                if (currentIndex === -1) {
                    console.error("Không tìm thấy bài hát!");
                     return;
                }
                // Cập nhật thông tin bài hát
                songTitle.textContent = title;
                songCover.src = cover;
                // Đặt lại thanh trượt & thời gian
                progressBar.value = 0;
                audio.currentTime = 0;
                // Cập nhật nguồn và phát nhạc
                source.src = src;
                audio.load();
                audio.play().then(() => {
                    isPlaying = true;
                    playBtn.innerText = "⏸️";
                }).catch(error => {
                    console.error("Lỗi phát nhạc:", error);
                    });
            }
            function togglePlay() {
                if (tracks.length === 0)
                return;
                                if (currentIndex === -1) {
                                    playSong(tracks[0].id, tracks[0].src, tracks[0].title, tracks[0].cover);
                                    return;
                                }
                                if (isPlaying) {
                                    audio.pause();
                                    playBtn.innerText = "▶️";
                                    isPlaying = false;
                                } else {
                                    audio.play().then(() => {
                                        playBtn.innerText = "⏸️";
                                        isPlaying = true;
                                    }).catch(error => {
                                        console.error("❌ Lỗi tiếp tục phát:", error);
                                    });
                                }
                            }

                            function playNext() {
                                if (tracks.length === 0)
                                    return;
                                currentIndex = (currentIndex + 1) % tracks.length; // Lặp lại danh sách khi hết
                                playSong(tracks[currentIndex].id, tracks[currentIndex].src,
                                        tracks[currentIndex].title, tracks[currentIndex].cover);
                            }

            function playPrevious() {
                if (tracks.length === 0)
                                    return;
                                currentIndex = (currentIndex - 1 + tracks.length) % tracks.length; // Lùi bài và lặp lại
                                playSong(tracks[currentIndex].id, tracks[currentIndex].src,
                                        tracks[currentIndex].title, tracks[currentIndex].cover);
                            }

                            function updateProgressBar() {
                                if (!isNaN(audio.duration) && audio.duration > 0) {
                                    progressBar.value = (audio.currentTime / audio.duration) * 100;
                                }
                            }

                            // Lắng nghe sự kiện cập nhật thanh trượt
                            audio.addEventListener("timeupdate", updateProgressBar);
                            audio.addEventListener("ended", playNext);

                            // Điều chỉnh vị trí nhạc khi kéo thanh trượt
                            progressBar.addEventListener("input", () => {
                                if (!isNaN(audio.duration)) {
                                    audio.currentTime = (progressBar.value / 100) * audio.duration;
                                }
                            });
        </script>
    </body>
</html>
