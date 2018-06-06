/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Goblins Pixel Dungeon
 * Copyright (C) 2016 Mario Braun
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

import com.shatteredpixel.pixeldungeonunleashed.items.food.MysteryMeat;
import com.shatteredpixel.pixeldungeonunleashed.sprites.RatSprite;

public class Rat extends Mob {

	{
		name = "marsupial rat";
		spriteClass = RatSprite.class;
		
		HP = HT = 8;
		defenseSkill = 2;
		atkSkill = 8;
		dmgRed = 1;
		dmgMin = 1;
		dmgMax = 4;

		maxLvl = 6;
		TYPE_ANIMAL = true;

		loot = MysteryMeat.class;
		lootChance = 0.5f;
	}
	
	@Override
	public String description() {
		return
			"Marsupial rats are aggressive but rather weak denizens " +
			"of the sewers. They have a nasty bite, but are only life threatening in large numbers.\n\n" +
			"Some say there is good eating on a rat, but you probably need to cook it.";
	}
}
