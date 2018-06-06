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

import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ToxicGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Amok;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Ooze;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Poison;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Sleep;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Terror;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Vertigo;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.ShadowParticle;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfPsionicBlast;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.enchantments.Death;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.sprites.RottingFistSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class RottingFist extends Mob {

    private static final int REGENERATION	= 4;

    {
        name = "rotting fist";
        spriteClass = RottingFistSprite.class;

        HP = HT = 300;
        defenseSkill = 25;
        atkSkill = 36;
        dmgRed = 15;
        dmgMin = 24;
        dmgMax = 36;

        EXP = 0;

        TYPE_EVIL = true;
        state = WANDERING;
    }

    private String TXT_DESC = "This Rotting Fist was once the right hand of the Sacred.";

    @Override
    public int attackProc( Char enemy, int damage ) {
        if (Random.Int(3) == 0) {
            Buff.affect(enemy, Ooze.class);
            enemy.sprite.burst( 0xFF000000, 5 );
        }

        return damage;
    }

    @Override
    public boolean act() {

        if (Level.water[pos] && HP < HT) {
            sprite.emitter().burst( ShadowParticle.UP, 2 );
            HP += REGENERATION;
        }

        return super.act();
    }

    @Override
    public String description() {
        return TXT_DESC;

    }

    private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
    static {
        RESISTANCES.add( ToxicGas.class );
        RESISTANCES.add( Death.class );
        RESISTANCES.add( ScrollOfPsionicBlast.class );
    }

    @Override
    public HashSet<Class<?>> resistances() {
        return RESISTANCES;
    }

    private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
    static {
        IMMUNITIES.add( Amok.class );
        IMMUNITIES.add( Sleep.class );
        IMMUNITIES.add( Paralysis.class );
        IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Poison.class );
        IMMUNITIES.add( Vertigo.class );
    }

    @Override
    public HashSet<Class<?>> immunities() {
        return IMMUNITIES;
    }
}
