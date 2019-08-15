package com.kotor4j.kotorgui.test;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture2D;
import com.kotor4j.kotorgui.GuiManager;
import com.kotor4j.kotorgui.border.SimpleLineCornerPictureBorder;
import com.kotor4j.kotorgui.components.ButtonComponent;
import com.kotor4j.kotorgui.components.PictureComponent;

/**
 * @author Dmitry
 */
public class GuiMain extends SimpleApplication {

    private GuiManager guiManager;

    public static void main(String[] args) {
        GuiMain guiMain = new GuiMain();
        guiMain.setShowSettings(false);
        AppSettings settings = new AppSettings(true);
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setVSync(true);
        guiMain.setSettings(settings);

        guiMain.start();
    }

    @Override
    public void simpleInitApp() {
        setDisplayStatView(false);
        getFlyByCamera().setDragToRotate(true);
        guiManager = new GuiManager(this);
        getInputManager().addRawInputListener(guiManager.getRawInputListener());

       /* PictureComponent pictureComponent = new PictureComponent(guiManager, "com/kotor4j/kotorgui/test/resources/background.png", 100, 75);
        pictureComponent.setXY(300, 10);
        guiManager.addComponent(pictureComponent);

        SimpleLineCornerPictureBorder border = new SimpleLineCornerPictureBorder((Texture2D) assetManager.loadTexture("com/kotor4j/kotorgui/test/resources/borderCorner.png"), (Texture2D) assetManager.loadTexture("com/kotor4j/kotorgui/test/resources/borderLine.png"));
        final PictureComponent pictureComponent2 = new PictureComponent(guiManager, "com/kotor4j/kotorgui/test/resources/background.png", 100, 75);
        pictureComponent2.setXY(200, 10);
        pictureComponent2.setBorder(border);
        guiManager.addComponent(pictureComponent2);
*/
        PictureComponent pic1 = new PictureComponentWithLogging(guiManager, "com/kotor4j/kotorgui/test/resources/level1.png", 100, 100) {

        };
        pic1.setName("Pic1");
        pic1.setXY(200, 200);
        guiManager.addComponent(pic1);

        PictureComponent pic2 = new PictureComponentWithLogging(guiManager, "com/kotor4j/kotorgui/test/resources/level2.png", 50, 50);
        pic2.setName("Pic2");
        pic2.setXY(10, 10);
        pic1.addChild(pic2);

        PictureComponent pic3 = new PictureComponentWithLogging(guiManager, "com/kotor4j/kotorgui/test/resources/level3.png", 76, 26);
        //SimpleLineCornerPictureBorder border2 = new SimpleLineCornerPictureBorder((Texture2D) assetManager.loadTexture("com/kotor4j/kotorgui/test/resources/borderCorner.png"), (Texture2D) assetManager.loadTexture("com/kotor4j/kotorgui/test/resources/borderLine.png"));
        //pic3.setBorder(border2);
        pic3.setName("Pic3");
        pic3.setXY(20, 20);
        pic2.addChild(pic3);

        PictureComponent pic4 = new PictureComponentWithLogging(guiManager, "com/kotor4j/kotorgui/test/resources/level4.png", 11, 11) {
            @Override
            public void onMouseDown(int x, int y, int globalX, int globalY, int mouseButton) {
                super.onMouseDown(x, y, globalX, globalY, mouseButton);
                captureInput();
            }

        };
        pic4.setName("Pic4");
        pic4.setXY(10, 10);
        pic3.addChild(pic4);

        ButtonComponent button = new ButtonComponent(guiManager, 100, 30);
        button.setText("Hello Button");
        button.setXY(10, 40);
        button.setBorder(new SimpleLineCornerPictureBorder((Texture2D) assetManager.loadTexture("com/kotor4j/kotorgui/test/resources/borderCorner.png"), (Texture2D) assetManager.loadTexture("com/kotor4j/kotorgui/test/resources/borderLine.png")));
        guiManager.addComponent(button);

        getInputManager().addMapping("left", new KeyTrigger(KeyInput.KEY_A));
        getInputManager().addMapping("right", new KeyTrigger(KeyInput.KEY_D));
        //getInputManager().addMapping("zorder+", new KeyTrigger(KeyInput.KEY_W));
        //getInputManager().addMapping("zorder-", new KeyTrigger(KeyInput.KEY_S));
        getInputManager().addMapping("top", new KeyTrigger(KeyInput.KEY_W));
        getInputManager().addMapping("bottom", new KeyTrigger(KeyInput.KEY_S));
        getInputManager().addMapping("width+", new KeyTrigger(KeyInput.KEY_ADD));
        getInputManager().addMapping("width-", new KeyTrigger(KeyInput.KEY_MINUS));

        getInputManager().addListener(new AnalogListener() {
            @Override
            public void onAnalog(String name, float value, float tpf) {
                switch (name) {
                    case "left": {
                        pic2.setXY(pic2.getX() - 2f, pic2.getY());
                        break;
                    }
                    case "right": {
                        pic2.setXY(pic2.getX() + 2f, pic2.getY());
                        break;
                    }
                    case "top": {
                        pic2.setXY(pic2.getX(), pic2.getY() - 2f);
                        break;
                    }
                    case "bottom": {
                        pic2.setXY(pic2.getX(), pic2.getY() + 2f);
                        break;
                    }
                    case "width+": {
                        pic2.setWidth(pic2.getWidth() + 2f);
                        break;
                    }
                    case "width-": {
                        pic2.setWidth(pic2.getWidth() - 2f);
                        break;
                    }

                    case "zorder+": {
                        pic2.setZOrder(pic2.getZOrder() + 0.1f);
                        break;
                    }
                    case "zorder-": {
                        pic2.setZOrder(pic2.getZOrder() - 0.1f);
                        break;
                    }
                }
            }
        }, "left", "right", "zorder+", "zorder-", "top", "bottom", "width+", "width-");
    }
    private float lasttpf;

    @Override
    public void simpleUpdate(float tpf) {
        guiManager.update(tpf);
        lasttpf = tpf;
    }

    @Override
    public void simpleRender(RenderManager rm) {
        guiManager.onRender(guiNode, rm, lasttpf);
    }

    private static class PictureComponentWithLogging extends PictureComponent {

        public PictureComponentWithLogging(GuiManager guiManager, String texturePath, float width, float height) {
            super(guiManager, texturePath, width, height);
        }

        @Override
        public void onMouseEnter() {
            System.out.println("Mouse entered in " + this.getName());
        }

        @Override
        public void onMouseExit() {
            System.out.println("Mouse exited in " + this.getName());
        }

        @Override
        public void onMouseDown(int x, int y, int globalX, int globalY, int mouseButton) {
            System.out.println("Mouse pressed in [" + this.getName() + "]\tx=" + x + "\ty=" + y + "\tgx=" + globalX + "\tgy=" + globalY + "\tbutton=" + mouseButton);
        }

        @Override
        public void onMouseUp(int x, int y, int globalX, int globalY, int mouseButton) {
            System.out.println("Mouse released in [" + this.getName() + "]\tx=" + x + "\ty=" + y + "\tgx=" + globalX + "\tgy=" + globalY + "\tbutton=" + mouseButton);
        }

        @Override
        public void onMouseMove(int x, int y, int globalX, int globalY, int dx, int dy) {
            System.out.println("Mouse move in [" + this.getName() + "]\tx=" + x + "\ty=" + y + "\tgx=" + globalX + "\tgy=" + globalY + "\tdx=" + dx + "\tdy=" + dy);
        }

        @Override
        public void onMouseWheel(int x, int y, int globalX, int globalY, int wheelScroll, int dWheelScroll) {
            System.out.println("Mouse wheel in [" + this.getName() + "]\tx=" + x + "\ty=" + y + "\tgx=" + globalX + "\tgy=" + globalY + "\tWheel=" + wheelScroll + "\tDWheel=" + dWheelScroll);
        }

    }

}
