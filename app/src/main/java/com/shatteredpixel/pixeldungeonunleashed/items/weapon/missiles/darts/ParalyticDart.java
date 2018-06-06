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
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class ParalyticDart extends TippedDart {
	
	{
		{
			name = "curare dart";
			image = ItemSpriteSheet.CURARE_DART;

			STR = 14;
		}
	}

	public ParalyticDart() {
		this( 1 );
	}

	public ParalyticDart( int number ) {
		super();
		quantity = number;
	}
	
	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		Buff.prolong( defender, Paralysis.class, 5f );
		super.proc( attacker, defender, damage );
	}

	@Override
	public String desc() {
		return
				"These little evil darts don't do much damage but they can paralyze " +
						"the target leaving it helpless and motionless for some time.";
	}
	@Override
	public Item random() {
		quantity = Random.Int( 2, 5 );
		return this;
	}

	@Override
	public int price() {
		return 8 * quantity;
	}
}
