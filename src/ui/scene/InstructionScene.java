package ui.scene;

import core.InputHandler;
import core.ResourceLoader;
import core.SceneManager;
import ui.base.Scene;
import utils.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class InstructionScene extends Scene {

    private final SceneManager sceneManager;
    private BufferedImage background;

    public InstructionScene(InputHandler input, SceneManager sceneManager) {
        super("InstructionScene", input);
        this.sceneManager = sceneManager;
        initUI();
    }

    @Override
    public void initUI() {
        background = ResourceLoader.loadImage("assets/images/Bg/book.jpg");
    }

    @Override
    public void update() {
        if (input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ESCAPE) ||
                input.isKeyJustPressed(java.awt.event.KeyEvent.VK_ENTER)) {
            sceneManager.goToMenu();
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(background,0,0,getWidth(),getHeight(),null);
    }
}
