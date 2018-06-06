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

import com.watabou.noosa.Game;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.ui.Component;
import com.shatteredpixel.pixeldungeonunleashed.Assets;

public class Archs extends Component {

	private static final float SCROLL_SPEED	= 10f;

	//private SkinnedBlock arcsBg;
	private SkinnedBlock arcsFg;

	//private static float offsB = 0;
	private static float offsF = 0;

	public boolean reversed = false;

	@Override
	protected void createChildren() {
		/*
		arcsBg = new SkinnedBlock( 1, 1, Assets.ARCS_BG );
		arcsBg.offsetTo( 0,  offsB );
		add( arcsBg );*/

		arcsFg = new SkinnedBlock( 1, 1, Assets.ARCS_FG );
		arcsFg.offsetTo( 0,  offsF );
		add( arcsFg );
	}

	@Override
	protected void layout() {
		/*arcsBg.size( width, height );
		arcsBg.offset( arcsBg.texture.height / 4 - (height % arcsBg.texture.height) / 2, 0 );*/

		arcsFg.size( width, height );
		arcsFg.offset( arcsFg.texture.height / 4 - (height % arcsFg.texture.height) / 2, 0 );
	}

	@Override
	public void update() {

		super.update();

		float shift = Game.elapsed * SCROLL_SPEED;
		if (reversed) {
			shift = -shift;
		}

		//arcsBg.offset( shift, 0 );
		arcsFg.offset( shift * 2, 0 );

		//offsB = arcsBg.offsetX();
		offsF = arcsFg.offsetX();
	}
}
