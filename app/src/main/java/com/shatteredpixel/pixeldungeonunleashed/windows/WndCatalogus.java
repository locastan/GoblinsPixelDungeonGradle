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

import java.util.ArrayList;

import com.shatteredpixel.pixeldungeonunleashed.GoblinsPixelDungeon;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.Ring;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ui.Component;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.Potion;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.Scroll;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.scenes.PixelScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.shatteredpixel.pixeldungeonunleashed.ui.ScrollPane;
import com.shatteredpixel.pixeldungeonunleashed.ui.Window;
import com.shatteredpixel.pixeldungeonunleashed.utils.Utils;

public class WndCatalogus extends WndTabbed {

	private static final int WIDTH_P    = 112;
	private static final int HEIGHT_P    = 160;

	private static final int WIDTH_L    = 128;
	private static final int HEIGHT_L    = 128;

	private static final int ITEM_HEIGHT	= 18;

	private static final String TXT_POTIONS	= "Potions";
	private static final String TXT_SCROLLS	= "Scrolls";
	private static final String TXT_RINGS   = "Rings";
	private static final String TXT_TITLE	= "Catalogus";
	
	private BitmapText txtTitle;
	private ScrollPane list;
	
	private ArrayList<ListItem> items = new ArrayList<WndCatalogus.ListItem>();

	private static int showTab = 0;

	public WndCatalogus() {
		
		super();

		if (GoblinsPixelDungeon.landscape()) {
			resize( WIDTH_L, HEIGHT_L );
		} else {
			resize( WIDTH_P, HEIGHT_P );
		}

		txtTitle = PixelScene.createText( TXT_TITLE, 9, true );
		txtTitle.hardlight(Window.TITLE_COLOR);
		txtTitle.measure();
		add(txtTitle);
		
		list = new ScrollPane( new Component() ) {
			@Override
			public void onClick( float x, float y ) {
				int size = items.size();
				for (int i=0; i < size; i++) {
					if (items.get( i ).onClick( x, y )) {
						break;
					}
				}
			}
		};
		add(list);
		list.setRect(0, txtTitle.height(), width, height - txtTitle.height());

		WndCatalogus.showTab = 0;
		Tab[] tabs = {
			new LabeledTab( TXT_POTIONS ) {
				protected void select( boolean value ) {
					super.select( value );
					if (value == true) {
						showTab = 0;
						updateList();
					}
				};
			},
			new LabeledTab( TXT_SCROLLS ) {
				protected void select( boolean value ) {
					super.select( value );
					if (value == true) {
						showTab = 1;
						updateList();
					}
				};
			},
			new LabeledTab( TXT_RINGS ) {
				protected void select( boolean value ) {
					super.select( value );
					if (value == true) {
						showTab = 2;
						updateList();
					}
				}
			}
		};
		for (Tab tab : tabs) {
			add( tab );
		}

		layoutTabs();

		select( showTab );
	}
	
	private void updateList() {
		if (showTab == 0) {
			txtTitle.text( Utils.format( TXT_TITLE, TXT_POTIONS ) );
		} else if (showTab == 1) {
			txtTitle.text( Utils.format( TXT_TITLE, TXT_SCROLLS ) );
		} else {
			txtTitle.text( Utils.format( TXT_TITLE, TXT_RINGS ) );
		}
		
		txtTitle.measure();
		txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width()) / 2 );

		items.clear();
		
		Component content = list.content();
		content.clear();
		list.scrollTo( 0, 0 );
		
		float pos = 0;
		if (showTab == 0) {
			for (Class<? extends Item> itemClass : Potion.getKnown()) {
				ListItem item = new ListItem( itemClass );
				item.setRect( 0, pos, width, ITEM_HEIGHT );
				content.add( item );
				items.add( item );

				pos += item.height();
			}

			for (Class<? extends Item> itemClass : Potion.getUnknown()) {
				ListItem item = new ListItem( itemClass );
				item.setRect( 0, pos, width, ITEM_HEIGHT );
				content.add( item );
				items.add( item );

				pos += item.height();
			}
		} else if (showTab == 1) {
			for (Class<? extends Item> itemClass : Scroll.getKnown()) {
				ListItem item = new ListItem( itemClass );
				item.setRect( 0, pos, width, ITEM_HEIGHT );
				content.add( item );
				items.add( item );

				pos += item.height();
			}

			for (Class<? extends Item> itemClass : Scroll.getUnknown()) {
				ListItem item = new ListItem( itemClass );
				item.setRect( 0, pos, width, ITEM_HEIGHT );
				content.add( item );
				items.add( item );

				pos += item.height();
			}
		} else {
			for (Class<? extends Item> itemClass : Ring.getKnown()) {
				ListItem item = new ListItem( itemClass );
				item.setRect( 0, pos, width, ITEM_HEIGHT );
				content.add( item );
				items.add( item );

				pos += item.height();
			}

			for (Class<? extends Item> itemClass : Ring.getUnknown()) {
				ListItem item = new ListItem( itemClass );
				item.setRect( 0, pos, width, ITEM_HEIGHT );
				content.add( item );
				items.add( item );

				pos += item.height();
			}
		}

		content.setSize( width, pos );
	}
	
	private static class ListItem extends Component {
		
		private Item item;
		private boolean identified;
		
		private ItemSprite sprite;
		private BitmapText label;
		
		public ListItem( Class<? extends Item> cl ) {
			super();
			
			try {
				item = cl.newInstance();
				if (identified = item.isIdentified()) {
					sprite.view( item.image(), null );
					label.text( item.name() );
				} else if (item instanceof Ring && (identified = ((Ring) item).isKnown())) {
					sprite.view( item.image(), null );
					label.text( item.name() );
				} else {
					sprite.view( 127, null );
					label.text( item.trueName() );
					label.hardlight( 0xCCCCCC );
				}
			} catch (Exception e) {
				// Do nothing
			}
		}
		
		@Override
		protected void createChildren() {
			sprite = new ItemSprite();
			add( sprite );
			
			label = PixelScene.createText( 8, false );
			add( label );
		}
		
		@Override
		protected void layout() {
			sprite.y = PixelScene.align( y + (height - sprite.height) / 2 );
			
			label.x = sprite.x + sprite.width;
			label.y = PixelScene.align( y + (height - label.baseLine()) / 2 );
		}
		
		public boolean onClick( float x, float y ) {
			if (identified && inside( x, y )) {
				GameScene.show( new WndInfoItem( item ) );
				return true;
			} else {
				return false;
			}
		}
	}
}
