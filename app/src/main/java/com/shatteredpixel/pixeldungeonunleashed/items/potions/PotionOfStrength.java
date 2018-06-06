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

import com.shatteredpixel.pixeldungeonunleashed.Badges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CharSprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;

public class PotionOfStrength extends Potion {

	{
		name = "Potion of Strength";
		initials = "St";

		bones = true;
	}

	@Override
	public void shatter( int cell ) {
		for (Mob mob : Dungeon.level.mobs) {
			if (mob.pos == cell) {
				mob.dmgMax += Dungeon.depth;
				GLog.w("The " + mob.description() + " looks stronger!");
			}
		}

		super.shatter(cell);
	}

	@Override
	public void apply( Hero hero ) {
		setKnown();
		
		hero.STR++;
		hero.sprite.showStatus( CharSprite.POSITIVE, "+1 str" );
		GLog.p( "Newfound strength surges through your body." );
		
		Badges.validateStrengthAttained();
	}

	@Override
	public int hungerMods() {
		return 5; // reduces hunger by 5%
	}

	@Override
	public String desc() {
		return
			"This powerful liquid will course through your muscles, " +
			"permanently increasing your strength by one point.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 100 * quantity : super.price();
	}
}
