/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015 David Mitchell
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

import com.shatteredpixel.pixeldungeonunleashed.sprites.AssassinSprite;

public class Assassin extends Mob {

    {
        name = "assassin";
        spriteClass = AssassinSprite.class;

        HP = HT = 30;
        defenseSkill = 10;
        atkSkill = 14;
        dmgRed = 5;
        dmgMin = 3;
        dmgMax = 10;

        EXP = 6;
        maxLvl = 15;
        TYPE_EVIL = true;
    }

    @Override
    public String description() {
        return
                "A trainee Assassin, eager to make his first kill so he can complete his training.";
    }
}
