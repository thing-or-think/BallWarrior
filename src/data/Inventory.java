package data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Inventory {

    private List<SkinData> items;
    private transient List<SkinData> balls;
    private transient List<SkinData> paddles;

    public Inventory(Inventory inventory) {
        this.items = new ArrayList<>(inventory.getItems());
        // Gọi helper để khởi tạo list
        initializeTransientLists();
    }

    // THÊM: Constructor rỗng (GSON cần cái này)
    public Inventory() {
        // GSON sẽ nạp 'items' sau.
        // 'balls' và 'paddles' sẽ tạm thời là null.
    }

    // THÊM: Phương thức helper để khởi tạo 2 list transient
    private void initializeTransientLists() {
        if (this.items == null) {
            // Đề phòng trường hợp 'items' chưa được nạp
            this.balls = new ArrayList<>();
            this.paddles = new ArrayList<>();
            return;
        }

        this.balls = this.items.stream()
                .filter(item -> "ball".equals(item.getType()))
                .collect(Collectors.toList());
        this.paddles = this.items.stream()
                .filter(item -> "paddle".equals(item.getType()))
                .collect(Collectors.toList());
    }


    public List<SkinData> getItems() { return items; }

    // SỬA LẠI getBalls()
    public List<SkinData> getBalls() {
        if (balls == null) {
            // Nếu GSON vừa nạp xong, list này sẽ null.
            // Hãy khởi tạo nó ngay bây.
            initializeTransientLists();
        }
        return balls;
    }

    // SỬA LẠI getPaddles()
    public List<SkinData> getPaddles() {
        if (paddles == null) {
            // Tương tự
            initializeTransientLists();
        }
        return paddles;
    }

    public void setInventory(Inventory inventory) {
        // Đảm bảo các list cũng được cập nhật
        this.items = new ArrayList<>(inventory.getItems());
        initializeTransientLists();
    }
}
