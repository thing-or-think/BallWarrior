package ui.base;

import ui.button.IconButton;
import java.util.ArrayList;
import java.util.List;

public class ButtonGroup {
    private List<Button> buttons = new ArrayList<>();

    public void add(Button button) {
        buttons.add(button);
    }

    public void select(Button button) {
        for (Button b : buttons) {
            b.setClicked(b == button);
        }
    }
}
