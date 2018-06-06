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
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Bless;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class HolyDart extends TippedDart {

	{
		name = "holy dart";
		image = ItemSpriteSheet.HOLY_DART;
	}
	
	@Override
	public void proc(Char attacker, Char defender, int damage) {
		
		Buff.affect(defender, Bless.class, 20f);

		
		super.proc(attacker, defender, damage);
	}

	@Override
	public String desc() {
		return
				"The tip of these darts holds a small vial of holy water.";
	}

	@Override
	public Item random() {
		quantity = Random.Int( 3, 6 );
		return this;
	}

	@Override
	public int price() {
		return 8 * quantity;
	}
}
