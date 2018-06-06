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
package com.shatteredpixel.pixeldungeonunleashed.ui;

import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CharSprite;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

public class PetHealthIndicator extends Component {

	private static final float HEIGHT	= 2;

	public static PetHealthIndicator instance;

	private Char target;

	private Image bg;
	private Image level;

	public PetHealthIndicator() {
		super();
		
		instance = this;
	}
	
	@Override
	// Color hex is in 0xAARRGGBB format
	protected void createChildren() {
        // grey bg with color changing bar for hp (see update function)
		bg = new Image( TextureCache.createSolid( 0xFF888888 ) );
		bg.scale.y = HEIGHT;
		add( bg );
		
		level = new Image( TextureCache.createSolid( 0xFF00cc00 ) );
		level.scale.y = HEIGHT;
		add( level );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (target != null && target.isAlive() && target.sprite != null) {
			CharSprite sprite = target.sprite;
			bg.scale.x = sprite.width;
            // adding color change to pethealthbar depending on health to make it stand out more from mobs.
            if (target.HP > target.HT*7/12) {
                level.texture(TextureCache.createSolid(0xFF00cc00));
			} else if (target.HP <= target.HT/3) {
				level.texture(TextureCache.createSolid(0xFFcc0000));
            } else if (target.HP <= target.HT*7/12) {
                level.texture(TextureCache.createSolid(0xFFddcc00));
            }
			level.scale.x = sprite.width * target.HP / target.HT;
			bg.x = level.x = sprite.x;
			bg.y = level.y = sprite.y - HEIGHT - 1;
			
			visible = true;
		} else {
			visible = false;
		}
	}
	
	public void target( Char ch ) {
		if (ch != null && ch.isAlive()) {
			target = ch;
		} else {
			target = null;
		}
	}
	
	public Char target() {
		return target;
	}
}
