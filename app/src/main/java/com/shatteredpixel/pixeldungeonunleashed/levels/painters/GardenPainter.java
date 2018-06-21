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
package com.shatteredpixel.pixeldungeonunleashed.levels.painters;

import com.shatteredpixel.pixeldungeonunleashed.Challenges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Foliage;
import com.shatteredpixel.pixeldungeonunleashed.items.EasterEgg;
import com.shatteredpixel.pixeldungeonunleashed.items.wands.WandOfRegrowth;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Room;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.plants.Prismweed;
import com.shatteredpixel.pixeldungeonunleashed.plants.YumyuckMoss;
import com.shatteredpixel.pixeldungeonunleashed.plants.Firebloom;
import com.shatteredpixel.pixeldungeonunleashed.plants.Stormvine;
import com.shatteredpixel.pixeldungeonunleashed.plants.Sungrass;
import com.watabou.utils.Random;

import java.util.Calendar;

public class GardenPainter extends Painter {

	public static void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.HIGH_GRASS );
		fill( level, room, 2, Terrain.GRASS );
		
		room.entrance().set( Room.Door.Type.REGULAR );

		if (Dungeon.isChallenged(Challenges.NO_FOOD)) {
			if (Random.Int(2) == 0){
				level.plant(new Sungrass.Seed(), room.random());
			}
		} else {
			int bushes = Random.Int(3);
			int special = Random.Int(5);
			if (bushes == 0) {
				level.plant(new Sungrass.Seed(), room.random());
			} else if (bushes == 1) {
				level.plant(new YumyuckMoss.Seed(), room.random());
			} else if (special == 0) {
				level.plant(new Sungrass.Seed(), room.random());
				level.plant(new YumyuckMoss.Seed(), room.random());
			} else if (special == 1) {
				level.plant(new Firebloom.Seed(), room.random());
				level.plant(new Stormvine.Seed(), room.random());
            } else if (special == 2) {
                level.plant(new WandOfRegrowth.Seedpod.Seed(), room.random());
                level.plant(new WandOfRegrowth.Dewcatcher.Seed(), room.random());
			} else if (special == 3) {
				level.plant(new Prismweed.Seed(), room.random());
				level.plant(new YumyuckMoss.Seed(), room.random());
			}
		}

		if (Random.Int(10)==0 && (Calendar.getInstance().get(Calendar.MONTH) == Calendar.APRIL || Calendar.getInstance().get(Calendar.MONTH) == Calendar.MAY)){
			int pos;
			do {pos = room.random();}
			while (level.heaps.get(pos) != null);
			level.drop(new EasterEgg(), pos);
		}

		Foliage light = (Foliage)level.blobs.get( Foliage.class );
		if (light == null) {
			light = new Foliage();
		}
		for (int i=room.top + 1; i < room.bottom; i++) {
			for (int j=room.left + 1; j < room.right; j++) {
				light.seed( j + Level.WIDTH * i, 1 );
			}
		}
		level.blobs.put( Foliage.class, light );
	}
}
