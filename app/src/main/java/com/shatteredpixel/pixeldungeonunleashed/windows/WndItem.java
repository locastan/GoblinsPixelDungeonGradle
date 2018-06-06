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
package com.shatteredpixel.pixeldungeonunleashed.windows;

import com.watabou.noosa.BitmapTextMultiline;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.scenes.PixelScene;
import com.shatteredpixel.pixeldungeonunleashed.ui.ItemSlot;
import com.shatteredpixel.pixeldungeonunleashed.ui.RedButton;
import com.shatteredpixel.pixeldungeonunleashed.ui.Window;

public class WndItem extends Window {

	private static final float BUTTON_WIDTH		= 36;
	private static final float BUTTON_HEIGHT	= 16;
	
	private static final float GAP	= 2;
	
	private static final int WIDTH = 120;
	
	public WndItem( final WndBag owner, final Item item ) {
		
		super();
		
		IconTitle titlebar = new IconTitle( item );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		if (item.levelKnown && item.level > 0) {
			titlebar.color( ItemSlot.UPGRADED );
		} else if (item.levelKnown && item.level < 0) {
			titlebar.color( ItemSlot.DEGRADED );
		}
		
		BitmapTextMultiline info = PixelScene.createMultiline( item.info(), 6, false );
		info.maxWidth = WIDTH;
		info.measure();
		info.x = titlebar.left();
		info.y = titlebar.bottom() + GAP;
		add( info );
	
		float y = info.y + info.height() + GAP;
		float x = 0;
		
		if (Dungeon.hero.isAlive() && owner != null) {
			for (final String action:item.actions( Dungeon.hero )) {
				
				RedButton btn = new RedButton( action ) {
					@Override
					protected void onClick() {
						hide();
						owner.hide();
						item.execute( Dungeon.hero, action );
					};
				};
				btn.setSize( Math.max( BUTTON_WIDTH, btn.reqWidth() ), BUTTON_HEIGHT );
				if (x + btn.width() > WIDTH) {
					x = 0;
					y += BUTTON_HEIGHT + GAP;
				}
				btn.setPos( x, y );
				add( btn );

				if (action == item.defaultAction) {
					btn.textColor( TITLE_COLOR );
				}

				x += btn.width() + GAP;
			}
		}
		
		resize( WIDTH, (int)(y + (x > 0 ? BUTTON_HEIGHT : 0)) );
	}
}
