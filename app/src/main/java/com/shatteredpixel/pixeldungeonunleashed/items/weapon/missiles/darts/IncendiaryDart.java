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

import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Blob;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Fire;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.watabou.utils.Random;

public class IncendiaryDart extends TippedDart {

	{
		name = "incendiary dart";
		image = ItemSpriteSheet.INCENDIARY_DART;

		STR = 10;
	}
	
	@Override
	protected void onThrow( int cell ) {
		Char enemy = Actor.findChar( cell );
		if ((enemy == null || enemy == curUser) && Level.flamable[cell]) {
			GameScene.add(Blob.seed(cell, 4, Fire.class));
			Dungeon.level.drop(new Dart(), cell).sprite.drop();
		} else{
			super.onThrow(cell);
		}
	}
	
	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		Buff.affect( defender, Burning.class ).reignite( defender );
		super.proc( attacker, defender, damage );
	}

	@Override
	public String desc() {
		return
				"The spike on each of these darts is designed to pin it to its target " +
						"while the unstable compounds strapped to its length burst into brilliant flames.";
	}

	@Override
	public Item random() {
		quantity = Random.Int( 3, 6 );
		return this;
	}

	@Override
	public int price() {
		return 5 * quantity;
	}
}
