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
package com.lobseek.decimated.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.lobseek.decimated.Main;
import com.lobseek.decimated.actors.Base;
import com.lobseek.decimated.actors.Test;
import com.lobseek.decimated.components.Room;
import com.lobseek.decimated.components.Sprite;
import com.lobseek.decimated.components.Touch;
import com.lobseek.decimated.gui.Button;
import com.lobseek.decimated.gui.Image;
import com.lobseek.decimated.gui.SpawnBar;
import com.lobseek.decimated.gui.Trigger;
import com.lobseek.utils.ColorFabricator;
import com.lobseek.utils.MapManager;
import static com.lobseek.utils.Math.*;
import com.lobseek.utils.MusicLoader;
import com.lobseek.utils.Sound;
import com.lobseek.widgets.LWAlignment;
import com.lobseek.widgets.LWContainer;

/**
 *
 * @author Yew_Mentzaki
 */
public class GameScreen extends Screen {

    public Room room;
    public LWContainer menu, unitList, settings;
    public static com.badlogic.gdx.graphics.g2d.Sprite background;

    void beginGame() {
        menu.hide();
        if (unitList != null) {
            unitList.hide();
        }
        if (room == null) {
            room = new Room(this, 1024, 64);
            room.player = 1;
            room.players[1].ai = false;
            unitList = room.players[room.player].getUnitList();
            Button play = new Button("menu.play") {
                @Override
                public void tapUp(Touch t) {
                    beginGame();
                }

            };
            play.setAlign(LWAlignment.RIGHT, LWAlignment.BOTTOM);
            play.y = 156f / 2f;
            play.x = -447f / 2f;
            play.hide();
            unitList.add(play);
            play = new Button("menu.exit") {
                @Override
                public void tapUp(Touch t) {
                    unitList.hide();
                    menu.show();
                    remove(room);
                    room.destroy();
                    room = null;
                }

            };
            play.setAlign(LWAlignment.LEFT, LWAlignment.BOTTOM);
            play.y = 156f / 2f;
            play.x = 447f / 2f;
            play.hide();
            unitList.add(play);
            add(unitList);
            add(room);
            MapManager mp = new MapManager(2 + Main.R.nextInt(7), 1, room);
            mp.generate();
        }
        room.pause();
    }

    public void initMenu() {
        menu = new LWContainer() {
            float alpha = 0;

            @Override
            public void act(float delta) {
                if (isVisible()) {
                    alpha = Math.min(1, alpha + delta);
                } else {
                    alpha = Math.max(0, alpha - delta);
                }
                super.act(delta);
            }

            @Override
            public void draw(Batch b) {
                background.setSize(parent.width, parent.height);
                background.setPosition(0, 0);
                background.setColor(ColorFabricator.neon(alpha));
                background.draw(b);
                width = parent.width;
                height = parent.height;
                super.draw(b);
            }

            @Override
            public boolean checkSwipe(Touch t) {
                super.checkSwipe(t);
                return isVisible();
            }

            @Override
            public boolean checkTapDown(Touch t) {
                super.checkTapDown(t);
                return isVisible();
            }

            @Override
            public boolean checkTapUp(Touch t) {
                super.checkTapUp(t);
                return isVisible();
            }

        };
        Image i = new Image("gui/logo");
        i.setAlign(LWAlignment.CENTER);
        i.y = 180;
        menu.add(i);
        Button play = new Button("menu.play") {
            @Override
            public void tapUp(Touch t) {
                beginGame();
            }

        };
        play.setAlign(LWAlignment.CENTER);
        play.y = 50 - 140 * 0;
        play.x = -300 / 2 - 75;
        menu.add(play);
        play = new Button("menu.settings") {
            @Override
            public void tapUp(Touch t) {
                menu.hide();
                settings.show();
            }
            
        };
        play.setAlign(LWAlignment.CENTER);
        play.y = 50 - 140 * 0;
        play.x = +300 / 2 + 75;
        menu.add(play);
        play = new Button("menu.exit") {
            @Override
            public void tapUp(Touch t) {
                System.exit(0);
            }

        };
        play.setAlign(LWAlignment.CENTER);
        play.y = 50 - 140 * 1;
        /*
        menu.add(play);
        play = new Trigger("menu.settings");
        play.setAlign(LWAlignment.CENTER);
        play.y = 90 - 140 * 1;
        ((Trigger)(play)).done();*/
        menu.add(play);
        add(menu);
    }

    /*
     Звук, музыка, тумблеры high contrast, additional particles и fast/fancy graphics.
     Тумблер ака tuggle switch ещё надо сделать. Юзай любой спрайт, я потом нарисую
     */
    public void initSettings() {
        settings = new LWContainer() {
            float alpha = 0;

            @Override
            public void act(float delta) {
                if (isVisible()) {
                    alpha = Math.min(1, alpha + delta);
                } else {
                    alpha = Math.max(0, alpha - delta);
                }
                super.act(delta);
            }

            @Override
            public void draw(Batch b) {
                background.setSize(parent.width, parent.height);
                background.setPosition(0, 0);
                background.setColor(ColorFabricator.neon(alpha));
                background.draw(b);
                width = parent.width;
                height = parent.height;
                super.draw(b);
            }

            @Override
            public boolean checkSwipe(Touch t) {
                super.checkSwipe(t);
                return isVisible();
            }

            @Override
            public boolean checkTapDown(Touch t) {
                super.checkTapDown(t);
                return isVisible();
            }

            @Override
            public boolean checkTapUp(Touch t) {
                super.checkTapUp(t);
                return isVisible();
            }

        };
        Trigger trigger = new Trigger("settings.simple"){
            @Override
            public void valueChanged() {
                Main.simple = value;
            }
            
        };
        trigger.setAlign(LWAlignment.LEFT, LWAlignment.TOP);
        trigger.x = 250;
        trigger.y = -75;
        trigger.value = Main.simple;
        settings.add(trigger);
        trigger = new Trigger("settings.contrast"){
            @Override
            public void valueChanged() {
                Main.contrast = value;
            }
            
        };
        trigger.setAlign(LWAlignment.LEFT, LWAlignment.CENTER);
        trigger.x = 250;
        trigger.value = Main.contrast;
        settings.add(trigger);
        trigger = new Trigger("settings.particles"){
            @Override
            public void valueChanged() {
                Main.particles = value;
            }
            
        };
        trigger.setAlign(LWAlignment.LEFT, LWAlignment.BOTTOM);
        trigger.x = 250;
        trigger.y = 75;
        trigger.value = Main.particles;
        settings.add(trigger);
        
        SpawnBar bar = new SpawnBar(){
            @Override
            public void valueChanged(float delta) {
                Sound.volume = value;
            }
            
        };
        bar.setAlign(LWAlignment.RIGHT);
        bar.x = -250;
        bar.value = Sound.volume;
        bar.sprite = new Sprite("gui/sound", true);
        settings.add(bar);
        
        
        bar = new SpawnBar(){
            @Override
            public void valueChanged(float delta) {
                Main.scale = value + 1;
                Main.gameScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
            
        };
        bar.setAlign(LWAlignment.RIGHT);
        bar.x = -125;
        bar.value = Main.scale - 1;
        bar.sprite = new Sprite("gui/sound", true);
        settings.add(bar);
        
        bar = new SpawnBar(){
            @Override
            public void valueChanged(float delta) {
                MusicLoader.setVolume(value);
            }
            
        };
        bar.setAlign(LWAlignment.RIGHT);
        bar.x = -375;
        bar.value = MusicLoader.getVolume();
        bar.sprite = new Sprite("gui/music", true);
        settings.add(bar);
        
        Button play = new Button("menu.play") {
            @Override
            public void tapUp(Touch t) {
                Main.updateSettings();
                settings.hide();
                menu.show();
            }

        };
        play.setAlign(LWAlignment.RIGHT, LWAlignment.BOTTOM);
        play.x = -250;
        play.y = 75;
        settings.add(play);
        settings.hide();
        add(settings);
    }

    public GameScreen() {
        background = new com.badlogic.gdx.graphics.g2d.Sprite(
                new Texture(Gdx.files.internal("background.png")));
        background.getTexture().setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        Main.gameScreen = this;

        initMenu();
        initSettings();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

}
