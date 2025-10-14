package ui.base;

import ui.button.IconButton;
import java.util.ArrayList;
import java.util.List;

public class ButtonGroup {
    private List<IconButton> buttons = new ArrayList<>();

    public void add(IconButton button) {
        buttons.add(button);
    }

    public void select(IconButton button) {
        for (IconButton b : buttons) {
            b.setClicked(b == button);
        }
    }
}
