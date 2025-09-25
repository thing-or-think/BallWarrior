# Changelog

Tất cả các thay đổi đáng chú ý trong dự án này được ghi lại ở đây.  
Tuân theo [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) và Semantic Versioning.

---

## [Unreleased]
- Thay đổi đang phát triển (chưa release)
- Có thể bao gồm: sửa lỗi nhỏ, thêm power-up mới, tối ưu hiệu năng
- By: thing-or-think, Lee-Hank

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

## [0.0.4] - 2025-09-25
### Added
- `Brick.java`: lớp gạch trong game, kế thừa từ `Entity`:
    - Có thuộc tính `health` (độ bền – số lần cần đánh để phá vỡ).
    - Có thuộc tính `color` (màu sắc của gạch).
    - `update()`: gạch đứng yên, không di chuyển.
    - `draw(Graphics g)`: vẽ gạch và viền đen bao quanh.
    - `hit()`: giảm độ bền khi bị bóng đập trúng.
    - `isDestroyed()`: trả về `true` nếu gạch bị phá hủy (health <= 0).
- `GameScene`:
    - Thêm danh sách `bricks` và logic tạo nhiều gạch.
    - Thêm cơ chế `score` và `lives`.
    - Thêm `resetBall()` để khởi tạo lại bóng khi mất mạng.
- By: thing-or-think

### Changed
- `CollisionSystem.handleBallCollision(...)`:
    - Giờ trả về `boolean` để báo có va chạm xảy ra hay không, giúp `GameScene` biết khi nào cần gọi `brick.hit()`, tính điểm hoặc xử lý logic khác.


### Fixed
- Xử lý khi bóng rơi ra ngoài màn hình: trừ mạng, đặt lại bóng thay vì tiếp tục chạy ngoài vùng chơi.

