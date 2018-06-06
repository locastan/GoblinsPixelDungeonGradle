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
import com.shatteredpixel.pixeldungeonunleashed.effects.Lightning;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CharSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShockingDart extends TippedDart {
	
	{
		name = "shocking dart";
		image = ItemSpriteSheet.SHOCKING_DART;
	}
	
	@Override
	public void proc(Char attacker, Char defender, int damage) {
		
		defender.damage(Random.NormalIntRange(8, 12), this);
		
		CharSprite s = defender.sprite;
		ArrayList<Lightning.Arc> arcs = new ArrayList<>();
		arcs.add(new Lightning.Arc(defender.pos, defender.pos-1));
		arcs.add(new Lightning.Arc(defender.pos-1, defender.pos+1));
		defender.sprite.parent.add( new Lightning( arcs, null ) );
		
		super.proc(attacker, defender, damage);
	}

	@Override
	public String desc() {
		return
				"The spike on each of these darts is designed to pin it to its target " +
						"while the electric capacitors discharge into it.";
	}

	@Override
	public Item random() {
		quantity = Random.Int( 3, 5 );
		return this;
	}

	@Override
	public int price() {
		return 15 * quantity;
	}
}
