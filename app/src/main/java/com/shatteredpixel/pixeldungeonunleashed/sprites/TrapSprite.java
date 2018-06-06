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
package com.shatteredpixel.pixeldungeonunleashed.sprites;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.DungeonTilemap;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.Trap;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class TrapSprite extends Image {

	private static TextureFilm frames;

	private int pos = -1;

	public TrapSprite() {
		super(Assets.TRAPS);

		if (frames == null) {
			frames = new TextureFilm( texture, 16, 16 );
		}

		origin.set(8, 12);
	}

	public TrapSprite( int image ) {
		this();
		reset( image );
	}

	public void reset( Trap trap ) {

		revive();
// Fixed endless mode problem with traps beyond level 36.
		if (Dungeon.depth == 1) {
			reset(trap.image);
		} else {
			int depth = Dungeon.depth;
            while (((depth - 1) / 6) > 5) {
				depth = depth - 30;
			}
			reset(trap.image + (((depth - 1) / 6) * 8));
		}
		alpha( 1f );

		pos = trap.pos;
		x = (pos % Level.WIDTH) * DungeonTilemap.SIZE;
		y = (pos / Level.WIDTH) * DungeonTilemap.SIZE;

	}

	public void reset( int image ) {
		frame( frames.get( image ) );
	}

}
