package ui.scene;

import data.LeaderboardEntry;
import core.InputHandler;
import core.ResourceLoader;
import core.ResourceSaver;
import core.SceneManager;
import data.GameData;
import data.PlayerData;
import ui.base.AnchorType;
import ui.base.Button;
import ui.base.Scene;
import ui.button.BuyButton;
import ui.button.MenuButton;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerSelectScene extends Scene {

    private final SceneManager sceneManager;
    private final GameData gameData;
    private final List<Button> buttons = new ArrayList<>();
    private JTextField nameField;

    private String feedbackMessage = "";
    private Color feedbackColor = Color.RED;
    private long feedbackTimer = 0L;
    private static final long FEEDBACK_DURATION = 3000;

    private List<PlayerData> playerList;
    private int selectedPlayerIndex = -1;

    private PlayerData templateData;

    // BIẾN pendingAction ĐÃ BỊ XÓA

    public PlayerSelectScene(InputHandler input, SceneManager sceneManager, GameData gameData) {
        super("PlayerSelectScene", input);
        this.sceneManager = sceneManager;
        this.gameData = gameData;

        this.templateData = ResourceLoader.loadPlayerDataTemplate();
        if (this.templateData == null) {
            System.err.println("CRITICAL ERROR: Không thể tải playerData.json template!");
        }

        // Gán chỉ số selected mặc định là người chơi hiện tại
        String currentPlayerName = gameData.getCurrentPlayerName();
        this.playerList = gameData.getAllPlayers();
        for(int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getPlayerName().equals(currentPlayerName)) {
                this.selectedPlayerIndex = i;
                break;
            }
        }

        refreshPlayerList();

        initUI();

        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleAddPlayer();
                }
            }
        });
    }

    public void refreshPlayerList() {
        this.playerList = gameData.getAllPlayers();
        this.buttons.clear();
        this.removeAll();

        Font font = new Font("Serif", Font.PLAIN, 28);
        FontMetrics fm = getFontMetrics(font);

        int yPos = 150;
        for (int i = 0; i < playerList.size(); i++) {
            PlayerData player = playerList.get(i);
            String name = player.getPlayerName();
            final int index = i;

            // Sửa 'name' TRƯỚC KHI tạo nút
            if (i == selectedPlayerIndex) {
                name = ">> " + name + " <<";
            }

            // Tạo nút VỚI TÊN ĐÃ ĐƯỢC SỬA ĐỔI
            MenuButton playerButton = new MenuButton(name, Constants.WINDOW_WIDTH / 2, yPos, fm, () -> {
                selectedPlayerIndex = index;
                refreshPlayerList(); // <-- Sửa lỗi: Gọi trực tiếp
            });

            // In đậm cho người chơi ĐANG ĐĂNG NHẬP
            if (player.getPlayerName().equals(gameData.getCurrentPlayerName())) {
                playerButton.setFont(font.deriveFont(Font.BOLD, 32f));
            }

            buttons.add(playerButton);
            yPos += 45;
        }

        Font smallFont = new Font("Serif", Font.PLAIN, 24);

        BuyButton switchBtn = new BuyButton("Switch to Selected", Constants.WINDOW_WIDTH / 2 - 200, 600, smallFont, AnchorType.CENTER_MIDDLE);
        switchBtn.setActivity(this::handleSwitchPlayer);
        buttons.add(switchBtn);

        BuyButton deleteBtn = new BuyButton("Delete Selected", Constants.WINDOW_WIDTH / 2 + 200, 600, smallFont, AnchorType.CENTER_MIDDLE);
        deleteBtn.setActivity(this::handleDeletePlayer);
        buttons.add(deleteBtn);

        BuyButton backBtn = new BuyButton("Back to Menu", Constants.WINDOW_WIDTH / 2, 660, smallFont, AnchorType.CENTER_MIDDLE);
        backBtn.setActivity(sceneManager::goToMenu);
        buttons.add(backBtn);

        initUI();

        this.revalidate();
        this.repaint();
    }

    @Override
    protected void initUI() {
        setLayout(null);

        nameField = new JTextField();
        nameField.setBounds(Constants.WINDOW_WIDTH / 2 - 150, 80, 200, 40);
        nameField.setFont(new Font("Serif", Font.PLAIN, 20));
        add(nameField);

        JButton addButton = new JButton("Add Player");
        addButton.setBounds(Constants.WINDOW_WIDTH / 2 + 60, 80, 120, 40);
        addButton.setFont(new Font("Serif", Font.BOLD, 16));
        addButton.addActionListener(e -> handleAddPlayer());
        add(addButton);
    }

    private void handleAddPlayer() {
        String newName = nameField.getText().trim();
        if (newName.isEmpty()) {
            setFeedback("Player name cannot be empty.", Color.RED);
            return;
        }

        boolean isDuplicate = gameData.getAllPlayers().stream()
                .anyMatch(p -> p.getPlayerName().equalsIgnoreCase(newName));

        if (isDuplicate) {
            setFeedback("Player name '" + newName + "' already exists.", Color.RED);
            return;
        }

        if (this.templateData == null) {
            setFeedback("Error: Player template file is missing.", Color.RED);
            return;
        }

        PlayerData newPlayer = new PlayerData(this.templateData);
        newPlayer.setPlayerName(newName);
        newPlayer.getCoins().set(1000);

        gameData.getAllPlayers().add(newPlayer);
        ResourceSaver.saveGameData(gameData);

        setFeedback("Player '" + newName + "' added successfully.", Color.GREEN);
        nameField.setText("");

        // Gọi trực tiếp
        refreshPlayerList();
    }

    private void handleSwitchPlayer() {
        if (selectedPlayerIndex == -1 || selectedPlayerIndex >= playerList.size()) {
            setFeedback("Please select a player to switch to.", Color.RED);
            return;
        }

        String newPlayerName = playerList.get(selectedPlayerIndex).getPlayerName();
        gameData.setCurrentPlayerName(newPlayerName);
        ResourceSaver.saveGameData(gameData);

        // Chuyển scene (an toàn)
        sceneManager.reloadApplicationState();
    }

    private void handleDeletePlayer() {
        if (selectedPlayerIndex == -1 || selectedPlayerIndex >= playerList.size()) {
            setFeedback("Please select a player to delete.", Color.RED);
            return;
        }

        if (playerList.size() <= 1) {
            setFeedback("Cannot delete the last player.", Color.RED);
            return;
        }

        PlayerData playerToDelete = playerList.get(selectedPlayerIndex);
        String nameToDelete = playerToDelete.getPlayerName(); // <-- Lấy tên trước khi xóa

        // --- BẮT ĐẦU SỬA LỖI 2 ---

        // 1. Xóa người chơi khỏi danh sách chính
        gameData.getAllPlayers().remove(playerToDelete);

        // 2. Xóa người chơi khỏi TẤT CẢ leaderboard
        // Lấy map leaderboard
        var leaderboards = gameData.getLeaderboards();
        // Duyệt qua từng danh sách điểm của từng level (mỗi 'scoreList' là 1 List<LeaderboardEntry>)
        for (List<LeaderboardEntry> scoreList : leaderboards.values()) {
            // Xóa mọi entry (dòng điểm) có tên trùng với người chơi bị xóa
            scoreList.removeIf(entry -> entry.playerName.equals(nameToDelete));
        }
        System.out.println("Đã xóa tất cả điểm số của " + nameToDelete + " khỏi leaderboard.");

        // --- KẾT THÚC SỬA LỖI 2 ---

        // Cập nhật người chơi hiện tại nếu cần
        if (nameToDelete.equals(gameData.getCurrentPlayerName())) {
            gameData.setCurrentPlayerName(gameData.getAllPlayers().get(0).getPlayerName());
        }

        // Lưu lại file GameData.json đã được cập nhật
        ResourceSaver.saveGameData(gameData);
        setFeedback("Player '" + nameToDelete + "' deleted.", Color.GREEN);
        selectedPlayerIndex = -1;

        // Làm mới UI
        refreshPlayerList();
    }

    private void setFeedback(String message, Color color) {
        this.feedbackMessage = message;
        this.feedbackColor = color;
        this.feedbackTimer = System.currentTimeMillis() + FEEDBACK_DURATION;
    }

    // --- SỬA LỖI CONCURRENT MODIFICATION (Cách 2) ---
    @Override
    protected void update() {
        // Hẹn giờ tự động xóa thông báo
        if (feedbackTimer > 0 && System.currentTimeMillis() > feedbackTimer) {
            this.feedbackMessage = "";
            this.feedbackTimer = 0L;
        }

        int mx = input.getMouseX();
        int my = input.getMouseY();

        // Tạo một bản sao của danh sách CHỈ ĐỂ DUYỆT
        List<Button> buttonsToUpdate = new ArrayList<>(buttons);

        for (Button button : buttonsToUpdate) {
            button.setHovered(button.contains(mx, my));

            if (button.isHovered() && input.consumeClick()) {
                // Gọi onClick() trên nút.
                // Hành động này (vd: refreshPlayerList) sẽ sửa đổi 'this.buttons' (list gốc),
                // nhưng không ảnh hưởng đến 'buttonsToUpdate' (list sao chép)
                // vì vậy vòng lặp này vẫn an toàn.
                button.onClick();

                // Dừng ngay lập tức để chỉ xử lý 1 click mỗi frame
                break;
            }
        }
    }
    // --- KẾT THÚC SỬA LỖI ---

    @Override
    protected void render(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 36));
        g2.drawString("Player Management", Constants.WINDOW_WIDTH / 2 - 150, 40);

        if (feedbackMessage != null && !feedbackMessage.isEmpty()) {
            g2.setFont(new Font("Serif", Font.PLAIN, 18));
            g2.setColor(feedbackColor);

            FontMetrics fm = g2.getFontMetrics();
            int msgWidth = fm.stringWidth(feedbackMessage);

            g2.drawString(feedbackMessage, (Constants.WINDOW_WIDTH - msgWidth) / 2, 70);
        }

        // Vòng lặp render an toàn (vì nó chỉ đọc, không ghi)
        for (Button button : buttons) {
            button.draw(g2);
        }
    }
}
