# 🏐 BallWarrior

BallWarrior là một game clone từ **Arkanoid**, được viết bằng **Java + Swing**.  
Người chơi điều khiển thanh trượt để đỡ bóng, phá hết gạch để qua màn.

---

## 🎮 Gameplay

- Di chuyển thanh trượt trái/phải để giữ bóng không rơi.
- Phá hết gạch trên màn hình để chiến thắng.
- Nếu bóng rơi xuống thì thua.

![Demo Screenshot](./docs/screenshot.png) <!-- thay bằng ảnh thực tế -->

---

## 📂 Cấu trúc thư mục 
```bash
BallWarrior/
├── Main.java              # Điểm khởi chạy
├── core/                  # Lõi game engine
│   ├── GameEngine.java    # Vòng lặp game (tick, render, update)
│   ├── InputHandler.java  # Quản lý phím bấm
│   ├── ResourceLoader.java# Load ảnh, âm thanh, font
│   └── SoundManager.java  # Quản lý âm thanh
├── entities/              # Các đối tượng trong game
│   ├── Ball.java
│   ├── Paddle.java
│   ├── Brick.java
│   └── PowerUp.java
├── levels/                # Định nghĩa màn chơi
│   ├── Level1.java
│   ├── Level2.java
│   └── ...
├── assets/                # Hình ảnh, âm thanh
└── README.md
```
---

## 🚀 Cách chạy

Yêu cầu:
- **Java 17+** (hoặc tương thích).

Chạy bằng dòng lệnh:

```bash
# Biên dịch
javac Main.java

# Chạy game
java Main
Hoặc mở trực tiếp trong IntelliJ IDEA / Eclipse / NetBeans và chạy Main.
```
🛠️ Công nghệ sử dụng

Java 17

Swing (UI & rendering)

OOP design

🤝 Đóng góp

Pull requests được hoan nghênh.
Nếu phát hiện bug, vui lòng tạo issue trong repo.