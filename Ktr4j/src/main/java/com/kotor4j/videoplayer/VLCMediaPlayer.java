package com.kotor4j.videoplayer;

import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

public class VLCMediaPlayer {

    private final String videoPath;

    private Image image;
    private final Texture2D texture;
    private final BufferedImage bufferedImage;

    private final AWTLoader awtloader = new AWTLoader();
    private final BufferFormat videoFormat;
    private final BufferFormatCallback bufferFormatCallback;
    private final JmeRenderCallbackAdapter jrca;

    DirectMediaPlayerComponent mediaPlayerComponent;

    public VLCMediaPlayer(String videoPath, int width, int height) {
        this.discover();
        this.videoPath = videoPath;
        image = new Image(Image.Format.RGBA16I, width, height, null);
        texture = new Texture2D(image);
        bufferedImage = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .createCompatibleImage(width, height);

        videoFormat = new RV32BufferFormat(width, height);
        bufferFormatCallback = new BufferFormatCallback() {
            @Override
            public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
                return videoFormat;
            }
        };

        jrca = new JmeRenderCallbackAdapter(bufferedImage);
        mediaPlayerComponent = new DirectMediaPlayerComponent(bufferFormatCallback) {
            @Override
            protected RenderCallback onGetRenderCallback() {
                return jrca;
            }
        };
    }

    private boolean discover() {
        boolean found = new NativeDiscovery().discover();
        System.out.println("VLC Native Discovery Status:" + found);
        return found;
    }

    public void play() {
        mediaPlayerComponent.getMediaPlayer().playMedia(this.videoPath);
    }
    
    public boolean isPlay(){
        return mediaPlayerComponent.getMediaPlayer().isPlaying();
    }
    
    public void stop(){
        mediaPlayerComponent.getMediaPlayer().stop();
        mediaPlayerComponent.release(true);
    }

    private class JmeRenderCallbackAdapter extends RenderCallbackAdapter {
        public JmeRenderCallbackAdapter(BufferedImage image) {
            super(new int[image.getWidth() * image.getHeight()]);
        }

        @Override
        protected void onDisplay(DirectMediaPlayer mediaPlayer, int[] rgbBuffer) {
            bufferedImage.setRGB(0, 0, image.getWidth(), image.getHeight(), rgbBuffer, 0, image.getWidth());
            image = awtloader.load(bufferedImage, true);
            texture.setImage(image);
        }
    }

    public Texture2D getTexture() {
        return this.texture;
    }
}
