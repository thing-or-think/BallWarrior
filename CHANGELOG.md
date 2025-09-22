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

