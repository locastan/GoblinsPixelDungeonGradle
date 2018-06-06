/*
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015  David Mitchell
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
import com.watabou.noosa.TextureFilm;

public class BodFlySprite extends MobSprite {

    public BodFlySprite() {
        super();

        texture( Assets.SWARM );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 15, true );
        idle.frames( frames, 32, 33, 34, 35, 36, 37 );

        run = new Animation( 15, true );
        run.frames( frames, 32, 33, 34, 35, 36, 37 );

        attack = new Animation( 20, false );
        attack.frames( frames, 38, 39, 40, 41 );

        die = new Animation( 15, false );
        die.frames( frames, 42, 43, 44, 45, 46 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0xFFF3EC81;
    }
}

