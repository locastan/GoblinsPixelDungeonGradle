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

import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.EternalFlame;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Fire;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Freezing;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.utils.BArray;
import com.watabou.utils.PathFinder;

public class PotionOfFrost extends Potion {
	
	private static final int DISTANCE	= 2;
	
	{
		name = "Potion of Frost";
		initials = "Fr";
	}
	
	@Override
	public void shatter( int cell ) {

		PathFinder.buildDistanceMap( cell, BArray.not( Level.losBlocking, null ), DISTANCE );

		Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );

		boolean visible = false;
		for (int i=0; i < Level.LENGTH; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				visible = Freezing.affect( i, fire ) || visible;
			}
		}

		EternalFlame eternalFlame = (EternalFlame)Dungeon.level.blobs.get( EternalFlame.class );
		if (eternalFlame != null) {
			if (eternalFlame.cur[cell] > 0) {
				GLog.i("The potion extinguishes the magical flames");
			}
			eternalFlame.clear(cell);
		}

		if (visible) {
			splash( cell );
			Sample.INSTANCE.play( Assets.SND_SHATTER );

			setKnown();
		}
	}

	@Override
	public int hungerMods() {
		return 5; // reduces hunger by 5%
	}

	@Override
	public String desc() {
		return
			"Upon exposure to open air this chemical will evaporate into a freezing cloud, causing " +
			"any creature that contacts it to be frozen in place unable to act and move. " +
			"The freezing effect is much stronger if the environment is wet.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 50 * quantity : super.price();
	}
}
