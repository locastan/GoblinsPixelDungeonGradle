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

import com.shatteredpixel.pixeldungeonunleashed.Challenges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Ghost;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.scenes.PixelScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.FetidRatSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.GnollTricksterSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.GreatCrabSprite;
import com.shatteredpixel.pixeldungeonunleashed.ui.RedButton;
import com.shatteredpixel.pixeldungeonunleashed.ui.Window;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.noosa.BitmapTextMultiline;

public class WndSadGhost extends Window {

	private static final String TXT_RAT	=
		"Thank you, that horrid rat is slain and I can finally rest..." +
		"I wonder what twisted magic created such a foul creature...\n\n";
	private static final String TXT_GNOLL	=
		"Thank you, that scheming gnoll is slain and I can finally rest..." +
		"I wonder what twisted magic made it so smart...\n\n";
	private static final String TXT_CRAB	=
		"Thank you, that giant crab is slain and I can finally rest..." +
		"I wonder what twisted magic allowed it to live so long...\n\n";
	private static final String TXT_GIVEITEM=
		"Please take one of these items, they are useless to me now... " +
		"Maybe they will help you in your journey...\n\n" +
		"Also... There is an item lost in this dungeon that is very dear to me..." +
		"If you ever... find my... rose......";
	private static final String TXT_WEAPON	= "Ghost's weapon";
	private static final String TXT_ARMOR	= "Ghost's armor";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	
	public WndSadGhost( final Ghost ghost, final int type ) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		BitmapTextMultiline message;
		switch (type){
			case 1:default:
				titlebar.icon( new FetidRatSprite() );
				titlebar.label( "DEFEATED FETID RAT" );
				message = PixelScene.createMultiline( TXT_RAT+TXT_GIVEITEM, 6, false );
				break;
			case 2:
				titlebar.icon( new GnollTricksterSprite() );
				titlebar.label( "DEFEATED GNOLL TRICKSTER" );
				message = PixelScene.createMultiline( TXT_GNOLL+TXT_GIVEITEM, 6, false );
				break;
			case 3:
				titlebar.icon( new GreatCrabSprite());
				titlebar.label( "DEFEATED GREAT CRAB" );
				message = PixelScene.createMultiline( TXT_CRAB+TXT_GIVEITEM, 6, false );
				break;

		}


		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		message.maxWidth = WIDTH;
		message.measure();
		message.y = titlebar.bottom() + GAP;
		add( message );
		
		RedButton btnWeapon = new RedButton( TXT_WEAPON ) {
			@Override
			protected void onClick() {
				selectReward( ghost, Ghost.Quest.weapon );
			}
		};
		btnWeapon.setRect( 0, message.y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnWeapon );

		if (!Dungeon.isChallenged( Challenges.NO_ARMOR )) {
			RedButton btnArmor = new RedButton(TXT_ARMOR) {
				@Override
				protected void onClick() {
					selectReward(ghost, Ghost.Quest.armor);
				}
			};
			btnArmor.setRect(0, btnWeapon.bottom() + GAP, WIDTH, BTN_HEIGHT);
			add(btnArmor);

			resize(WIDTH, (int) btnArmor.bottom());
		} else {
			resize(WIDTH, (int) btnWeapon.bottom());
		}
	}
	
	private void selectReward( Ghost ghost, Item reward ) {
		
		hide();
		
		if (reward.doPickUp( Dungeon.hero )) {
			GLog.i( Hero.TXT_YOU_NOW_HAVE, reward.name() );
		} else {
			Dungeon.level.drop( reward, ghost.pos ).sprite.drop();
		}
		
		ghost.yell( "Farewell, adventurer!" );
		ghost.die( null );
		
		Ghost.Quest.complete();
	}
}
