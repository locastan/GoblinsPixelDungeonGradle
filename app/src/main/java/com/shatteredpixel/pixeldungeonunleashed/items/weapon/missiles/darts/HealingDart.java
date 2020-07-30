/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

package com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.darts;

import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Healing;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Regeneration;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfHealing;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class HealingDart extends TippedDart {
	
	{
		name = "healing dart";
		image = ItemSpriteSheet.HEALING_DART;
		MIN = 1;
		MAX = 1;
	}
	
	@Override
	public void proc(Char attacker, Char defender, int damage) {

		//heals 30 hp at base, scaling with character HT
		Buff.affect( defender, Healing.class ).setHeal((int)(0.5f*defender.HT + 30), 0.333f, 0);
		PotionOfHealing.cure(defender);
        // return damage = 1 to superfunction.
		super.proc(attacker, defender, 1);
	}

	@Override
	public String desc() {
		return
				"The spike on each of these darts is designed to pin it to its target " +
						"while a bit of healing potion is injected.";
	}

	@Override
	public Item random() {
		quantity = Random.Int( 3, 6 );
		return this;
	}

	@Override
	public int price() {
		return 15 * quantity;
	}
	
}
