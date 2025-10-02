# Changelog

Tất cả các thay đổi đáng chú ý trong dự án này được ghi lại ở đây.  
Tuân theo [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) và Semantic Versioning.

---

## [0.1.1] - 2025-10-01

### Fixed
- Sửa lỗi precision trong `CollisionUtils.circleLineIntersection(...)` và `circleSegmentIntersection(...)` khi điểm giao nằm gần ranh giới.
- Thêm `Constants.COLLISION_EPSILON` để xử lý sai số khi so sánh vị trí giao điểm và khoảng cách.
- Sửa lỗi `CollisionSystem.findNearestCollision(...)` trả về kết quả sai trong trường hợp có nhiều va chạm cùng lúc.
- Khắc phục trường hợp bóng bị kẹt trong AABB (Paddle/Brick) do tính toán va chạm thiếu kiểm tra hướng.
- Cải thiện tính ổn định khi tính `t` (thời gian va chạm) trong `CollisionResult`.

### Changed
- Tối ưu hàm `circleLineIntersection(...)` và `getLineIntersection(...)` để loại bỏ tính toán dư thừa.
- Chuẩn hóa điều kiện kiểm tra `isBetween(...)` bằng sai số `COLLISION_EPSILON`.
- Cải thiện log debug khi phát hiện va chạm để dễ theo dõi.

---

## [0.1.0] - 2025-09-30

### Added
- `CollisionSystem.java`: lớp điều phối va chạm, tìm nearest collision giữa các entity
- `CollisionResult.java`: data class lưu thông tin va chạm
    - `entity`: entity bị va chạm
    - `hitPoint`: điểm va chạm
    - `t`: thời gian va chạm trên quãng đường di chuyển
- `CollisionUtils.java`: các hàm tiện ích hỗ trợ va chạm
    - `isBetween(float a, float b, float target)`: kiểm tra target có nằm giữa a và b
    - `getLineIntersection(...)`: tính giao điểm giữa 2 đoạn thẳng
    - `circleLineIntersection(...)`: tính giao điểm giữa đoạn thẳng và đường tròn
- `CircleVsAABB.java`: sửa lại va chạm giữa Ball (circle) và Paddle/Brick (AABB)
    - Cải thiện độ chính xác va chạm liên tục (swept collision)
    - Xử lý va chạm với cạnh và góc, bao gồm trường hợp bóng kẹt trong AABB
    - Sửa `intersect(...)` trả về CollisionResult sớm nhất trong frame- `CircleVsCircle.java`: kiểm tra và xử lý va chạm giữa Ball với Ball hoặc Ball với PowerUp nếu cần
- `Vector2D.java`:
    - Constructor mới `Vector2D(Vector2D other)` để khởi tạo từ vector khác
    - Setter mới `set(Vector2D other)` để gán giá trị từ vector khác
    - Phương thức `isParallel(Vector2D other)` kiểm tra hai vector song song
    - Phương thức `normalLeft()` và `normalRight()` trả về vector pháp tuyến

### Changed
- Chia module va chạm sang thư mục `game/collision/` để dễ quản lý và mở rộng
- `GameScene.java`:
    - Loại bỏ kiểm tra va chạm thủ công với Ball-Paddle và Ball-Brick
    - Tích hợp `CollisionSystem` để xử lý va chạm
    - Đăng ký Paddle và Bricks vào `CollisionSystem`
    - Gọi `collisionSystem.findNearestCollision(ball)` và `collisionSystem.resolveCollision(...)` trong `update()`
    - Cập nhật xử lý điểm và mạng thông qua `ScoreSystem` khi va chạm với Brick hoặc khi mất mạng
- `Vector2D.java`:
    - Đổi tên phương thức `multiply(float k)` trả về void sang `multiplied(float k)` trả về vector mới
    - Cải thiện `normalized()` để trả về vector `(0,0)` nếu độ dài vector quá nhỏ
    - Sắp xếp lại các phương thức theo nhóm: constructor, setter, arithmetic, geometry

### Fixed
- Cải thiện và sửa lỗi hệ thống va chạm giữa Ball và AABB
- Đảm bảo các class va chạm hoạt động chính xác, độc lập và dễ mở rộng cho các entity khác
- Sửa một số vấn đề nhỏ về precision trong các phương thức tính toán vector

---

## [Unreleased]
- Thay đổi đang phát triển (chưa release)
- Có thể bao gồm: sửa lỗi nhỏ, thêm power-up mới, tối ưu hiệu năng
- By: thing-or-think, Lee-Hank

---

## [0.0.5] - 2025-09-26
### Added
- `HUD.java`: lớp hiển thị thông tin HUD trên màn hình:
    - `render(Graphics g, ScoreSystem scoreSystem)`: vẽ `Score` bên trái và `Lives` bên phải màn hình.
- `LevelManager.java`: lớp chịu trách nhiệm đọc file level (dạng text) và sinh danh sách `Brick`:
    - `load(String path)`: đọc file level, ký tự `'1'` => có gạch, ký tự khác => bỏ trống.
    - Hỗ trợ tùy chỉnh vị trí bắt đầu, kích thước gạch, và khoảng cách giữa các gạch.
- `ScoreSystem.java`: quản lý điểm và mạng của người chơi:
    - `addScore(int points)`: cộng điểm.
    - `getScore()`: trả về điểm hiện tại.
    - `loseLife()`, `addLife()`: trừ hoặc cộng mạng.
    - `getLives()`: trả về số mạng hiện tại.
    - `isGameOver()`: kiểm tra hết mạng.
- `InputHandler.java`: bổ sung xử lý chuột:
    - `mouseMoved(MouseEvent e)`: cập nhật vị trí chuột khi di chuyển.
    - `mouseDragged(MouseEvent e)`: cập nhật vị trí chuột khi kéo giữ.
    - `mousePressed(MouseEvent e)`: xử lý khi nhấn chuột.
    - `mouseReleased(MouseEvent e)`: xử lý khi thả chuột.
    - `mouseClicked(MouseEvent e)`: xử lý khi click chuột.
    - `mouseEntered(MouseEvent e)`: xử lý khi chuột đi vào cửa sổ game.
    - `mouseExited(MouseEvent e)`: xử lý khi chuột rời khỏi cửa sổ game.
    - Getter:
        - `getMouseX()`: trả về hoành độ chuột hiện tại.
        - `getMouseY()`: trả về tung độ chuột hiện tại.
- `level1.txt`: file mô tả màn chơi đầu tiên, định nghĩa layout gạch bằng ký tự (`'1'` có gạch, `'0'` trống).

### Changed
- `GameScene.java`:
    - Loại bỏ biến `score` và `lives` cục bộ, thay bằng `ScoreSystem` để quản lý điểm và mạng tập trung.
    - Thêm gọi `HUD.render(...)` trong `render()` để hiển thị thông tin người chơi.
    - Tích hợp `LevelManager` để load bricks từ file level.

### Fixed
- Dọn dẹp code `GameScene`: tránh trùng lặp dữ liệu (score, lives) giữa `GameScene` và `ScoreSystem`.

---

## [0.0.4] - 2025-09-25
### Added
- `Brick.java`: lớp gạch trong game, kế thừa từ `Entity`:
    - Có thuộc tính `health` (độ bền – số lần cần đánh để phá vỡ).
    - Có thuộc tính `color` (màu sắc của gạch).
    - `update()`: gạch đứng yên, không di chuyển.
    - `draw(Graphics g)`: vẽ gạch và viền đen bao quanh.
    - `hit()`: giảm độ bền khi bị bóng đập trúng.
    - `isDestroyed()`: trả về `true` nếu gạch bị phá hủy (health <= 0).
- `GameScene.java`:
    - Thêm danh sách `bricks` và logic tạo nhiều gạch.
    - Thêm cơ chế `score` và `lives`.
    - Thêm `resetBall()` để khởi tạo lại bóng khi mất mạng.
- By: thing-or-think

### Changed
- `CollisionSystem.handleBallCollision(...)`:
    - Giờ trả về `boolean` để báo có va chạm xảy ra hay không, giúp `GameScene` biết khi nào cần gọi `brick.hit()`, tính điểm hoặc xử lý logic khác.


### Fixed
- Xử lý khi bóng rơi ra ngoài màn hình: trừ mạng, đặt lại bóng thay vì tiếp tục chạy ngoài vùng chơi.

---

## [0.0.3] - 2025-09-25
### Added
- `MathUtils.java`: lớp tiện ích toán học 2D, bao gồm:
    - `isBetween()`: kiểm tra một giá trị có nằm giữa hai giá trị với EPSILON.
    - `getLineIntersection()`: tìm giao điểm của hai đoạn thẳng; trả về `Vector2D` nếu có, ngược lại trả về `null`.
- `CollisionSystem.java`: quản lý tất cả va chạm giữa các thực thể trong game, bao gồm:
    - Kiểm tra va chạm AABB giữa hai entity với EPSILON (`checkCollision`).
    - Xử lý va chạm giữa `Ball` và các entity, tính giao điểm các cạnh, cập nhật vị trí và đảo chiều vận tốc (`handleBallCollision`).
    - Xử lý trường hợp bóng bị chui vào entity, đẩy bóng ra sát cạnh và giới hạn vị trí trong màn hình (`handleBallInsideEntity`).
- `Vector2D.java`: thêm các phương thức toán học và tiện ích cho vector 2D:
    - `added()`, `subtracted()`: trả về vector mới sau khi cộng/trừ.
    - `equals()`: so sánh vector với EPSILON để chống sai số float.
    - `dot()`, `cross()`: tính tích vô hướng và tích chéo 2D.
    - `length()`, `distance()`, `angle()`: tính độ dài, khoảng cách và góc giữa các vector.
    - `multiply()`, `normalized()`: nhân vector với scalar, chuẩn hóa vector.
    - `distancePointToLine()`: tính khoảng cách từ điểm tới đoạn thẳng.
- `Constants.java`: thêm hằng số `COLLISION_EPSILON` để sử dụng trong các phép so sánh số thực, giúp xử lý va chạm chính xác hơn.
- By: thing-or-think

### Changed
- Chuyển hàm `checkCollision(Entity a, Entity b)` từ `GameScene` sang `CollisionSystem` để tập trung quản lý tất cả va chạm trong một lớp riêng.

### Fixed
- Sửa lỗi va chạm giữa `Ball` và `Paddle` để bóng không chui vào paddle khi di chuyển nhanh.

---

## [0.0.2] - 2025-09-22
### Added
- `Vector2D.java`: lớp hỗ trợ tính toán toán học cho vị trí, vận tốc, và vector 2D.
- `Entity.java`: lớp cơ sở (base class) cho các thực thể trong game như bóng và thanh trượt.
- `Ball.java`: lớp con của `Entity`, đại diện cho quả bóng trong game, bao gồm logic di chuyển và va chạm.
- `Paddle.java`: lớp con của `Entity`, đại diện cho thanh trượt (paddle), quản lý di chuyển theo phím bấm.
- `Constants.java`: chứa các hằng số chung cho game (kích thước màn hình, tốc độ di chuyển, kích thước paddle/ball...).
- `GameScene.java`: lớp quản lý trạng thái và các đối tượng trong một cảnh game cụ thể, hỗ trợ render và update các thực thể; được tích hợp vào `GameEngine` để quản lý cảnh game dễ dàng hơn.
- By: thing-or-think

### Changed
- Cải tiến `GameEngine` để sử dụng `GameScene` làm trung tâm quản lý các đối tượng và cập nhật game.

### Fixed
- Không có
---

## [0.0.1] - 2025-09-22
### Added
- `Main.java`: lớp khởi tạo game, tạo cửa sổ và chạy `GameEngine`.
- `GameEngine.java`: lớp lõi quản lý vòng lặp game, cập nhật và render các đối tượng.
- `InputHandler.java`: lớp quản lý trạng thái phím bấm của người chơi.
- By: thing-or-think

### Changed
- Không có (phiên bản đầu tiên)

### Fixed
- Không có (phiên bản đầu tiên)


