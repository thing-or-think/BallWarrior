# 🏐 BallWarrior

BallWarrior là một game clone từ **Arkanoid**, được viết bằng **Java + Swing**.  
Người chơi điều khiển thanh trượt để đỡ bóng, phá hết gạch để qua màn.

---

## 🎮 Gameplay

- Di chuyển thanh trượt trái/phải để giữ bóng không rơi.
- Phá hết gạch trên màn hình để chiến thắng.
- Nếu bóng rơi xuống thì thua.

![Demo Screenshot](./docs/screenshot.png) <!-- thay bằng ảnh thực tế -->

### 🔹 Va chạm bóng – gạch
Minh họa hệ thống Collision:

<img src="./docs/collision_animation.gif" alt="Collision Demo" width="400"/>
---

## 📂 Cấu trúc thư mục 
```bash
BallWarrior/
├── src/
│   ├── Main.java                  # Điểm khởi chạy game (tạo JFrame, gắn GameEngine)
│   │
│   ├── core/                      # Lõi engine (tách biệt logic Arkanoid)
│   │   ├── GameEngine.java        # Khởi tạo game, gắn JFrame, chạy GameLoop
│   │   ├── InputHandler.java      # Quản lý phím bấm, lưu trạng thái key
│   │   ├── ResourceLoader.java    # Load ảnh, âm thanh, font từ thư mục assets
│   │   ├── ResourceSaver.java     # Lưu dữ liệu hoặc tài nguyên game (ví dụ: tiến trình, cài đặt)
│   │   ├── SoundManager.java      # Quản lý âm thanh (phát nhạc, hiệu ứng sfx)
│   │   └── SceneManager.java      # Quản lý chuyển đổi scene (Menu, Game, Pause…)
│   │   
│   ├── data/                      # Dữ liệu và logic gameplay của Arkanoid
│   │   ├── Equipped.java           # Lưu thông tin vật phẩm (ball, paddle, v.v.) đang được trang bị
│   │   ├── Inventory.java          # Quản lý kho vật phẩm người chơi sở hữu
│   │   ├── PlayerData.java         # Lưu dữ liệu người chơi: tiền, vật phẩm, trạng thái trang bị
│   │   └── SkinData.java           # Mô tả thông tin chi tiết của từng skin (loại, màu, ảnh, v.v.)
│   │
│   ├── entity/                    # Các đối tượng trong game (ball, paddle, brick, v.v.)
│   │   ├── Ball.java              # Quả bóng (di chuyển, nảy, va chạm)
│   │   ├── Brick.java             # Gạch (màu sắc, độ bền, bị phá hủy khi trúng bóng)
│   │   ├── Entity.java            # Lớp cha cho mọi thực thể (position, velocity, draw, update)
│   │   ├── Paddle.java            # Thanh trượt do người chơi điều khiển bằng bàn phím
│   │   └── Rarity.java            # Enum xác định độ hiếm (rarity) của skin hoặc vật phẩm
│   │
│   ├── game/                      # Logic gameplay Arkanoid
│   │   ├── collision/                 # Module va chạm (tách riêng, dễ mở rộng)
│   │   │   ├── CircleVsAABB.java      # Ball vs Paddle/Brick (AABB)
│   │   │   ├── CircleVsCircle.java    # Ball vs Ball / PowerUp (nếu cần)
│   │   │   ├── CollisionResult.java   # Data class (entity, hitPoint…)
│   │   │   ├── CollisionSystem.java   # Điều phối va chạm (tìm nearest collision)
│   │   │   └── CollisionUtils.java    # Hàm tiện ích (isBetween, getLineIntersection, circleLineIntersection…)
│   │   │
│   │   ├── core/                        # Module lõi điều phối entity và xử lý chính của GameWorld
│   │   │   ├── GameInitializer.java     # Khởi tạo dữ liệu gameplay (paddle, ball, brick, orb…)
│   │   │   ├── EntityManager.java       # Quản lý toàn bộ entity trong game (add/remove/update/render)
│   │   │   ├── CollisionProcessor.java  # Xử lý va chạm giữa các entity (Ball–Brick, Ball–Paddle, v.v.)
│   │   │   ├── OrbSpawner.java          # Quản lý việc spawn Mana Orb và Power-up khi brick bị phá
│   │   │   └── AssetRefresher.java      # Tải lại tài nguyên hoặc cập nhật asset trong runtime (ảnh, âm thanh…)
│   │   │
│   │   ├── skill/                # Hệ thống ghi và phát lại gameplay (replay system)
│   │   │   ├── active/
│   │   │   │   ├── LaserSkill.java
│   │   │   │   ├── ShieldSkill.java
│   │   │   │   └── TimeSlowSkill.java
│   │   │   │
│   │   │   ├── base/
│   │   │   │   ├── Skill.java              # Abstract base class
│   │   │   │   ├── ActiveSkill.java        # Base cho skill chủ động (Q/W/E)
│   │   │   │   └── PassiveSkill.java       # Base cho skill bị động (item rơi)
│   │   │   │
│   │   │   ├── PassiveSkill/
│   │   │   │   ├── ExpandSkill.java
│   │   │   │   ├── MultiBallSkill.java
│   │   │   │   ├── CatchSkill.java
│   │   │   │   ├── LaserPowerUpSkill.java
│   │   │   │   └── ExtraLifeSkill.java
│   │   │   │
│   │   │   ├── effect/         
│   │   │   │   ├── SkillEffect.java          # abstract class SkillEffect
│   │   │   │   ├── ExplosionEffect.java      # kế thừa SkillEffect, hiển thị vụ nổ
│   │   │   │   ├── FireBallEffect.java       # kế thừa SkillEffect, hiệu ứng bóng lửa
│   │   │   │   ├── ShieldEffect.java         # kế thừa SkillEffect, hiệu ứng shield
│   │   │   │   └── SkillEffectManager.java   # quản lý tất cả SkillEffect đang hoạt động
│   │   │   ├── ui/                            # Giao diện hiển thị kỹ năng
│   │   │   │   ├── SkillPanel.java            # Panel chính hiển thị các skill (HUD)
│   │   │   │   └── SkillIcon.java             # Đại diện 1 ô skill riêng (icon, cooldown overlay)
│   │   │   │
│   │   │   └── SkillManager.java       # Quản lý tất cả skill trong game
│   │   │
│   │   ├── replay/                # Hệ thống ghi và phát lại gameplay (replay system)
│   │   │   ├── ReplayRecorder.java   # Ghi lại hành động người chơi (input, frame state)
│   │   │   ├── ReplayData.java       # Cấu trúc dữ liệu lưu trữ thông tin replay (frame list, seed…)
│   │   │   └── ReplayPlayer.java     # Phát lại replay theo dữ liệu đã ghi
│   │   │
│   │   ├── GameWorld.java       # Điều phối tổng thể gameplay hiện tại
│   │   ├── LevelBuilder.java    # Tạo danh sách gạch từ dữ liệu level (LevelData)
│   │   ├── LevelData.java       # Định nghĩa cấu trúc dữ liệu của một màn chơi (bản đồ gạch, số hàng/cột, màu sắc)
│   │   ├── LevelManager.java    # Quản lý dữ liệu màn chơi (load/save từ JSON hoặc file)
│   │   └── ScoreSystem.java     # Quản lý điểm, combo và số mạng của người chơi
│   │
│   ├── ui/                            # Toàn bộ giao diện người dùng
│   │   ├── base/                      # Các lớp cơ sở và trừu tượng cho UI và scene
│   │   │   ├── AnchorType.java        # Enum xác định vị trí neo (anchor) của phần tử giao diện
│   │   │   ├── Button.java            # Lớp cơ sở cho các nút bấm trong giao diện
│   │   │   ├── Scene.java             # Lớp cha cho mọi màn hình (Menu, Shop, Game…)
│   │   │   ├── ButtonGroup.java       # Quản lý nhóm các nút (radio, toggle, v.v.)
│   │   │   └── TextElement.java       # Lớp cơ sở cho các phần tử hiển thị văn bản
│   │   │
│   │   ├── button/                    # Các loại nút kế thừa Button
│   │   │   ├── MenuButton.java         # Nút văn bản trung tâm, dùng trong menu chính
│   │   │   ├── LeftArrowButton.java    # Nút mũi tên trái (chuyển trang, chọn level, tùy chỉnh…)
│   │   │   ├── RightArrowButton.java   # Nút mũi tên phải (chuyển trang, chọn level, tùy chỉnh…)
│   │   │   ├── PlayButton.java         # Nút bắt đầu chơi (biểu tượng “Play”, dùng ở selectLevel)
│   │   │   ├── RectButton.java         # Nút hình chữ nhật tiêu chuẩn (shop, pause menu, confirm…)
│   │   │   └── IconButton.java         # Nút có biểu tượng hoặc hình ảnh (âm thanh, cài đặt, thoát…)
│   │   │
│   │   ├── element/                   # Các phần tử giao diện cơ bản kế thừa TextElement
│   │   │   ├── ComboLabel.java        # Hiển thị combo động với hiệu ứng màu và phóng to/thu nhỏ
│   │   │   └── Label.java             # Hiển thị văn bản tĩnh (không tương tác)
│   │   │
│   │   ├── panel/                     # Các panel phụ trong giao diện UI
│   │   │   ├── LevelPreviewPanel.java # Hiển thị preview của một màn chơi (dùng lại ở nhiều scene)
│   │   │   ├── GachaPanel.java        # Giao diện mở gacha để nhận skin hoặc phần thưởng
│   │   │   ├── GridPanel.java         # Bố cục hiển thị dạng lưới cho các phần tử UI
│   │   │   ├── InfoPanel.java         # Hiển thị thông tin chi tiết (skin, vật phẩm, chỉ số…)
│   │   │   └── SkinGrid.java          # Hiển thị danh sách skin dưới dạng lưới chọn
│   │   │
│   │   ├── scene/                     # Các màn hình giao diện riêng biệt
│   │   │   ├── GameOverScene.java     # Màn hình kết thúc (thắng / thua)
│   │   │   ├── GameScene.java       # Quản lý scene chính của gameplay (vòng lặp update/render, chuyển cảnh)
│   │   │   ├── LevelSelectScene.java  # Màn chọn màn chơi (hiển thị preview, metadata)
│   │   │   ├── MenuScene.java         # Menu chính (Play, Exit…)
│   │   │   ├── PauseScene.java        # Màn hình tạm dừng game
│   │   │   ├── ShopScene.java         # Màn hình shop (mua/chọn skin)
│   │   │   └── LevelEditorScene.java  # Màn tạo/sửa level (UI kéo thả, save/load)
│   │   │
│   │   └── HUD.java                   # Heads-up display trong gameplay (điểm, mạng…)
│   │
│   └── utils/                     # Các tiện ích và lớp hỗ trợ dùng chung
│       ├── Constants.java         # Chứa các hằng số toàn cục (WIDTH, HEIGHT, tốc độ, v.v.)
│       ├── MathUtils.java         # Cung cấp các hàm toán học 2D (giao điểm, góc, độ dài, v.v.)
│       ├── ScrollManager.java     # Quản lý cuộn nội dung trong giao diện (menu, danh sách, shop, v.v.)
│       ├── TextUtils.java         # Xử lý và định dạng văn bản (đo kích thước, canh giữa, v.v.)
│       └── Vector2D.java          # Lớp vector 2D (dx, dy, hỗ trợ các phép toán vector)
│
├── assets/                        # Tài nguyên (cùng bậc với src)
│   ├── images/                    # Hình ảnh
│   │   ├── ball.png
│   │   ├── paddle.png
│   │   └── brick_red.png
│   ├── sounds/                    # Âm thanh
│   │   ├── bounce.wav
│   │   ├── break.wav
│   │   └── powerup.wav
│   └── levels/                        # Map / màn chơi
│       ├── level1.json
│       ├── level2.json
│       └── custom/                    # Các màn do người chơi tạo
│           ├── myLevel1.json
│           └── testArena.json
│
├── CHANGELOG.md
└── README.md                      # Tài liệu mô tả project

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

Gameplay demo: ![BallWarrior2025](https://drive.google.com/file/d/1ioLZHJUYgCq26w67vLNrHM2oBPz7r5xQ/view?usp=drive_link)
