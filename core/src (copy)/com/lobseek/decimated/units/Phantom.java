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
package com.lobseek.decimated.units;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.lobseek.decimated.Main;
import com.lobseek.decimated.components.Actor;
import com.lobseek.decimated.components.Bullet;
import com.lobseek.decimated.components.Point;
import com.lobseek.decimated.components.Sprite;
import com.lobseek.decimated.components.Unit;
import com.lobseek.decimated.components.Weapon;
import com.lobseek.decimated.particles.Explosion;
import static com.lobseek.utils.Math.*;

/**
 *
 * @author Yew_Mentzaki
 */
public class Phantom extends Unit {

    private static Sprite explosion_0 = new Sprite("plasma/explosion0");

    class PlasmaBullet extends Bullet {

        float waiter = 0.25f;

        public PlasmaBullet(Unit from, Unit to, float x, float y) {
            super(from, to, x, y);
            speed = 500;
            angle += (Main.R.nextFloat() - 0.5f) / 2f;
            vx = cos(angle) * speed;
            vy = sin(angle) * speed;
            sprite = new Sprite("rocket");
            sprite.setScale(0.5f);
            detonationDistance = 100;
            lifeTime = 5 + Main.R.nextInt(10);
        }

        @Override
        public void explode(Unit to) {
            to.hit(35, from);
            room.add(new Explosion(x, y, 300, explosion_0, 20, 200));
        }

        @Override
        public void act(float delta) {
            if (to.hp <= 0 || to.immortal) {
                Unit lto = to;
                float dist = 1000;
                for (Actor a : room.actors) {
                    if (a instanceof Unit) {
                        Unit u = (Unit) a;
                        if (u.hp > 0 && !u.immortal && room.players[from.owner].isEnemy(u.owner)) {
                            float d = dist(x, y, u.x, u.y);
                            if (d < dist) {
                                to = u;
                                dist = d;
                            }
                        }
                    }
                }
                if (lto == to) {
                    waiter -= delta;
                    if (waiter < 0) {
                        room.add(new Explosion(x, y, 300, explosion_0, 20, 100));
                        lifeTime = -1000;
                    }
                }
            }
            super.act(delta);
        }

        @Override
        public void move(float delta) {
            float angle = atan2(to.y - y, to.x - x);
            if (dist(0, 0, vx + cos(angle) * 40, vy + sin(angle) * 40)
                    < speed) {
                vx += cos(angle) * 30;
                vy += sin(angle) * 30;
            }
            this.angle = atan2(vy, vx);
            x += delta * vx;
            y += delta * vy;
            x += cos(angle) * 150 * delta;
            y += sin(angle) * 150 * delta;
        }
    }

    class PhantomWeapon extends Weapon {

        public PhantomWeapon(float y) {
            x = 5;
            this.y = y;
            cx = 20;
            turnSpeed = 3;
            speed = 500;
            range = 500;
            ammo = maxAmmo = 3;
            reloadTime = 0.1f;
            reloadAmmoTime = 5;
        }

        @Override
        public void shoot(Unit to, Point from) {
            room.add(new PlasmaBullet(on, to, from.x, from.y));
            on.visiblity = 1;
        }

        @Override
        public void setSprite(String name) {

        }

        @Override
        public void render(Batch batch, float delta, Point point, Color parentColor) {

        }

        @Override
        public void renderShadow(Batch batch, float delta) {

        }
    }

    public Phantom(float xcord, float ycord, float angle, int owner) {
        super(xcord, ycord, angle, owner);
        weapons = new Weapon[]{
            new PhantomWeapon(4),
            new PhantomWeapon(0),
            new PhantomWeapon(-4)
        };
        setSprite("phantom");
        width = height = 50;
        mass = 15;
        hp = maxHp = 250;
        speed = 350;
        turnSpeed = 2;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (visiblity > 0) {
            visiblity = Math.max(0, visiblity - 0.1f * delta);
        }
    }

}
