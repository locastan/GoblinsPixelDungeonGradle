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
package com.shatteredpixel.pixeldungeonunleashed.scenes;

import com.shatteredpixel.pixeldungeonunleashed.GoblinsPixelDungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.PET;
import com.shatteredpixel.pixeldungeonunleashed.items.Amulet;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.ResultDescriptions;
import com.shatteredpixel.pixeldungeonunleashed.effects.Flare;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.ui.RedButton;
import com.watabou.utils.Random;

public class AmuletScene extends PixelScene {

	private static final String TXT_EXIT	= "Let's call it a day";
	private static final String TXT_STAY	= "I'm not done yet";
	private static final String TXT_ENDLESS	= "I never want it to end";
	
	private static final int WIDTH			= 120;
	private static final int BTN_HEIGHT		= 18;
	private static final float SMALL_GAP	= 2;
	private static final float LARGE_GAP	= 8;
	
	private static final String TXT =
		"You finally hold it in your hands, the Amulet of Yendor. Using its power " +
		"you can take over the world or bring peace and prosperity to people or whatever. " +
		"Anyway, your life will change forever and this game will end here. " +
		"Or you can stay and fight your way back to the surface." +
		"Or you could go deeper and deeper forever...";
	
	public static boolean noText = false;
	
	private Image amulet;
	
	@Override
	public void create() {
		super.create();
		
		BitmapTextMultiline text = null;
		if (!noText) {
			text = createMultiline( TXT, 8, false );
			text.maxWidth = WIDTH;
			text.measure();
			add( text );
		}
		
		amulet = new Image( Assets.AMULET );
		add( amulet );
		
		RedButton btnExit = new RedButton( TXT_EXIT ) {
			@Override
			protected void onClick() {
				Dungeon.win( ResultDescriptions.WIN );
				Dungeon.deleteGame( Dungeon.hero.heroClass, true );
				Game.switchScene( noText ? TitleScene.class : RankingsScene.class );
			}
		};
		btnExit.setSize( WIDTH, BTN_HEIGHT );
		add( btnExit );

		RedButton btnEndless = new RedButton( TXT_ENDLESS ) {
			@Override
			protected void onClick() {
                Dungeon.win( ResultDescriptions.WIN );
                Dungeon.hero.belongings.getItem(Amulet.class).detach(Dungeon.hero.belongings.backpack);
                GoblinsPixelDungeon.setDifficulty(15);
                checkPetPort();
                Dungeon.difficultyLevel = GoblinsPixelDungeon.getDifficulty();
                InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
				Game.switchScene( InterlevelScene.class );
			}
		};
		btnEndless.setSize( WIDTH, BTN_HEIGHT );
		add( btnEndless );
		
		RedButton btnStay = new RedButton( TXT_STAY ) {
			@Override
			protected void onClick() {
				onBackPressed();
			}
		};
		btnStay.setSize( WIDTH, BTN_HEIGHT );
		add( btnStay );
		
		float height;
		if (noText) {
			height = amulet.height + LARGE_GAP + btnExit.height() + SMALL_GAP + btnStay.height() + SMALL_GAP + btnEndless.height();
			
			amulet.x = align( (Camera.main.width - amulet.width) / 2 );
			amulet.y = align( (Camera.main.height - height) / 2 );
			
			btnExit.setPos( (Camera.main.width - btnExit.width()) / 2, amulet.y + amulet.height + LARGE_GAP );
			btnStay.setPos( btnExit.left(), btnExit.bottom() + SMALL_GAP );
            btnEndless.setPos( btnStay.left(), btnStay.bottom() + SMALL_GAP );
			
		} else {
			height = amulet.height + LARGE_GAP + text.height() + LARGE_GAP + btnExit.height() + SMALL_GAP + btnStay.height() + SMALL_GAP + btnEndless.height();
			
			amulet.x = align( (Camera.main.width - amulet.width) / 2 );
			amulet.y = align( (Camera.main.height - height) / 2 );
			
			text.x =  align( (Camera.main.width - text.width()) / 2 );
			text.y = amulet.y + amulet.height + LARGE_GAP;
			
			btnExit.setPos( (Camera.main.width - btnExit.width()) / 2, text.y + text.height() + LARGE_GAP );
			btnStay.setPos( btnExit.left(), btnExit.bottom() + SMALL_GAP );
            btnEndless.setPos( btnStay.left(), btnStay.bottom() + SMALL_GAP );
		}

		new Flare( 8, 48 ).color( 0xFFDDBB, true ).show( amulet, 0 ).angularSpeed = +30;
		
		fadeIn();
	}

    private PET checkpet(){
        for (Mob mob : Dungeon.level.mobs) {
            if(mob instanceof PET) {
                return (PET) mob;
            }
        }
        return null;
    }

    private boolean checkpetNear(){
        for (int n : Level.NEIGHBOURS8) {
            int c =  Dungeon.hero.pos + n;
            if (Actor.findChar(c) instanceof PET) {
                return true;
            }
        }
        return false;
    }

    private void checkPetPort(){
        PET pet = checkpet();
        if(pet!=null && checkpetNear()){
            //GLog.i("I see pet");
            Dungeon.hero.petType=pet.type;
            Dungeon.hero.petLevel=pet.level;
            Dungeon.hero.petKills=pet.kills;
            Dungeon.hero.petHP=pet.HP;
            Dungeon.hero.petExperience=pet.experience;
            Dungeon.hero.petCooldown=pet.cooldown;
            pet.destroy();
            Dungeon.hero.petfollow=true;
        } else if (Dungeon.hero.haspet && Dungeon.hero.petfollow) {
            Dungeon.hero.petfollow=true;
        } else {
            Dungeon.hero.petfollow=false;
        }

    }
	
	@Override
	protected void onBackPressed() {
		InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
		Game.switchScene( InterlevelScene.class );
	}
	
	private float timer = 0;
	
	@Override
	public void update() {
		super.update();
		
		if ((timer -= Game.elapsed) < 0) {
			timer = Random.Float( 0.5f, 5f );
			
			Speck star = (Speck)recycle( Speck.class );
			star.reset( 0, amulet.x + 10.5f, amulet.y + 5.5f, Speck.DISCOVER );
			add( star );
		}
	}
}
