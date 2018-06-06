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

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ToxicGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.ShadowParticle;
import com.shatteredpixel.pixeldungeonunleashed.items.Ankh;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfPsionicBlast;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.pixeldungeonunleashed.items.wands.WandOfDisintegration;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Death;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.NecromancerSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Necromancer extends Mob {

    private static final int MAX_ARMY_SIZE	= 4;

    {
        name = "necromancer";
        spriteClass = NecromancerSprite.class;

        HP = HT = 60;
        defenseSkill = 18;
        atkSkill = 20;
        dmgRed = 8;
        dmgMin = 8;
        dmgMax = 18;

        EXP = 8;
        maxLvl = 18;
        mobType = MOBTYPE_SPECIAL;
        TYPE_EVIL = true;
    }

    @Override
    public void die( Object cause ) {
        Dungeon.level.drop( new Ankh(), pos ).sprite.drop();

        super.die( cause );

        yell( "I am... death..." );
    }

    private boolean summon() {
        Mob DUMMY = new Mob() {};

        sprite.centerEmitter().start(Speck.factory(Speck.BONE), 0.4f, 2);
        Sample.INSTANCE.play(Assets.SND_CHALLENGE);

        int nMobs = 2;
        if (Random.Int( 1 ) == 0) {
            nMobs++;
            if (Random.Int( 2 ) == 0) {
                nMobs++;
            }
        }

        ArrayList<Integer> candidates = new ArrayList<>();

        for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
            int p = pos + Level.NEIGHBOURS8[i];
            if (Actor.findChar( p ) == null && (Level.passable[p] || Level.avoid[p])) {
                candidates.add( p );
            }
        }

        ArrayList<Integer> respawnPoints = new ArrayList<>();

        while (nMobs > 0 && candidates.size() > 0) {
            int index = Random.index( candidates );

            DUMMY.pos = candidates.get( index );

            respawnPoints.add( candidates.remove( index ) );
            nMobs--;
        }

        for (Integer point : respawnPoints) {
            Mob mob = (Random.Int(2) == 0) ? new Zombie() : new Skeleton();
            mob.scaleMob();

            mob.state = mob.WANDERING;
            GameScene.add( mob, 2f );
            ScrollOfTeleportation.appear( mob, point );
        }
        yell( "Arise my children!" );
        return true;
    }

    @Override
    public void notice() {
        super.notice();
        yell("Your soul will be mine!" );
        summon();
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        if (damage > 3) {
            int healingAmt = Random.Int(0, damage / 4);
            HP = Math.min(HT, HP + healingAmt);
            sprite.emitter().burst(ShadowParticle.UP, 2);
        }
        return damage;
    }

    @Override
    public String description() {
        return "The necromancer has come to this place to add to his power.  He brings only death and wants you for his evil army.";
    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<>();
    static {
        RESISTANCES.add( ToxicGas.class );
        RESISTANCES.add( Death.class );
        RESISTANCES.add( ScrollOfPsionicBlast.class );
        RESISTANCES.add( WandOfDisintegration.class );
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<>();
    static {
        IMMUNITIES.add( Paralysis.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }

}