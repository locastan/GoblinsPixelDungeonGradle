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
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfLevitation;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Room;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.ParalyticTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.SummoningTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.ToxicTrap;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.Trap;
import com.watabou.utils.Random;

public class TrapsPainter extends Painter {

	public static void paint( Level level, Room room ) {
		 
		Class traps[] = new Class[]{
			ToxicTrap.class, ToxicTrap.class, ToxicTrap.class,
			ParalyticTrap.class, ParalyticTrap.class,
			!Dungeon.bossLevel(Dungeon.depth + 1) ? null : SummoningTrap.class};
		fill( level, room, Terrain.WALL );

		Class trap = Random.element(traps);

		if (trap == null){
			fill(level, room, 1, Terrain.CHASM);
		} else {
			fill(level, room, 1, Terrain.TRAP);
		}
		
		Room.Door door = room.entrance();
		door.set( Room.Door.Type.REGULAR );
		
		int lastRow = level.map[room.left + 1 + (room.top + 1) * Level.WIDTH] == Terrain.CHASM ? Terrain.CHASM : Terrain.EMPTY;

		int x = -1;
		int y = -1;
		if (door.x == room.left) {
			x = room.right - 1;
			y = room.top + room.height() / 2;
			fill( level, x, room.top + 1, 1, room.height() - 1 , lastRow );
		} else if (door.x == room.right) {
			x = room.left + 1;
			y = room.top + room.height() / 2;
			fill( level, x, room.top + 1, 1, room.height() - 1 , lastRow );
		} else if (door.y == room.top) {
			x = room.left + room.width() / 2;
			y = room.bottom - 1;
			fill( level, room.left + 1, y, room.width() - 1, 1 , lastRow );
		} else if (door.y == room.bottom) {
			x = room.left + room.width() / 2;
			y = room.top + 1;
			fill( level, room.left + 1, y, room.width() - 1, 1 , lastRow );
		}

		int pos = x + y * Level.WIDTH;
		if (Random.Int( 3 ) == 0) {
			if (lastRow == Terrain.CHASM) {
				set( level, pos, Terrain.EMPTY );
			}
			level.drop( prize( level ), pos ).type = Heap.Type.CHEST;
		} else {
			set( level, pos, Terrain.PEDESTAL );
			level.drop( prize( level ), pos );
		}

		for (y = room.top; y < room.bottom; y++) {
			for (x = room.left; x < room.right; x++) {
				int cell = (y * Level.WIDTH) + x;
				if (level.map[cell] == Terrain.TRAP) {
					try {
						level.setTrap(((Trap) trap.newInstance()).reveal(), cell);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		level.addItemToSpawn( new PotionOfLevitation() );
	}

    private static Item prize( Level level ) {

        Item prize;

        if (Random.Int(3) != 0){
            prize = level.findPrizeItem();
            if (prize != null)
                return prize;
        }

        //1 floor set higher in probability, never cursed
        do {
            if (Random.Int(2) == 0) {
                prize = Generator.randomWeapon((Dungeon.depth / 5) + 1);
            } else {
                prize = Generator.randomArmor((Dungeon.depth / 5) + 1);
            }
        } while (prize.cursed || Challenges.isItemBlocked(prize));

        //33% chance for an extra update.
        if (Random.Int(3) == 0){
            prize.upgrade();
        }

        return prize;
    }
}
