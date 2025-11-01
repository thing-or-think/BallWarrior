package ui.scene;

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

    private List<PlayerData> playerList;
    private int selectedPlayerIndex = -1;

    private PlayerData templateData;

    // --- BIẾN MỚI ĐỂ SỬA LỖI ---
    private Runnable pendingAction = null;
    // ---

    public PlayerSelectScene(InputHandler input, SceneManager sceneManager, GameData gameData) {
        super("PlayerSelectScene", input);
        this.sceneManager = sceneManager;
        this.gameData = gameData;

        this.templateData = ResourceLoader.loadPlayerDataTemplate();
        if (this.templateData == null) {
            System.err.println("CRITICAL ERROR: Không thể tải playerData.json template!");
        }

        refreshPlayerList();

        initUI();

        // Chỉ thêm KeyListener MỘT LẦN
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
        this.removeAll(); // Xóa các thành phần Swing cũ

        Font font = new Font("Serif", Font.PLAIN, 28);
        FontMetrics fm = getFontMetrics(font);

        int yPos = 150;
        for (int i = 0; i < playerList.size(); i++) {
            PlayerData player = playerList.get(i);
            String name = player.getPlayerName();
            if (name.equals(gameData.getCurrentPlayerName())) {
                name = ">> " + name + " <<";
            }

            final int index = i; // Biến final cho lambda
            MenuButton playerButton = new MenuButton(name, Constants.WINDOW_WIDTH / 2, yPos, fm, () -> {
                selectedPlayerIndex = index;
                refreshPlayerList(); // Gọi đệ quy vẫn an toàn vì nó nằm trong pendingAction
            });

            if (i == selectedPlayerIndex) {
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

        // Thêm lại các thành phần UI
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
        newPlayer.getCoins().set(1000); // Set coin khởi điểm

        gameData.getAllPlayers().add(newPlayer);
        ResourceSaver.saveGameData(gameData);

        setFeedback("Player '" + newName + "' added successfully.", Color.GREEN);
        nameField.setText("");
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
        gameData.getAllPlayers().remove(playerToDelete);

        if (playerToDelete.getPlayerName().equals(gameData.getCurrentPlayerName())) {
            gameData.setCurrentPlayerName(gameData.getAllPlayers().get(0).getPlayerName());
        }

        ResourceSaver.saveGameData(gameData);
        setFeedback("Player '" + playerToDelete.getPlayerName() + "' deleted.", Color.GREEN);
        selectedPlayerIndex = -1;
        refreshPlayerList();
    }

    private void setFeedback(String message, Color color) {
        this.feedbackMessage = message;
        this.feedbackColor = color;
    }

    // --- SỬA LỖI CONCURRENT MODIFICATION ---
    @Override
    protected void update() {
        // 1. Thực thi hành động đang chờ (nếu có)
        // Hành động này (ví dụ: refreshPlayerList) sẽ sửa đổi danh sách 'buttons'
        if (pendingAction != null) {
            pendingAction.run();
            pendingAction = null; // Xóa hành động sau khi thực thi
        }

        int mx = input.getMouseX();
        int my = input.getMouseY();

        // 2. Vòng lặp 'for' bây giờ đã an toàn,
        // vì nó đang lặp qua danh sách 'buttons' mới (hoặc cũ) mà không bị sửa đổi
        for (Button button : buttons) {
            button.setHovered(button.contains(mx, my));
            if (button.isHovered() && input.consumeClick()) {
                // 3. Thay vì gọi button.onClick() ngay lập tức,
                // chúng ta gán nó vào 'pendingAction' để thực thi ở frame TỚI.
                pendingAction = button::onClick;

                // 4. Dừng vòng lặp ngay khi tìm thấy một click
                // để ngăn 'pendingAction' bị ghi đè nếu có 2 nút bị click
                break;
            }
        }
    }

    @Override
    protected void render(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 36));
        g2.drawString("Player Management", Constants.WINDOW_WIDTH / 2 - 150, 40);

        g2.setFont(new Font("Serif", Font.PLAIN, 18));
        g2.setColor(feedbackColor);
        g2.drawString(feedbackMessage, Constants.WINDOW_WIDTH / 2 - 150, 130);

        for (Button button : buttons) {
            button.draw(g2);
        }
    }
}
