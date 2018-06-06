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
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Poison;

import com.shatteredpixel.pixeldungeonunleashed.sprites.RedSnakeSprite;
import com.watabou.utils.Random;

public class RedSnake extends Mob {

    {
        name = "red snake";
        spriteClass = RedSnakeSprite.class;

        HP = HT = 12;
        defenseSkill = 3;
        atkSkill = 9;
        dmgRed = 1;
        dmgMin = 1;
        dmgMax = 5;
        EXP = 3;

        TYPE_ANIMAL = true;
        maxLvl = 10;
    }

    @Override
    public int attackProc(Char enemy, int damage ) {

        if (Random.Int( 3 ) == 0) {
            Buff.affect(enemy, Poison.class).set(Random.Int(2, 4) * Poison.durationFactor(enemy));
        }

        return damage;
    }

    @Override
    public String description() {
        return
                "Rare red dungeon snakes thrive in the dark and damp environments found in caves and sewers. They are known to be poisonous!";
    }
}

