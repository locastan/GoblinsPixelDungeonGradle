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
package com.shatteredpixel.pixeldungeonunleashed.levels.traps;

import java.util.ArrayList;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Bestiary;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.InfiniteBestiary;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.watabou.utils.Random;

public class SummoningTrap extends Trap {

	private static final float DELAY = 2f;
	
	private static final Mob DUMMY = new Mob() {};
	
	// 0x770088
	{
		name = "Summoning trap";
		image = 7;
	}

	@Override
	public void activate() {

		if (Dungeon.bossLevel() && (Dungeon.difficultyLevel != Dungeon.DIFF_ENDLESS)) {
			return;
		}

		int nMobs = 1;
		if (Random.Int( 2 ) == 0) {
			nMobs++;
			if (Random.Int( 2 ) == 0) {
				nMobs++;
			}
		}

		// It's complicated here, because these traps can be activated in chain

		ArrayList<Integer> candidates = new ArrayList<>();

		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
			int p = pos + Level.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Level.passable[p] || Level.avoid[p])) {
				candidates.add( p );
			}
		}

		ArrayList<Integer> respawnPoints = new ArrayList<>();

		while (nMobs > 0 && candidates.size() > 0) {
			int index = Random.index( candidates );

			DUMMY.pos = candidates.get( index );

			respawnPoints.add( candidates.remove( index ) );
			nMobs--;
		}

		for (Integer point : respawnPoints) {
			Mob mob = Bestiary.mob( Dungeon.depth );
			if (Dungeon.difficultyLevel == Dungeon.DIFF_ENDLESS) {
                mob.infiniteScaleMob(Dungeon.depth + 5);
			} else {
                mob.scaleMob();
            }
			mob.state = mob.WANDERING;
			GameScene.add( mob, DELAY );
			ScrollOfTeleportation.appear( mob, point );
		}

		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) {heap.summon();}

	}
}
