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
BallWarrior/src/
 ├── Main.java
 │
 ├── core/                     # Lõi engine (tách biệt game)
 │    ├── GameEngine.java      # vòng lặp game (tick, render, update)
 │    ├── InputHandler.java    # quản lý phím bấm
 │    ├── ResourceLoader.java  # load ảnh, âm thanh, font
 │    └── SoundManager.java    # quản lý âm thanh (music, sfx)
 │
 ├── game/                     # Logic game Arkanoid
 │    ├── GameScene.java       # cảnh chính (arkanoid)
 │    ├── LevelManager.java    # load level từ file JSON/TXT
 │    ├── CollisionSystem.java # xử lý va chạm
 │    ├── ScoreSystem.java     # điểm số, mạng
 │    └── PowerUpSystem.java   # item rơi ra
 │
 ├── entity/                   # Đối tượng game
 │    ├── Ball.java
 │    ├── Paddle.java
 │    ├── Brick.java
 │    ├── PowerUp.java
 │    └── Entity.java          # class cha (position, velocity, draw/update)
 │
 ├── ui/                       # Giao diện / scene
 │    ├── MenuScene.java
 │    ├── HUD.java
 │    ├── PauseScene.java
 │    └── GameOverScene.java
 │
 ├── assets/                   # Tài nguyên
 │    ├── images/
 │    │    ├── ball.png
 │    │    ├── paddle.png
 │    │    └── brick_red.png
 │    ├── sounds/
 │    │    ├── bounce.wav
 │    │    ├── break.wav
 │    │    └── powerup.wav
 │    └── levels/
 │         ├── level1.txt
 │         ├── level2.txt
 │         └── level3.txt
 │
 └── utils/                    # Tiện ích
      ├── Constants.java       # định nghĩa hằng số (WIDTH, HEIGHT…)
      └── Vector2D.java        # class vector 2D (dx, dy)

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