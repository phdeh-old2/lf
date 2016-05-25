/*
 * This program is open software.
 * You may:
 *  * buy this program with Google Play or App Store.
 *  * read code, change code.
 *  * compile and run code if you bought this program.
 *  * share your modification with people who bought this program.
 * You may not:
 *  * sell this program.
 *  * sell your modification of this program as independent product.
 *  * share your modification with people who have no legal copy of
 *                                                    this program.
 *  * share compiled program with people who have no legal copy of it. 
 */
package com.lobseek.decimated;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.lobseek.decimated.gui.Font;
import com.lobseek.decimated.screens.GameScreen;
import com.lobseek.decimated.screens.SplashScreen;
import com.lobseek.utils.SoundLoader;
import com.lobseek.widgets.LWLocale;
import java.util.Random;

/**
 *
 * @author Yew_Mentzaki
 */
public class Main extends Game {

    public static final String NAME = "Decimare";
    public static final Random R = new Random(Long.MAX_VALUE - System.nanoTime());
    public static final AssetManager AM = new AssetManager();
    public static Main main;
    public static int fps;
    public static long nanos;
    public static TextureAtlas atlas, small_atlas;
    private boolean loaded;
    public static boolean simple, contrast, particles = true;
    private static long time;
    private static int frames;
    public static Texture laser;
    public static SoundLoader sl = new SoundLoader();
    public static GameScreen gameScreen;
    public static float scale = 1;

    /**
     * Called when application is loading. Handle load of all textures, sounds,
     * fonts and locale files.
     */
    public void load() {
        float que = 1.2f;
        if (Gdx.graphics.getDensity() > que) {
            scale = Gdx.graphics.getDensity() / que;
        }
        if (loaded) {
            return;
        }
        loaded = true;
        if (!simple) {
            AM.load("atlas.pack", TextureAtlas.class);
        }
        AM.load("small_atlas.pack", TextureAtlas.class);
        AM.load("font.pack", TextureAtlas.class);
        AM.load("font_simple.pack", TextureAtlas.class);
        Thread soundLoaderThread = new Thread() {
            @Override
            public void run() {
                sl.load(AM);
            }

        };
        soundLoaderThread.start();
        laser = new Texture(Gdx.files.internal("laser.png"));
        AM.finishLoading();
        LWLocale.init();
        if (!simple) {
            atlas = main.AM.get("atlas.pack");
            small_atlas = main.AM.get("small_atlas.pack");
            if (Gdx.graphics.getDensity() > que) {
                for (Texture t : atlas.getTextures()) {
                    t.setFilter(Texture.TextureFilter.Linear,
                            Texture.TextureFilter.Linear);
                }
                for (Texture t : small_atlas.getTextures()) {
                    t.setFilter(Texture.TextureFilter.Linear,
                            Texture.TextureFilter.Linear);
                }
            }
        } else {
            atlas = small_atlas = main.AM.get("small_atlas.pack");
        }
        Font.atlas = main.AM.get("font.pack");
        Font.atlas_simple = main.AM.get("font_simple.pack");
    }

    /**
     * Called when application was just launched.
     */
    @Override
    public void create() {
        main = this;
        setScreen(new SplashScreen());
    }

    @Override
    public void pause() {
        super.pause(); 
        if(gameScreen != null && gameScreen.room != null){
            gameScreen.room.stop();
        }
    }

    /**
     * Handles render and counts frames per second.
     */
    @Override
    public void render() {
        frames++;
        long t = System.currentTimeMillis();
        long n = System.nanoTime();
        if (t - time > 1000) {
            time = t;
            fps = frames;
            frames = 0;
        }
        super.render();
        n = System.nanoTime() - n;
        nanos = (nanos * 10 + n) / 11;
    }
}
