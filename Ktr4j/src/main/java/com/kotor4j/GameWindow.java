package com.kotor4j;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.kotor4j.appstates.console.CommandEvent;
import com.kotor4j.appstates.console.CommandListener;
import com.kotor4j.appstates.console.ConsoleAppState;
import com.kotor4j.appstates.gui.GuiAppState;
import com.kotor4j.materials.MaterialWithClipping;
import com.kotor4j.resourcemanager.Context;
import com.kotor4j.resourcemanager.ResourceManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * @author Dmitry
 */
public class GameWindow extends SimpleApplication {

    private Context context;
    private ConsoleAppState console;
    private GuiAppState guiAppState;

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        getFlyByCamera().setDragToRotate(true);
        initContext();
        initConsole();
        initGui();
        inputManager.reset();
    }

    private void testGui() {
        try {
            BufferedImage im = ImageIO.read(new File("g:\\kotor_Extracted\\testExtraction\\tpc\\swpc_tex_gui\\SCR_DEF_SN.tpc.png"));
            final int w = im.getWidth();
            final int h = im.getHeight();
            Quad quad = new Quad(w, h);
            final Geometry geom = new Geometry("quad", quad);
            Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat2.setTexture("ColorMap", Utils.textureFromImage(im, "texture"));

            geom.setMaterial(mat2);
            geom.setLocalTranslation(0, 0, 0);

            final Node parentNode = new Node();
            parentNode.attachChild(geom);
            parentNode.setLocalTranslation(200, 200, 0);
            guiNode.attachChild(parentNode);

            im = ImageIO.read(new File("g:\\kotor_Extracted\\testExtraction\\tpc\\swpc_tex_gui\\uparrow.tpc.png"));
            quad = new Quad(im.getWidth(), im.getHeight());
            final Geometry geom2 = new Geometry("quad", quad);
            MaterialWithClipping mat3 = new MaterialWithClipping(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat3.setClipping(200, 200, w, h);
            mat3.setTexture("ColorMap", Utils.textureFromImage(im, "texture"));
            geom2.setMaterial(mat3);
            geom2.setLocalTranslation(0, 0, 0);
            parentNode.attachChild(geom2);

            im = ImageIO.read(new File("g:\\kotor_Extracted\\testExtraction\\tpc\\swpc_tex_gui\\ybutton.tpc.png"));
            quad = new Quad(im.getWidth(), im.getHeight());
            final Geometry geom3 = new Geometry("quad", quad);
            mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat2.setTexture("ColorMap", Utils.textureFromImage(im, "texture"));

            geom3.setMaterial(mat2);
            geom3.setLocalTranslation(200, 300, 0);
            guiNode.attachChild(geom3);

            inputManager.addMapping("l", new KeyTrigger(KeyInput.KEY_H));
            inputManager.addMapping("r", new KeyTrigger(KeyInput.KEY_K));
            inputManager.addMapping("u", new KeyTrigger(KeyInput.KEY_U));
            inputManager.addMapping("d", new KeyTrigger(KeyInput.KEY_J));

            inputManager.addListener(new AnalogListener() {
                @Override
                public void onAnalog(String name, float value, float tpf) {
                    switch (name) {
                        case "l":
                            geom2.move(-0.1f, 0, 0);
                            break;
                        case "r":
                            geom2.move(0.1f, 0, 0);
                            break;
                        case "u":
                            geom2.move(0f, 0.1f, 0);
                            break;
                        case "d":
                            geom2.move(0f, -0.1f, 0);
                            break;
                    }
                }
            }, "l", "r", "u", "d");
        } catch (IOException ex) {
            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Context getGameContext() {
        return context;
    }

    private void initContext() {
        try {
            context = loadContext();
            context.getResourceManager().scanWholeResourcesList(false);
            context.getResourceManager().reconstructResourceMap();
        } catch (IOException ex) {
            Logger.getLogger(GameWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initConsole() {
        console = new ConsoleAppState();
        console.setUseAlphaOnConsoleGeoms(true);
        console.setConsoleUsesFullViewPort(true);

        console.registerCommand("loadgui", new CommandListener() {
            @Override
            public void execute(CommandEvent evt) {
                try {
                    guiAppState.clearGui();
                    guiAppState.loadGui(evt.getParser().get(0));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    console.appendConsoleError("ERR:" + ex.getMessage());
                }
            }
        });

        console.registerCommand("exit", new CommandListener() {
            @Override
            public void execute(CommandEvent evt) {
                System.exit(0);
            }
        });

        console.registerCommand("help", new CommandListener() {
            @Override
            public void execute(CommandEvent evt) {
                console.appendConsole("loadgui nameOfGui - load gui file");
                console.appendConsole("exit - Exit from application");
            }
        });
        stateManager.attach(console);
    }

    private void initGui() {
        guiAppState = new GuiAppState();
        stateManager.attach(guiAppState);
    }

    @Override
    public void update() {
        super.update();
    }

    private Context loadContext() throws IOException {
        Context tContext = ResourceManager.loadContext(false);
        return tContext;
    }
    
     public AppSettings getSettings() {
        return settings;
    }
}
