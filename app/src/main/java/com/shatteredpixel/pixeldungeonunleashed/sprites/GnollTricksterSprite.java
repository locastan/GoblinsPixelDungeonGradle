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
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.darts.ParalyticDart;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class GnollTricksterSprite extends MobSprite {

	private Animation cast;

	public GnollTricksterSprite() {
		super();

		texture( Assets.GNOLL );

		TextureFilm frames = new TextureFilm( texture, 12, 15 );

		idle = new MovieClip.Animation( 2, true );
		idle.frames( frames, 11, 11, 11, 12, 11, 11, 12, 12 );

		run = new MovieClip.Animation( 12, true );
		run.frames( frames, 15, 16, 17, 18 );

		attack = new MovieClip.Animation( 12, false );
		attack.frames( frames, 13, 14, 11 );

		cast = attack.clone();

		die = new MovieClip.Animation( 12, false );
		die.frames( frames, 19, 20, 21 );

		play( idle );
	}

	@Override
	public void attack( int cell ) {
		if (!Level.adjacent(cell, ch.pos)) {

			((MissileSprite)parent.recycle( MissileSprite.class )).
					reset( ch.pos, cell, new ParalyticDart(1), new Callback() {
						@Override
						public void call() {
							ch.onAttackComplete();
						}
					} );

			play( cast );
			turnTo( ch.pos , cell );

		} else {

			super.attack( cell );

		}
	}
}
