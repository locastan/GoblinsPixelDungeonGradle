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

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Poison;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class PoisonDart extends TippedDart {
	
	{
		name = "poison dart";
		image = ItemSpriteSheet.POISON_DART;
	}
	
	@Override
	public void proc(Char attacker, Char defender, int damage) {
		
		Buff.affect( defender, Poison.class ).set( 3 + Dungeon.depth / 3 );
		
		super.proc(attacker, defender, damage);
	}

	@Override
	public String desc() {
		return
				"The spike on each of these darts is designed to pin it to its target " +
						"while concentrated poison is slowly injected into it.";
	}

	@Override
	public Item random() {
		quantity = Random.Int( 3, 8 );
		return this;
	}

	@Override
	public int price() {
		return 15 * quantity;
	}
}
