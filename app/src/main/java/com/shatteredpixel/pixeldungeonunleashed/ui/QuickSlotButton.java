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

import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.PET;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.Crossbow;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.DungeonTilemap;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.scenes.PixelScene;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndBag;

public class QuickSlotButton extends Button implements WndBag.Listener {

	private static final String TXT_SELECT_ITEM = "Select an item for the quickslot";


	private static QuickSlotButton[] instance = new QuickSlotButton[4];
	private int slotNum;

	private ItemSlot slot;
	
	private static Image crossB;
	private static Image crossM;
	private Image image;
	
	private static boolean targeting = false;

	protected QuickSlotButton(int slotNum) {
		super();
		this.slotNum = slotNum;
		item(select(slotNum));
		
		instance[slotNum] = this;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		reset();
	}

	public static void reset() {
		instance = new QuickSlotButton[4];
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		slot = new ItemSlot() {
			@Override
			protected void onClick() {
				if (targeting && Dungeon.hero.lastTarget != null) {
					GameScene.handleCell( Dungeon.hero.lastTarget.pos );
				} else {
					Item item = select(slotNum);
					if (item.usesTargeting)
						useTargeting();
					item.execute( Dungeon.hero );
				}
			}
			@Override
			protected boolean onLongClick() {
				return QuickSlotButton.this.onLongClick();
			}
			@Override
			protected void onTouchDown() {
				icon.lightness( 0.7f );
			}
			@Override
			protected void onTouchUp() {
				icon.resetColor();
			}
		};
		slot.showParams( true, false, true );
		add( slot );
		
		crossB = Icons.TARGET.get();
		crossB.visible = false;
		add(crossB);
		
		crossM = new Image();
		crossM.copy( crossB );

		image = Icons.CROSSY.get();
		image.visible = false;
		add( image );
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		slot.fill( this );
		
		crossB.x = PixelScene.align( x + (width - crossB.width) / 2 );
		crossB.y = PixelScene.align( y + (height - crossB.height) / 2 );

		image.x = PixelScene.align(x + (width - image.width) / 2);
		image.y = PixelScene.align(y + (height - image.height) / 2);
		//image.alpha(0.6f);
	}
	
	@Override
	protected void onClick() {
		GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM );
	}
	
	@Override
	protected boolean onLongClick() {
		GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM );
		return true;
	}

	private static Item select(int slotNum){
		return Dungeon.quickslot.getItem( slotNum );
	}

	@Override
	public void onSelect( Item item ) {
		if (item != null) {
			Dungeon.quickslot.setSlot( slotNum , item );
			refresh();
		}
	}
	
	public void item( Item item ) {
		slot.item( item );
		enableSlot();
	}
	
	public void enable( boolean value ) {
		active = value;
		if (value) {
			enableSlot();
		} else {
			slot.enable( false );
		}
	}
	
	private void enableSlot() {
		slot.enable(Dungeon.quickslot.isNonePlaceholder( slotNum ));
	}
	
	private void useTargeting() {
		targeting = Dungeon.hero.lastTarget != null && Dungeon.hero.lastTarget.isAlive()
				&& Dungeon.visible[Dungeon.hero.lastTarget.pos] && !(Dungeon.hero.lastTarget instanceof PET);

		if (!targeting) {
			// we don't have a pre-selected target, try to choose a new one
			Dungeon.hero.lastTarget = null;
			int mobDistance = 999; // supports mobs well beyond the furthest line-of-sight
			int myX = Dungeon.hero.pos % Level.WIDTH;
			int myY = Dungeon.hero.pos / Level.WIDTH;

			int v = Dungeon.hero.visibleEnemies();
			if (v > 0) {
				for (int i = 0; i < v; i++) {
					Mob mob = Dungeon.hero.visibleEnemy(i);  // DSM-xxxx - do a path check to the target mob
					int mobX = mob.pos % Level.WIDTH;
					int mobY = mob.pos / Level.WIDTH;

					// the following formula is good enough to determine which mob is closest
					int thisDistance = ((mobX - myX) * (mobX - myX)) + ((mobY - myY) * (mobY - myY));
					if ((Dungeon.hero.lastTarget == null || (thisDistance < mobDistance)) && !(mob instanceof
					PET)) {
						Dungeon.hero.lastTarget = mob;
						mobDistance = thisDistance;
						active = true;
						targeting = true;
					}
				}
			}
		}

		if (targeting) {
			if (Actor.all().contains( Dungeon.hero.lastTarget )) {
				Dungeon.hero.lastTarget.sprite.parent.add( crossM );
				crossM.point( DungeonTilemap.tileToWorld( Dungeon.hero.lastTarget.pos ) );
				crossB.x = PixelScene.align( x + (width - crossB.width) / 2 );
				crossB.y = PixelScene.align( y + (height - crossB.height) / 2 );
				image.x = PixelScene.align(x + (width - image.width) / 2);
				image.y = PixelScene.align(y + (height - image.height) / 2);
				crossB.visible = true;
			} else {
				Dungeon.hero.lastTarget = null;
			}
		}
	}
	
	public static void refresh() {
		for (int i = 0; i < instance.length; i++) {
			if (instance[i] != null) {
				instance[i].item(select(i));
				if (Dungeon.hero.belongings.weapon instanceof Crossbow && select(i) instanceof Dart) {
					instance[i].image.visible = true;
				} else instance[i].image.visible = false;
			}
		}
	}

	public static void crossbow(boolean set) {
		for (int i = 0; i < instance.length; i++) {
			if (instance[i] != null) {
				if (select(i) instanceof Dart) {
					instance[i].image.visible = set;
				} else instance[i].image.visible = false;
			}
		}
	}

	public static void target( Char target ) {
		if (target != Dungeon.hero) {
			Dungeon.hero.lastTarget = target;
			if (target instanceof PET) {
				GameScene.pethealth.target(target);
			} else {
                HealthIndicator.instance.target(target);
            }
		}
	}
	
	public static void cancel() {
		if (targeting) {
			crossB.visible = false;
			crossM.remove();
			targeting = false;
		}
	}
}
