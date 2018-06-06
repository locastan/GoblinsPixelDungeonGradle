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
package com.shatteredpixel.pixeldungeonunleashed.items;

import java.util.ArrayList;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.sprites.HeroSprite;
import com.shatteredpixel.pixeldungeonunleashed.ui.StatusPane;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Badges;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Blindness;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Fury;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.HeroSubClass;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.effects.SpellSprite;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.utils.Utils;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndChooseWay;

public class TomeOfMastery extends Item {

	private static final String TXT_BLINDED	= "You can't read while blinded";
	
	public static final float TIME_TO_READ = 10;
	
	public static final String AC_READ	= "READ";
	
	{
		stackable = false;
		name = "Tome of Mastery";
		image = ItemSpriteSheet.MASTERY;
		level = 1;
		
		unique = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_READ );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_READ )) {
			
			if (hero.buff( Blindness.class ) != null) {
				GLog.w( TXT_BLINDED );
				return;
			}

			curUser = hero;
			if (hero.subClass == HeroSubClass.NONE) {
				HeroSubClass way1 = null;
				HeroSubClass way2 = null;
				switch (hero.heroClass) {
					case COMPLAINS:
						way1 = HeroSubClass.GLADIATOR;
						way2 = HeroSubClass.BERSERKER;
						break;
					case CHIEF:
						way1 = HeroSubClass.BATTLEMAGE;
						way2 = HeroSubClass.WARLOCK;
						break;
					case FUMBLES:
						way1 = HeroSubClass.FREERUNNER;
						way2 = HeroSubClass.ASSASSIN;
						break;
					case THACO:
						way1 = HeroSubClass.SNIPER;
						way2 = HeroSubClass.WARDEN;
						break;
				}
				GameScene.show(new WndChooseWay(this, way1, way2));
			} else if (level == 1) {
				level = 0;
				GLog.w( "You learn more about being a %s!", Utils.capitalize( hero.subClass.title() ) );
				hero.earnExp(hero.maxExp());

				try { // investigate this a bit deeper...
					this.detach(curUser.belongings.backpack);
				} catch (Exception e) {

				}
				curUser.spend(TomeOfMastery.TIME_TO_READ);
				curUser.busy();

				curUser.sprite.operate(curUser.pos);
				Sample.INSTANCE.play(Assets.SND_MASTERY);
				curUser.sprite.emitter().burst(Speck.factory(Speck.MASTERY), 12);
			} else {
				GLog.i("There is nothing more you can learn from this book");
			}
			
		} else {
			
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public boolean doPickUp( Hero hero ) {
		Badges.validateMastery();
		return super.doPickUp( hero );
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String info() {
		return
			"This worn leather book is not that thick, but you feel somehow, " +
			"that you can gather a lot from it. Remember though that reading " +
			"this tome may require some time.";
	}
	
	public void choose( HeroSubClass way ) {
		level = 0;
		detach( curUser.belongings.backpack );
		
		curUser.spend( TomeOfMastery.TIME_TO_READ );
		curUser.busy();
		
		curUser.subClass = way;
		
		curUser.sprite.operate( curUser.pos );
		Sample.INSTANCE.play( Assets.SND_MASTERY );
		
		SpellSprite.show( curUser, SpellSprite.MASTERY );
		curUser.sprite.emitter().burst( Speck.factory( Speck.MASTERY ), 12 );
		GLog.w( "You have chosen the way of the %s!", Utils.capitalize( way.title() ) );
		
		if (way == HeroSubClass.BERSERKER && curUser.HP <= curUser.HT * Fury.LEVEL) {
			Buff.affect( curUser, Fury.class );
			((HeroSprite) Dungeon.hero.sprite).refresh();
			StatusPane.refreshavatar();
		}
	}
}
