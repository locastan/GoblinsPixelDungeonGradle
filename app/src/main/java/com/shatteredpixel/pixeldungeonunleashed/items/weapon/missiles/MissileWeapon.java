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
package com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles;

import java.util.ArrayList;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.PinCushion;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.HeroClass;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfSharpshooting;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.Weapon;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndOptions;
import com.watabou.utils.Random;

public class MissileWeapon extends Weapon {

	private static final String TXT_MISSILES	= "Missile weapon";
	private static final String TXT_YES			= "Yes, I know what I'm doing";
	private static final String TXT_NO			= "No, I changed my mind";
	private static final String TXT_R_U_SURE	=
		"Do you really want to equip it as a melee weapon?";
	
	{
		stackable = true;
		levelKnown = true;
		defaultAction = AC_THROW;
		usesTargeting = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (hero.heroClass != HeroClass.THACO && hero.heroClass != HeroClass.FUMBLES) {
			actions.remove( AC_EQUIP );
			actions.remove( AC_UNEQUIP );
		}
		return actions;
	}

	@Override
	protected void onThrow( int cell ) {
		Char enemy = Actor.findChar( cell );
		if (enemy == null || enemy == curUser) {
			if (this instanceof Boomerang)
				super.onThrow( cell );
			else
				miss( cell );
		} else {
			if (!curUser.shoot( enemy, this )) {
				miss( cell );
			} else if (!(this instanceof Boomerang)){
				int bonus = 0;

				for (Buff buff : curUser.buffs(RingOfSharpshooting.Aim.class))
					bonus += ((RingOfSharpshooting.Aim)buff).level;

				if (curUser.heroClass == HeroClass.THACO && enemy.buff(PinCushion.class) == null)
					bonus += 3;

				if (Random.Float() > Math.pow(0.7, bonus))
					Buff.affect(enemy, PinCushion.class).stick(this);
			}
		}
	}
	
	protected void miss( int cell ) {
		int bonus = 0;
		for (Buff buff : curUser.buffs(RingOfSharpshooting.Aim.class)) {
			bonus += ((RingOfSharpshooting.Aim)buff).level;
		}

		//degraded ring of sharpshooting will even make missed shots break.
		if (Random.Float() < Math.pow(0.6, -bonus))
			super.onThrow( cell );
	}
	
	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		
		super.proc( attacker, defender, damage );
		
		Hero hero = (Hero)attacker;
		if (hero.rangedWeapon == null && stackable) {
			if (quantity == 1) {
				//doUnequip( hero, false, false ); this causes the main weapon to disappear when stabbing with 1 dart left
                detach( hero.belongings.backpack );
			} else {
				detach( hero.belongings.backpack );
			}
		}
	}
	
	@Override
	public boolean doEquip( final Hero hero ) {
		GameScene.show(
			new WndOptions( TXT_MISSILES, TXT_R_U_SURE, TXT_YES, TXT_NO ) {
				@Override
				protected void onSelect(int index) {
					if (index == 0) {
						MissileWeapon.super.doEquip( hero );
					}
				};
			}
		);
		
		return false;
	}
	
	@Override
	public Item random() {
		return this;
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
		
		StringBuilder info = new StringBuilder( desc() );
		
		info.append( "\n\nAverage damage of this weapon equals to " + (MIN + (MAX - MIN) / 2) + " points per hit. " );
		
		if (Dungeon.hero.belongings.backpack.items.contains( this )) {
			if (STR > Dungeon.hero.STR()) {
				info.append(
					"\n\nBecause of your inadequate strength the accuracy and speed " +
					"of your attack with this " + name + " is decreased." );
			}
			if (STR < Dungeon.hero.STR() && Dungeon.hero.heroClass == HeroClass.THACO) {
				info.append(
					"\n\nBecause of your excess strength the damage " +
					"of your attack with this " + name + " is increased." );
			}
		}

		info.append( "\n\nThis weapon is designed to be used at range, it's much less accurate at melee range.");
		
		if (isEquipped( Dungeon.hero )) {
			info.append( "\n\nYou hold the " + name + " at the ready." );
		}
		
		return info.toString();
	}
}
