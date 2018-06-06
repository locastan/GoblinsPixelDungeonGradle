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
package com.shatteredpixel.pixeldungeonunleashed.items.potions;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;

public class PotionOfExperience extends Potion {

	{
		name = "Potion of Experience";
		initials = "Ex";

		bones = true;
	}
	
	@Override
	public void apply( Hero hero ) {
		setKnown();
		hero.earnExp( hero.maxExp() );
	}

	@Override
	public void shatter( int cell ) {
		for (Mob mob : Dungeon.level.mobs) {
			if (mob.pos == cell) {
				mob.atkSkill += (Dungeon.depth / 4);
				mob.defenseSkill += (Dungeon.depth / 3);
				mob.HT += Dungeon.depth;
				mob.HP += Dungeon.depth;
				GLog.w("The " + mob.description() + " looks tougher!");
			}
		}

		super.shatter(cell);
	}

	@Override
	public String desc() {
		return
			"The storied experiences of multitudes of battles reduced to liquid form, " +
			"this draught will instantly raise your experience level.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 80 * quantity : super.price();
	}
}
