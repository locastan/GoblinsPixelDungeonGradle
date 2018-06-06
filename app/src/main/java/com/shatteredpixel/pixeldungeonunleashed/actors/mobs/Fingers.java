/*
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015  David Mitchell
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import java.util.HashSet;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ToxicGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Cripple;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Roots;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Weakness;
import com.shatteredpixel.pixeldungeonunleashed.items.food.MysteryMeat;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Death;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.sprites.SquidSprite;
import com.watabou.utils.Random;

public class Fingers extends Mob {

    {
        name = "Mr. Fingers";
        spriteClass = SquidSprite.class;
        dmgRed = Dungeon.depth;
        dmgMin = Dungeon.depth * 2;
        dmgMax = 4 + Dungeon.depth * 3;
        TYPE_ANIMAL = true;

        baseSpeed = 2f;

        EXP = 0;
    }

    public Fingers() {
        super();

        HP = HT = 18 + Dungeon.depth * 6;
        defenseSkill = 14 + Dungeon.depth * 2;
        atkSkill = 24 + Dungeon.depth * 2;
    }

    @Override
    protected boolean act() {
        if (!Level.water[pos]) {
            die( null );
            return true;
        } else {
            //this causes squid to move away when a door is closed on them.
            Dungeon.level.updateFieldOfView( this );
            enemy = chooseEnemy();
            if (state == this.HUNTING &&
                    !(enemy != null && enemy.isAlive() && Level.fieldOfView[enemy.pos] && enemy.invisible <= 0)){
                state = this.WANDERING;
                int oldPos = pos;
                int i = 0;
                do {
                    i++;
                    target = Dungeon.level.randomDestination();
                    if (i == 100) return true;
                } while (!getCloser(target));
                moveSprite( oldPos, pos );
                return true;
            }

            return super.act();
        }
    }

    @Override
    public void die( Object cause ) {
        Dungeon.level.drop( new MysteryMeat(), pos ).sprite.drop();
        super.die(cause);
    }

    @Override
    protected boolean getCloser( int target ) {

        if (rooted) {
            return false;
        }

        int step = Dungeon.findPath( this, pos, target,
                Level.water,
                Level.fieldOfView );
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean getFurther( int target ) {
        int step = Dungeon.flee(this, pos, target,
                Level.water,
                Level.fieldOfView);
        if (step != -1) {
            move( step );
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String description() {
        return
                "All fingers and joints, this undead creature isn't normally found in dungeons. " +
                        "They were created specifically to protect flooded treasure vaults.";
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        if (Level.adjacent( pos, enemy.pos )) {
            return true;
        } else {
            return false; // DSM-xxxx troubleshoot this next block...
            //Ballistica attack = new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE);
            //return (attack.collisionPos == enemy.pos);
        }
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (enemy == Dungeon.hero) {
            if (Random.Int(4) == 0) {
                Buff.prolong(enemy, Cripple.class, Cripple.DURATION);
            } else if (Random.Int(4) == 1) {
                Buff.prolong(enemy, Weakness.class, Weakness.DURATION);
            }
        }

        return damage;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Burning.class );
        IMMUNITIES.add( Paralysis.class );
        IMMUNITIES.add( ToxicGas.class );
        IMMUNITIES.add( Roots.class );
        IMMUNITIES.add( Death.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

    /*
    @Override
    public boolean reset() {
        return true;
    }
   */
}
