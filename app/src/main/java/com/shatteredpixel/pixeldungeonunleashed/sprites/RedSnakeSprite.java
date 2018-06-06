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

public class RedSnakeSprite extends MobSprite {

    public RedSnakeSprite() {
        super();

        texture( Assets.SNAKE );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 8, 8, 8, 9 );

        run = new Animation( 10, true );
        run.frames( frames, 13, 12, 11, 12, 13 );

        attack = new Animation( 14, false );
        attack.frames( frames, 8, 9, 10, 8 );

        die = new Animation( 10, false );
        die.frames( frames, 10, 14, 15 );

        play( idle );
    }
}
