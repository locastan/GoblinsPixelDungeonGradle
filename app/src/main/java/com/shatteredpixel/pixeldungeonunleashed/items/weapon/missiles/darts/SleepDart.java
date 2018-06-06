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
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.FlavourBuff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Sleep;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class SleepDart extends TippedDart {
	
	{
		name = "sleep dart";
		image = ItemSpriteSheet.SLEEP_DART;
	}
	
	@Override
	public void proc(Char attacker, final Char defender, int damage) {
		
		//need to delay this so damage from the dart doesn't break the sleep
		new FlavourBuff(){
			{actPriority = 100;}
			public boolean act() {
				Buff.affect( defender, Sleep.class );
				return super.act();
			}
		}.attachTo(defender);
		
		super.proc(attacker, defender, damage);
	}

	@Override
	public String desc() {
		return
				"The spike on each of these darts is designed to pin it to its target " +
						"while delivering powerful narcotics.";
	}

	@Override
	public Item random() {
		quantity = Random.Int( 3, 4 );
		return this;
	}

	@Override
	public int price() {
		return 20 * quantity;
	}
}
