/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.PhaseKlik;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.Lightnings;
import com.watabou.noosa.TextureFilm;

public class PhaseKlikSprite extends MobSprite {
	
	//Frames 1-4 are idle, 5-8 are moving, 9-12 are attack and the last are for death RBVG
	
	private int[] points = new int[2];

	public PhaseKlikSprite() {
		super();

		texture(Assets.KLIKS);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(2, true);
		idle.frames(frames, 48, 49, 50, 51);

		run = new Animation(4, true);
		run.frames(frames, 52, 53, 54, 55);

		attack = new Animation(4, false);
		attack.frames(frames, 56, 57, 58, 59);

		zap = attack.clone();
		
		die = new Animation(8, false);
		die.frames(frames, 60, 61, 62, 63);

		play(idle);
	}

	@Override
	public void zap(int pos) {

		points[0] = ch.pos;
		points[1] = pos;
		parent.add(new Lightnings(points, 2, (PhaseKlik) ch));

		turnTo(ch.pos, pos);
		play(zap);
	}
	
	@Override
	public int blood() {
		return 0xFFcdcdb7;
	}
}
