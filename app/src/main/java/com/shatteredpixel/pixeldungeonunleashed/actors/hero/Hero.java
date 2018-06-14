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
package com.shatteredpixel.pixeldungeonunleashed.actors.hero;

import android.util.Log;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Badges;
import com.shatteredpixel.pixeldungeonunleashed.Bones;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.GamesInProgress;
import com.shatteredpixel.pixeldungeonunleashed.ResultDescriptions;
import com.shatteredpixel.pixeldungeonunleashed.Statistics;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Barkskin;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Bleeding;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Bless;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Blindness;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Charm;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Combo;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Cripple;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Drowsy;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Fury;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Hunger;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Invisibility;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Ooze;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Poison;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Regeneration;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Roots;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.SnipersMark;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Vertigo;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Weakness;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Euphoria;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.NPC;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.PET;
import com.shatteredpixel.pixeldungeonunleashed.effects.CellEmitter;
import com.shatteredpixel.pixeldungeonunleashed.effects.CheckedCell;
import com.shatteredpixel.pixeldungeonunleashed.effects.Flare;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.items.Amulet;
import com.shatteredpixel.pixeldungeonunleashed.items.Ankh;
import com.shatteredpixel.pixeldungeonunleashed.items.Dewdrop;
import com.shatteredpixel.pixeldungeonunleashed.items.EasterEgg;
import com.shatteredpixel.pixeldungeonunleashed.items.Egg;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap.Type;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.KindOfWeapon;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.glyphs.Viscosity;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.CapeOfThorns;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.DriedRose;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.EtherealChains;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.HummingTool;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.ShieldOfWonders;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.pixeldungeonunleashed.items.food.Food;
import com.shatteredpixel.pixeldungeonunleashed.items.keys.GoldenKey;
import com.shatteredpixel.pixeldungeonunleashed.items.keys.IronKey;
import com.shatteredpixel.pixeldungeonunleashed.items.keys.Key;
import com.shatteredpixel.pixeldungeonunleashed.items.keys.SkeletonKey;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.Potion;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfMight;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfStrength;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfElements;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfEvasion;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfForce;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfFuror;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfHaste;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfMight;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfSearching;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.RingOfTenacity;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.Scroll;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfMagicalInfusion;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.levels.features.Bookshelf;
import com.shatteredpixel.pixeldungeonunleashed.levels.features.Chasm;
import com.shatteredpixel.pixeldungeonunleashed.levels.features.HolyAltar;
import com.shatteredpixel.pixeldungeonunleashed.levels.features.Sign;
import com.shatteredpixel.pixeldungeonunleashed.plants.Earthroot;
import com.shatteredpixel.pixeldungeonunleashed.plants.Sungrass;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.scenes.InterlevelScene;
import com.shatteredpixel.pixeldungeonunleashed.scenes.SurfaceScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CharSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.HeroSprite;
import com.shatteredpixel.pixeldungeonunleashed.ui.AttackIndicator;
import com.shatteredpixel.pixeldungeonunleashed.ui.BuffIndicator;
import com.shatteredpixel.pixeldungeonunleashed.ui.StatusPane;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndAlchemy;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndLockpick;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndMessage;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndResurrect;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndStory;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndTradeItem;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import static java.lang.Math.max;

public class Hero extends Char {

	{
		actPriority = 0; //acts at priority 0, baseline for the rest of behaviour.
	}
	
	private static final String TXT_LEAVE = "One does not simply leave Pixel Dungeon.";
	
	public static final int MAX_LEVEL = 35;
	private static final String TXT_LEVEL_UP = "level up!";
	private static final String TXT_NEW_LEVEL =
		"Welcome to level %d! Now you are healthier and more focused. " +
		"It's easier for you to hit enemies and dodge their attacks.";
	private static final String TXT_LEVEL_CAP =
		"You can't gain any more levels, but your experiences still give you a burst of energy!";
	
	public static final String TXT_YOU_NOW_HAVE	= "You now have %s";
	
	private static final String TXT_SOMETHING_ELSE	= "There is something else here";
	private static final String TXT_LOCKED_CHEST	= "This chest is locked and you don't have matching key";
	private static final String TXT_LOCKED_DOOR		= "You don't have a matching key";
	private static final String TXT_NOTICED_SMTH	= "You noticed something";
	
	private static final String TXT_WAIT	= "...";
	private static final String TXT_SEARCH	= "search";
	
	public static final int STARTING_STR = 10;
	
	private static final float TIME_TO_REST		= 1f;
	private static final float TIME_TO_SEARCH	= 2f;
    private static final float TIME_TO_PICKUP	= 1f;
	
	public HeroClass heroClass = HeroClass.FUMBLES;
	public HeroSubClass subClass = HeroSubClass.NONE;
	
	private int attackSkill = 10;
	private int defenseSkill = 5;

	public boolean ready = false;

	public boolean haspet = false;
	public boolean petfollow = false;
	public int petType = 0;
	public int petLevel = 0;
	public int petKills = 0;
	public int petHP = 0;
	public int petExperience = 0;
	public int petCooldown = 0;

	public int petCount = 0;

	private boolean damageInterrupt = true;
	public HeroAction curAction = null;
	public HeroAction lastAction = null;

	public Char lastTarget = null;

	private Char enemy;
	
	private Item theKey;
    private HummingTool hummingtool;
	
	public boolean resting = false;

	public MissileWeapon rangedWeapon = null;
	public Belongings belongings;
	
	public int STR;
	public boolean weakened = false;
	
	public float awareness;
	
	public int lvl = 1;
	public int exp = 0;
	public int donatedLoot = 0;
	
	private ArrayList<Mob> visibleEnemies;
	
	public Hero() {
		super();
		name = "you";

		switch (Dungeon.difficultyLevel) {
			case Dungeon.DIFF_TUTOR:
			case Dungeon.DIFF_EASY:
				HP = HT = 25;
				STR = STARTING_STR + 1;
				attackSkill += 2;
				defenseSkill += 1;
				break;
			case Dungeon.DIFF_HARD:
				HP = HT = 18;
				STR = STARTING_STR;
				break;
			case Dungeon.DIFF_NTMARE:
				HP = HT = 15;
				STR = STARTING_STR - 1;
				attackSkill -= 2;
				defenseSkill -= 1;
				break;
			default:
				HP = HT = 20;
				STR = STARTING_STR;
				break;
		}
		awareness = 0.1f;
		
		belongings = new Belongings( this );
		
		visibleEnemies = new ArrayList<>();
	}

	public int STR() {
		int STR = this.STR;

		for (Buff buff : buffs(RingOfMight.Might.class)) {
			STR += ((RingOfMight.Might)buff).level;
		}

		return weakened ? STR - 2 : STR;
	}

	private static final String ATTACK		= "attackSkill";
	private static final String DEFENSE		= "defenseSkill";
	private static final String STRENGTH	= "STR";
	private static final String LEVEL		= "lvl";
	private static final String EXPERIENCE	= "exp";
	private static final String DONATE      = "donation";
	private static final String HASPET = "haspet";
	private static final String PETFOLLOW = "petfollow";
	private static final String PETTYPE = "petType";
	private static final String PETLEVEL = "petLevel";
	private static final String PETKILLS = "petKills";
	private static final String PETHP = "petHP";
	private static final String PETEXP = "petExperience";
	private static final String PETCOOLDOWN = "petCooldown";
	private static final String PETCOUNT = "petCount";

	@Override
	public void storeInBundle( Bundle bundle ) {

		super.storeInBundle( bundle );
		
		heroClass.storeInBundle( bundle );
		subClass.storeInBundle( bundle );
		
		bundle.put( ATTACK, attackSkill );
		bundle.put( DEFENSE, defenseSkill );
		
		bundle.put(STRENGTH, STR);
		
		bundle.put( LEVEL, lvl );
		bundle.put( EXPERIENCE, exp );
		bundle.put( DONATE, donatedLoot );
		bundle.put(HASPET, haspet);
		bundle.put(PETFOLLOW, petfollow);
		bundle.put(PETTYPE, petType);
		bundle.put(PETLEVEL, petLevel);
		bundle.put(PETKILLS, petKills);
		bundle.put(PETHP, petHP);
		bundle.put(PETEXP, petExperience);
		bundle.put(PETCOOLDOWN, petCooldown);
		bundle.put(PETCOUNT, petCount);

		belongings.storeInBundle(bundle);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		
		heroClass = HeroClass.restoreInBundle( bundle );
		subClass = HeroSubClass.restoreInBundle( bundle );
		
		attackSkill = bundle.getInt( ATTACK );
		defenseSkill = bundle.getInt( DEFENSE );
		
		STR = bundle.getInt( STRENGTH );
		updateAwareness();
		
		lvl = bundle.getInt( LEVEL );
		exp = bundle.getInt( EXPERIENCE );
		donatedLoot = bundle.getInt( DONATE );
		haspet = bundle.getBoolean(HASPET);
		petfollow = bundle.getBoolean(PETFOLLOW);
		petType = bundle.getInt(PETTYPE);
		petLevel = bundle.getInt(PETLEVEL);
		petKills = bundle.getInt(PETKILLS);
		petHP = bundle.getInt(PETHP);
		petExperience = bundle.getInt(PETEXP);
		petCooldown = bundle.getInt(PETCOOLDOWN);
		petCount = bundle.getInt(PETCOUNT);
		
		belongings.restoreFromBundle(bundle);
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.level = bundle.getInt( LEVEL );
	}
	
	public String className() {
		return heroClass.title();
	}

	public String givenName(){
		return name.equals("you") ? className() : name;
	}
	
	public void live() {
		Buff.affect( this, Regeneration.class );
		Buff.affect( this, Hunger.class );
	}
	
	public int tier() {
		return belongings.armor == null ? 0 : belongings.armor.tier;
	}

	public boolean shoot( Char enemy, MissileWeapon wep ) {

		rangedWeapon = wep;
		boolean result = attack( enemy );
		Invisibility.dispel();
		rangedWeapon = null;

		return result;
	}
	
	@Override
	public int attackSkill( Char target ) {
		float accuracy = 1;
		if (rangedWeapon != null && Level.distance( pos, target.pos ) == 1) {
			accuracy *= 0.5f;
		}

		KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
		if (wep != null) {
			return (int)(attackSkill * accuracy * wep.acuracyFactor( this ));
		} else {
			return (int)(attackSkill * accuracy);
		}
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		
		int bonus = 0;
		for (Buff buff : buffs( RingOfEvasion.Evasion.class )) {
			bonus += ((RingOfEvasion.Evasion)buff).effectiveLevel;
		}

		float evasion = (float)Math.pow( 1.15, bonus );
		if (paralysed) {
			evasion /= 2;
		}
		
		int aEnc = belongings.armor != null ? belongings.armor.STR - STR() : 9 - STR();
		
		if (aEnc > 0) {
			return (int)(defenseSkill * evasion / Math.pow( 1.5, aEnc ));
		} else {
			
			if (heroClass == HeroClass.FUMBLES) {
				if (belongings.armor != null && belongings.armor.STR > 14) {
					return (int) ((defenseSkill - (aEnc * 4)) * evasion);
				} else {
					return (int) ((defenseSkill - (aEnc * 2)) * evasion);
				}
			} else if (heroClass == HeroClass.CHIEF) {
					if (belongings.armor != null && belongings.armor.STR > 14) {
						return (int) ((defenseSkill - (aEnc * 2)) * evasion);
					} else {
						return (int)(defenseSkill * evasion);
					}
			} else {
				return (int)(defenseSkill * evasion);
			}
		}
	}
	
	@Override
	public int dr() {
		int dr = belongings.armor != null ? max( belongings.armor.DR, 0 ) : 0;
		Barkskin barkskin = buff(Barkskin.class);
		if (barkskin != null) {
			dr += barkskin.level();
		}
		return dr;
	}
	
	@Override
	public int damageRoll() {
		KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
		int dmg;
		int bonus = 0;
		for (Buff buff : buffs( RingOfForce.Force.class )) {
			bonus += ((RingOfForce.Force)buff).level;
		}

		if (wep != null) {
			dmg = wep.damageRoll( this ) + bonus;
		} else {
			int str = STR() - 8;
			dmg = bonus == 0 ?
					str > 1 ? Random.NormalIntRange( 1, str ) : 1
					: bonus > 0 ?
							str > 0 ? Random.NormalIntRange( str/2+bonus, (int)(str*0.5f*bonus) + str*2 ) : 1
							: 0;
		}
		if (dmg < 0) dmg = 0;
		return buff( Fury.class ) != null ? (int)(dmg * 1.5f) : dmg;
	}
	
	@Override
	public float speed() {

		float speed = super.speed();

		int hasteLevel = 0;
		for (Buff buff : buffs( RingOfHaste.Haste.class )) {
			hasteLevel += ((RingOfHaste.Haste)buff).level;
		}

		if(haspet){
			int pethaste=Dungeon.petHasteLevel;
			PET heropet = checkpet();

			if(pethaste>0 && hasteLevel>10 && heropet!=null){
				hasteLevel=10;
			}

		}

		if (hasteLevel != 0)
			speed *= Math.pow(1.2, hasteLevel);
		
		int aEnc = belongings.armor != null ? belongings.armor.STR - STR() : 0;
		if (aEnc > 0) {
			
			return (float)(speed * Math.pow( 1.3, -aEnc ));
			
		} else {

			return ((HeroSprite)sprite).sprint( subClass == HeroSubClass.FREERUNNER && !isStarving() ) ?
					invisible > 0 ?
							4f * speed :
							1.5f * speed :
					speed;
			
		}
	}
	
	public float attackDelay() {
		KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;
		if (wep != null) {
			
			return wep.speedFactor( this );
						
		} else {
			//Normally putting furor speed on unarmed attacks would be unnecessary
			//But there's going to be that one guy who gets a furor+force ring combo
			//This is for that one guy, you shall get your fists of fury!
			int bonus = 0;
			for (Buff buff : buffs(RingOfFuror.Furor.class)) {
				bonus += ((RingOfFuror.Furor)buff).level;
			}
			return (float)(0.25 + (1 - 0.25)*Math.pow(0.8, bonus));
		}
	}

	@Override
	public void spend( float time ) {
		TimekeepersHourglass.timeFreeze buff = buff(TimekeepersHourglass.timeFreeze.class);
		if (!(buff != null && buff.processTime(time)))
			super.spend( time );
	}
	
	public void spendAndNext( float time ) {
		busy();
		spend(time);
		next();
	}

	@Override
	public boolean act() {
		
		super.act();
		
		if (paralysed) {
			
			curAction = null;
			
			spendAndNext( TICK );
			return false;
		}

		Egg egg = belongings.getItem(Egg.class);
		if (egg!=null){
			egg.moves++;
		}

		EasterEgg egg2 = belongings.getItem(EasterEgg.class);
		if (egg2!=null){
			egg2.moves++;
		}
		
		checkVisibleMobs();

		
		if (curAction == null) {
			
			if (resting) {
				spend( TIME_TO_REST ); next();
				return false;
			}
			
			ready();
			return false;
			
		} else {

            resting = false;

            ready = false;

            if (curAction instanceof HeroAction.Move) {

                return actMove((HeroAction.Move) curAction);

            } else if (curAction instanceof HeroAction.Interact) {

                return actInteract((HeroAction.Interact) curAction);

            } else if (curAction instanceof HeroAction.InteractPet) {

                return actInteractPet((HeroAction.InteractPet) curAction);

            } else if (curAction instanceof HeroAction.Buy) {

                return actBuy((HeroAction.Buy) curAction);

            } else if (curAction instanceof HeroAction.PickUp) {

                return actPickUp((HeroAction.PickUp) curAction);

            } else if (curAction instanceof HeroAction.OpenChest) {

                return actOpenChest((HeroAction.OpenChest) curAction);

            } else if (curAction instanceof HeroAction.Unlock) {

                return actUnlock((HeroAction.Unlock) curAction);

            } else if (curAction instanceof HeroAction.Rummage) {

                return actExamine((HeroAction.Rummage) curAction);

            } else if (curAction instanceof HeroAction.Descend) {

                return actDescend((HeroAction.Descend) curAction);

            } else if (curAction instanceof HeroAction.Ascend) {

                return actAscend((HeroAction.Ascend) curAction);

            } else if (curAction instanceof HeroAction.Attack) {

                return actAttack((HeroAction.Attack) curAction);

            } else if (curAction instanceof HeroAction.Donate) {
                return actDonate((HeroAction.Donate) curAction);
            } else if (curAction instanceof HeroAction.Alchemy) {
                return actAlchemy((HeroAction.Alchemy) curAction);
            }
        }
		return false;
	}
	
	public void busy() {
		ready = false;
	}
	
	public void ready() {
		if(sprite != null){
			sprite.idle();
		}

		curAction = null;
		damageInterrupt = true;
		ready     = true;

		AttackIndicator.updateState();

		GameScene.ready();
	}
	
	public void interrupt() {
		if (isAlive() && curAction != null && curAction instanceof HeroAction.Move && curAction.dst != pos) {
			lastAction = curAction;
		}
		curAction = null;
	}
	
	public void resume() {
		curAction = lastAction;
		lastAction = null;
		damageInterrupt = false;
		act();
	}
	
	private boolean actMove( HeroAction.Move action ) {

		if (getCloser(action.dst)) {

			return true;

		} else {
			if (Dungeon.level.map[pos] == Terrain.SIGN) {
				Sign.read(pos);
			}
			ready();

			return false;
		}
	}
	
	private boolean actInteract( HeroAction.Interact action ) {
		
		NPC npc = action.npc;

		if (Level.adjacent( pos, npc.pos )) {
			
			ready();
			sprite.turnTo( pos, npc.pos );
			npc.interact();
			return false;
			
		} else {
			
			if (Level.fieldOfView[npc.pos] && getCloser( npc.pos )) {

				return true;

			} else {
				ready();
				return false;
			}
			
		}
	}

	private boolean actInteractPet(HeroAction.InteractPet action) {

		PET pet = action.pet;

		if (Level.adjacent(pos, pet.pos)) {

			ready();
			sprite.turnTo(pos, pet.pos);
			pet.interact();
			return false;

		} else {

			if (Level.fieldOfView[pet.pos] && getCloser(pet.pos)) {

				return true;

			} else {
				ready();
				return false;
			}

		}
	}
	
	private boolean actBuy( HeroAction.Buy action ) {
		int dst = action.dst;
		if (pos == dst || Level.adjacent( pos, dst )) {

			ready();
			
			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1) {
				GameScene.show( new WndTradeItem( heap, true ) );
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actDonate( HeroAction.Donate action ) {
		int dst = action.dst;
		if (Dungeon.visible[dst]) {

			ready();
			HolyAltar.operate(this, dst);
			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAlchemy( HeroAction.Alchemy action ) {
		int dst = action.dst;
		if (Level.distance(dst, pos) <= 1) {

			ready();
			GameScene.show(new WndAlchemy());
			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actPickUp( HeroAction.PickUp action ) {
		int dst = action.dst;
		if (pos == dst) {
			
			Heap heap = Dungeon.level.heaps.get( pos );
			if (heap != null) {
				Item item = heap.pickUp();
				if (item.doPickUp( this )) {
					
					if (item instanceof Dewdrop
							|| item instanceof TimekeepersHourglass.sandBag
							|| item instanceof DriedRose.Petal) {
						//Do Nothing
					} else {

						boolean important =
								((item instanceof ScrollOfUpgrade || item instanceof ScrollOfMagicalInfusion) && ((Scroll)item).isKnown()) ||
								((item instanceof PotionOfMight || item instanceof PotionOfStrength) && ((Potion)item).isKnown());
						if (important) {
							GLog.p( TXT_YOU_NOW_HAVE, item.name() );
						} else {
							GLog.i( TXT_YOU_NOW_HAVE, item.name() );
						}

						if (Dungeon.difficultyLevel == Dungeon.DIFF_TUTOR) {
							if ((!Dungeon.tutorial_food_found) && (item instanceof Food)) {
								Dungeon.tutorial_food_found = true;
								GameScene.show(new WndMessage("You just picked up some food.  Food is an important resource in this game; " +
									"Although wasted When you are full, food can save you when you are starving.  Other things " +
									"can also affect your hunger, like leveling up (at easier difficulties), drinking " +
									"some potions, and even some items.  Eat food when you are hungry."));
							} else if ((!Dungeon.tutorial_key_found) && (item instanceof Key)) {
								Dungeon.tutorial_key_found = true;
								GameScene.show(new WndMessage("You just picked up a key; Somewhere on this level is a locked door that this " +
									"key will open.  There is usually something interesting or valuable inside of a locked room."));
							}
						}
					}

					if (!heap.isEmpty()) {
						GLog.i( TXT_SOMETHING_ELSE );
					}
					curAction = null;
				} else {
					Dungeon.level.drop( item, pos ).sprite.drop();
					ready();
				}
			} else {
				ready();
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actOpenChest( HeroAction.OpenChest action ) {
		int dst = action.dst;
		if (Level.adjacent( pos, dst ) || pos == dst) {
			
			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && (heap.type != Type.HEAP && heap.type != Type.FOR_SALE)) {

				theKey = null;
				
				if (heap.type == Type.LOCKED_CHEST || heap.type == Type.CRYSTAL_CHEST) {

					theKey = belongings.getKey( GoldenKey.class, Dungeon.depth );
					
					if (theKey == null) {
                        hummingtool = belongings.getItem(HummingTool.class);
                        if (hummingtool != null && (hummingtool.charge() > 0)) {
                            GameScene.show( new WndLockpick(this, dst ) );
                            return false;
                        } else {
                            GLog.w(TXT_LOCKED_CHEST);
                            ready();
                            return false;
                        }
					}
				}
				
				switch (heap.type) {
				case TOMB:
					Sample.INSTANCE.play( Assets.SND_TOMB );
					Camera.main.shake( 1, 0.5f );
					break;
				case SKELETON:
				case REMAINS:
					break;
				default:
					Sample.INSTANCE.play( Assets.SND_UNLOCK );
				}
				
				spend(Key.TIME_TO_UNLOCK);
				sprite.operate(dst);
				
			} else {
				ready();
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actExamine( HeroAction.Rummage action ) {

		int dest = action.dst;
		if (Level.adjacent( pos, dest )) {

			spend(Hero.TIME_TO_PICKUP);
			sprite.operate(dest);

			return false;

		} else if (getCloser( dest )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actUnlock( HeroAction.Unlock action ) {
		int doorCell = action.dst;
		if (Level.adjacent( pos, doorCell )) {
			
			theKey = null;
            hummingtool = null;
			int door = Dungeon.level.map[doorCell];
			
			if (door == Terrain.LOCKED_DOOR) {
				
				theKey = belongings.getKey( IronKey.class, Dungeon.depth );
				
			} else if (door == Terrain.LOCKED_EXIT) {
				
				theKey = belongings.getKey( SkeletonKey.class, Dungeon.depth );
				
			}
			
			if (theKey != null) {
				
				spend( Key.TIME_TO_UNLOCK );
				sprite.operate( doorCell );
				
				Sample.INSTANCE.play( Assets.SND_UNLOCK );
				
			} else {
                Log.e("Enter Window Cell: ", String.valueOf(doorCell));
                hummingtool = belongings.getItem(HummingTool.class);
				if (hummingtool != null && (hummingtool.charge() > 0) && (door == Terrain.LOCKED_DOOR)) {
                    GameScene.show( new WndLockpick(this, doorCell ) );
                } else {
                    GLog.w(TXT_LOCKED_DOOR);
                    ready();
                }
			}

			return false;

		} else if (getCloser( doorCell )) {

			return true;

		} else {
			ready();
			return false;
		}
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
		// Changed to not have the pet be right by your side when going up/down. Field of view is enough.
		if (Level.fieldOfView[checkpet().pos]) {
			return true;
		}
		return false;
	}
	
	private boolean actDescend( HeroAction.Descend action ) {
		int stairs = action.dst;
		if (pos == stairs && pos == Dungeon.level.exit) {
			
			curAction = null;

			// reward the player for going down the steps by reducing the hunger level
			Hunger hunger = buff( Hunger.class );
			switch (Dungeon.difficultyLevel) {
				case Dungeon.DIFF_TUTOR:
				case Dungeon.DIFF_EASY:
					hunger.reduceHunger( Hunger.STARVING / 10 );
					break;
				case Dungeon.DIFF_NORM:
				case Dungeon.DIFF_ENDLESS:
				case Dungeon.DIFF_TEST:
					hunger.reduceHunger( Hunger.STARVING / 20 );
					break;
				case Dungeon.DIFF_HARD:
					hunger.reduceHunger( Hunger.STARVING / 40 );
					break;
			}

			PET pet = checkpet();
			if(pet!=null && checkpetNear()){
				Dungeon.hero.petType=pet.type;
				Dungeon.hero.petLevel=pet.level;
				Dungeon.hero.petKills=pet.kills;
				Dungeon.hero.petHP=pet.HP;
				Dungeon.hero.petExperience=pet.experience;
				Dungeon.hero.petCooldown=pet.cooldown;
				pet.destroy();
				petfollow=true;
			} else if (Dungeon.hero.haspet && Dungeon.hero.petfollow) {
				petfollow=true;
			} else {
				petfollow=false;
			}

			Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
			if (buff != null) buff.detach();

			for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] ))
				if (mob instanceof DriedRose.GhostHero) mob.destroy();
			
			InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
			Game.switchScene( InterlevelScene.class );

			return false;

		} else if (getCloser( stairs )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actAscend( HeroAction.Ascend action ) {
		int stairs = action.dst;
		if (pos == stairs && pos == Dungeon.level.entrance) {
			
			if (Dungeon.depth == 1) {
				
				if (belongings.getItem( Amulet.class ) == null) {
					GameScene.show( new WndMessage( TXT_LEAVE ) );
					ready();
				} else {
					Dungeon.win( ResultDescriptions.WIN );
					Dungeon.deleteGame( Dungeon.hero.heroClass, true );
					Game.switchScene( SurfaceScene.class );
				}
				
			} else {
				
				curAction = null;
				
				Hunger hunger = buff( Hunger.class );
				if (hunger != null && !hunger.isStarving()) {
					hunger.reduceHunger( -Hunger.STARVING / 10 );
				}

				PET pet = checkpet();
				if(pet!=null && checkpetNear()){
					Dungeon.hero.petType=pet.type;
					Dungeon.hero.petLevel=pet.level;
					Dungeon.hero.petKills=pet.kills;
					Dungeon.hero.petHP=pet.HP;
					Dungeon.hero.petExperience=pet.experience;
					Dungeon.hero.petCooldown=pet.cooldown;
					pet.destroy();
					petfollow=true;
				} else if (Dungeon.hero.haspet && Dungeon.hero.petfollow) {
					petfollow=true;
				} else if (pet!=null) {
					petfollow=false;
                    GLog.w("Your " + pet.name + " is too far away to follow you to the next level.");
				}

				Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
				if (buff != null) buff.detach();

				for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] ))
					if (mob instanceof DriedRose.GhostHero) mob.destroy();

				InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
				Game.switchScene( InterlevelScene.class );
			}

			return false;

		} else if (getCloser( stairs )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actAttack( HeroAction.Attack action ) {

		enemy = action.target;

		if (Level.adjacent( pos, enemy.pos ) && enemy.isAlive() && !isCharmedBy( enemy )) {
			
			spend( attackDelay() );
			sprite.attack( enemy.pos );

			return false;

		} else {

			if (Level.fieldOfView[enemy.pos] && getCloser( enemy.pos )) {

				return true;

			} else {
				ready();
				return false;
			}

		}
	}
	
	public void rest( boolean tillHealthy ) {
		spendAndNext( TIME_TO_REST );
		if (!tillHealthy) {
			sprite.showStatus( CharSprite.DEFAULT, TXT_WAIT );
		}
		resting = tillHealthy;
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		KindOfWeapon wep = rangedWeapon != null ? rangedWeapon : belongings.weapon;

		if (wep != null)  wep.proc( this, enemy, damage );
			
		switch (subClass) {
		case GLADIATOR:
			if (wep instanceof MeleeWeapon || wep == null) {
				damage += Buff.affect( this, Combo.class ).hit( enemy, damage );
			}
			break;
		case SNIPER:
			if (rangedWeapon != null) {
				Buff.prolong( this, SnipersMark.class, attackDelay() * 1.1f ).object = enemy.id();
			}
			break;
		default:
		}

		
		return damage;
	}
	
	@Override
	public int defenseProc( Char enemy, int damage ) {
		
		Earthroot.Armor armor = buff( Earthroot.Armor.class );
		if (armor != null) {
			damage = armor.absorb(damage);
		}

		Sungrass.Health health = buff( Sungrass.Health.class );
		if (health != null) {
			health.absorb( damage );
		}
		
		if (belongings.armor != null) {
			damage = belongings.armor.proc( enemy, this, damage );
		}
		
		return damage;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
		if (buff(TimekeepersHourglass.timeStasis.class) != null)
			return;

		if (!(src instanceof Hunger || src instanceof Viscosity.DeferedDamage) && damageInterrupt) {
			interrupt();
			resting = false;
		}

		if (this.buff(Drowsy.class) != null){
			Buff.detach(this, Drowsy.class);
			GLog.w("The pain helps you resist the urge to sleep.");
		}

        ShieldOfWonders sow = Dungeon.hero.belongings.getItem(ShieldOfWonders.class);
		if (sow != null && sow.isEquipped(this) && src instanceof Char && !(src instanceof Hero.Doom) && !(src instanceof Scroll)) {
            float sowchance = max((sow.level * 0.04f), 0.06f);
            float rest = 1f - sowchance;
			switch (Random.chances(new float[]{rest, sowchance})){
                case 0:
                    break;
                case 1:
					Dungeon.hero.sprite.showStatus(CharSprite.PURPLE, "blocked" );
					sow.causeEffect(this, (Char)src);
                    break;
            }

		}

		CapeOfThorns.Thorns thorns = buff( CapeOfThorns.Thorns.class );
		if (thorns != null) {
			dmg = thorns.proc(dmg, (src instanceof Char ? (Char)src : null),  this);
		}

		int tenacity = 0;
		for (Buff buff : buffs(RingOfTenacity.Tenacity.class)) {
			tenacity += ((RingOfTenacity.Tenacity)buff).level;
		}
		if (tenacity != 0) //(HT - HP)/HT = heroes current % missing health.
			dmg = (int)Math.ceil((float)dmg * Math.pow(0.9, tenacity*((float)(HT - HP)/HT)));

		super.damage( dmg, src );
		
		if (subClass == HeroSubClass.BERSERKER && 0 < HP && HP <= HT * Fury.LEVEL) {
			Buff.affect(this, Fury.class);
            ((HeroSprite)Dungeon.hero.sprite).refresh();
			StatusPane.refreshavatar();
		}
	}
	
	private void checkVisibleMobs() {
		ArrayList<Mob> visible = new ArrayList<>();

		boolean newMob = false;
		
		for (Mob m : Dungeon.level.mobs) {
			if (Level.fieldOfView[ m.pos ] && m.hostile) {
				visible.add( m );
				if (!visibleEnemies.contains( m )) {
					newMob = true;
				}
			}
		}
		
		if (newMob) {
			interrupt();
			resting = false;
		}
		
		visibleEnemies = visible;
	}
	
	public int visibleEnemies() {
		return visibleEnemies.size();
	}
	
	public Mob visibleEnemy( int index ) {
		return visibleEnemies.get(index % visibleEnemies.size());
	}
	
	private boolean getCloser( final int target ) {
		
		if (rooted) {
			Camera.main.shake( 1, 1f );
			return false;
		}
		
		int step = -1;
		
		if (Level.adjacent( pos, target )) {
			
			if (Actor.findChar( target ) == null) {
				if (Level.pit[target] && !flying && !Chasm.jumpConfirmed) {
					if (!Level.solid[target]) {
						Chasm.heroJump(this);
						interrupt();
					}
					return false;
				}
				if (Level.passable[target] || Level.avoid[target]) {
					step = target;
				}
			}
			
		} else {
		
			int len = Level.LENGTH;
			boolean[] p = Level.passable;
			boolean[] v = Dungeon.level.visited;
			boolean[] m = Dungeon.level.mapped;
			boolean[] passable = new boolean[len];
			for (int i=0; i < len; i++) {
				passable[i] = p[i] && (v[i] || m[i]);
			}
			
			step = Dungeon.findPath( this, pos, target, passable, Level.fieldOfView );
		}
		
		if (step != -1) {
			sprite.move(pos, step);
			move(step);
			spend( 1 / speed() );
			
			return true;
		} else {
			return false;
		}

	}
	
	public boolean handle( int cell ) {
		
		if (cell == -1) {
			return false;
		}
		
		Char ch;
		Heap heap;
		
		if (Dungeon.level.map[cell] == Terrain.ALCHEMY && cell != pos) {
			
			curAction = new HeroAction.Alchemy( cell );
			
		} else if (Dungeon.level.map[cell] == Terrain.ALTAR && cell != pos) {

			curAction = new HeroAction.Donate( cell );

		} else if (Level.fieldOfView[cell] && (ch = Actor.findChar( cell )) instanceof Mob) {

			if (ch instanceof NPC) {
				curAction = new HeroAction.Interact( (NPC)ch );
			} else if (ch instanceof PET) {
				curAction = new HeroAction.InteractPet((PET) ch);
			} else {
				curAction = new HeroAction.Attack( ch );
			}
			
		} else if ((heap = Dungeon.level.heaps.get( cell )) != null) {

			switch (heap.type) {
			case HEAP:
				curAction = new HeroAction.PickUp( cell );
				break;
			case FOR_SALE:
				curAction = heap.size() == 1 && heap.peek().price() > 0 ?
					new HeroAction.Buy( cell ) :
					new HeroAction.PickUp( cell );
				break;
			default:
				curAction = new HeroAction.OpenChest( cell );
			}
			
		} else if (Dungeon.level.map[cell] == Terrain.LOCKED_DOOR || Dungeon.level.map[cell] == Terrain.LOCKED_EXIT) {
			
			curAction = new HeroAction.Unlock( cell );
			
		} else if (Dungeon.level.map[cell] == Terrain.BOOKSHELF) {

			curAction = new HeroAction.Rummage( cell );

		} else if (cell == Dungeon.level.exit &&
				((Dungeon.depth < Level.MAX_DEPTH) || (Dungeon.difficultyLevel == Dungeon.DIFF_ENDLESS))) {
			
			curAction = new HeroAction.Descend( cell );
			
		} else if (cell == Dungeon.level.entrance) {
			if (Dungeon.difficultyLevel == Dungeon.DIFF_ENDLESS) {
				curAction = new HeroAction.Move( cell );
				lastAction = null;
				WndStory.showChapter("The magic of this place compels you to go forever downwards.");

			} else {
				curAction = new HeroAction.Ascend(cell);
			}
			
		} else  {
			
			curAction = new HeroAction.Move( cell );
			lastAction = null;
		}

		return act();
	}
	
	public void earnExp( int exp ) {
		
		this.exp += exp;
		float percent = exp/(float)maxExp();

		EtherealChains.chainsRecharge chains = buff(EtherealChains.chainsRecharge.class);
		if (chains != null) chains.gainExp(percent);

		if (subClass == HeroSubClass.WARLOCK) {

			int healed = Math.round(Math.min(HT - HP, (HT * percent * 0.35f) + 1));
			if (healed > 0) {
				HP += healed;
				sprite.emitter().burst( Speck.factory( Speck.HEALING ), percent > 0.3f ? 2 : 1 );
			}

			(buff( Hunger.class )).consumeSoul( Hunger.STARVING*percent );
		}
		
		boolean levelUp = false;
		while (this.exp >= maxExp()) {
			this.exp -= maxExp();
			if (lvl < MAX_LEVEL || Dungeon.difficultyLevel == Dungeon.DIFF_ENDLESS) {
				lvl++;
				levelUp = true;

				// scaled to reach ~135 hp by level 30 with Normal difficulty
				switch (Dungeon.difficultyLevel) {
					case Dungeon.DIFF_TUTOR:
					case Dungeon.DIFF_EASY:
						HT += 6 - ( lvl / 15);
						HP += 6 - ( lvl / 15);
						attackSkill++;
						defenseSkill++;
						break;
					case Dungeon.DIFF_HARD:
						HT += 4 - ( lvl / 13);
						HP += 4 - ( lvl / 13);
						attackSkill++;
						defenseSkill++;
						break;
					case Dungeon.DIFF_NTMARE:
						HT += 3 - ( lvl / 13);
						HP += 3 - ( lvl / 13);
						attackSkill++;
						defenseSkill++;
						break;
					default:
						HT += 5 - ( lvl / 13);
						HP += 5 - ( lvl / 13);
						attackSkill++;
						defenseSkill++;
						break;
				}

			} else {
				Buff.prolong(this, Bless.class, 30f);
				this.exp = 0;

				GLog.p( TXT_LEVEL_CAP );
				Sample.INSTANCE.play( Assets.SND_LEVELUP );
			}

			// some bonuses for leveling up, you regain up to 10% of your health and 10% of your hunger
			switch (Dungeon.difficultyLevel) {
				case Dungeon.DIFF_TUTOR:
				case Dungeon.DIFF_EASY:
					HP = HT;
					(buff(Hunger.class)).reduceHunger(buff(Hunger.class).STARVING / 2);
					break;
				case Dungeon.DIFF_HARD:
				case Dungeon.DIFF_NTMARE:
					break;
				default:
					HP = HP + (HT / 10);
					if (HP > HT) {
						HP = HT;
					}
					(buff(Hunger.class)).reduceHunger(buff(Hunger.class).STARVING / 10);
					break;
			}

			if (lvl < 10) {
				updateAwareness();
			}
		}

		if (levelUp) {

			GLog.p( TXT_NEW_LEVEL, lvl);
			sprite.showStatus(CharSprite.POSITIVE, TXT_LEVEL_UP );
			Sample.INSTANCE.play( Assets.SND_LEVELUP );

			Badges.validateLevelReached();
		}
	}
	
	public int maxExp() {
		// scaled so that we need 3000 total experience to reach level 30
		if (lvl < 13) {
			return (5 + (lvl * 5));
		}
		else {
			return (60 + ((lvl - 12) * 10));
		}
	}
	
	void updateAwareness() {
		awareness = (float)(1 - Math.pow(
			(heroClass == HeroClass.FUMBLES ? 0.85 : 0.90),
			(1 + Math.min( lvl,  9 )) * 0.5
		));
	}
	
	public boolean isStarving() {
		return buff(Hunger.class) != null && (buff( Hunger.class )).isStarving();
	}
	
	@Override
	public void add( Buff buff ) {

		if (buff(TimekeepersHourglass.timeStasis.class) != null)
			return;

		super.add(buff);

		if (sprite != null) {
			if (buff instanceof Burning) {
				GLog.w( "You catch fire!" );
				interrupt();
			} else if (buff instanceof Paralysis) {
				GLog.w( "You are paralysed!" );
				interrupt();
			} else if (buff instanceof Poison) {
				GLog.w( "You are poisoned!" );
				interrupt();
			} else if (buff instanceof Ooze) {
				GLog.w( "Caustic ooze eats your flesh. Wash it away!" );
			} else if (buff instanceof Roots) {
				GLog.w( "You can't move!" );
			} else if (buff instanceof Weakness) {
				GLog.w( "You feel weakened!" );
			} else if (buff instanceof Blindness) {
				GLog.w( "You are blinded!" );
			} else if (buff instanceof Fury) {
				GLog.w( "You become furious!" );
			} else if (buff instanceof Charm) {
				GLog.w( "You are charmed!" );
			}  else if (buff instanceof Cripple) {
				GLog.w( "You are crippled!" );
			} else if (buff instanceof Bleeding) {
				GLog.w( "You are bleeding!" );
			} else if (buff instanceof RingOfMight.Might){
				if (((RingOfMight.Might)buff).level > 0) {
					HT += ((RingOfMight.Might) buff).level * 5;
				}
			} else if (buff instanceof Vertigo) {
				GLog.w("Everything is spinning around you!");
				interrupt();
            } else if (buff instanceof Euphoria) {
                GLog.w("Whoooheee! Look! Aww...sooo cute!");
                interrupt();
			}

		}
		
		BuffIndicator.refreshHero();
	}
	
	@Override
	public void remove( Buff buff ) {
		super.remove( buff );
		
		if (buff instanceof RingOfMight.Might){
			if (((RingOfMight.Might)buff).level > 0){
				HT -= ((RingOfMight.Might) buff).level * 5;
				HP = Math.min(HT, HP);
			}
		}
		
		BuffIndicator.refreshHero();
	}
	
	@Override
	public int stealth() {
		int stealth = super.stealth();
		for (Buff buff : buffs( RingOfEvasion.Evasion.class )) {
			stealth += ((RingOfEvasion.Evasion)buff).effectiveLevel;
		}
		return stealth;
	}
	
	@Override
	public void die( Object cause  ) {
		
		curAction = null;

		Ankh ankh = null;

		//look for ankhs in player inventory, prioritize ones which are blessed.
		for (Item item : belongings){
			if (item instanceof Ankh) {
				if (ankh == null || ((Ankh) item).isBlessed()) {
					ankh = (Ankh) item;
				}
			}
		}

		if (ankh != null && ankh.isBlessed()) {
			this.HP = HT/2;

			//ensures that you'll get to act first in almost any case, to prevent reviving and then instantly dieing again.
			Buff.detach(this, Paralysis.class);
			spend(-cooldown());

			new Flare(8, 32).color(0xFFFF66, true).show(sprite, 2f);
			CellEmitter.get(this.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

			ankh.detach(belongings.backpack);

			Sample.INSTANCE.play( Assets.SND_TELEPORT );
			GLog.w( ankh.TXT_REVIVE );
			Statistics.ankhsUsed++;

			return;
		}
		
		Actor.fixTime();
		super.die(cause);

		if (ankh == null) {
			
			reallyDie( cause );
			
		} else {
			
			Dungeon.deleteGame( Dungeon.hero.heroClass, false );
			GameScene.show( new WndResurrect( ankh, cause ) );
			
		}
	}
	
	public static void reallyDie( Object cause ) {
		
		int length = Level.LENGTH;
		int[] map = Dungeon.level.map;
		boolean[] visited = Dungeon.level.visited;
		boolean[] discoverable = Level.discoverable;
		
		for (int i=0; i < length; i++) {
			
			int terr = map[i];
			
			if (discoverable[i]) {
				
				visited[i] = true;
				if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
					Dungeon.level.discover( i );
				}
			}
		}
		
		Bones.leave();
		
		Dungeon.observe();
		// Removed because it allows to know items on loading the game from a slot after death.
		//Dungeon.hero.belongings.identify();

		int pos = Dungeon.hero.pos;

		ArrayList<Integer> passable = new ArrayList<>();
		for (Integer ofs : Level.NEIGHBOURS8) {
			int cell = pos + ofs;
			if ((Level.passable[cell] || Level.avoid[cell]) && Dungeon.level.heaps.get( cell ) == null) {
				passable.add( cell );
			}
		}
		Collections.shuffle( passable );

		ArrayList<Item> items = new ArrayList<>( Dungeon.hero.belongings.backpack.items );
		for (Integer cell : passable) {
			if (items.isEmpty()) {
				break;
			}

			Item item = Random.element( items );
			Dungeon.level.drop( item, cell ).sprite.drop( pos );
			items.remove( item );
		}

		GameScene.gameOver();
		
		if (cause instanceof Hero.Doom) {
			((Hero.Doom)cause).onDeath();
		}
		
		Dungeon.deleteGame( Dungeon.hero.heroClass, true );
	}
	
	@Override
	public void move( int step ) {
		super.move(step);
		
		if (!flying) {
			
			if (Level.water[pos]) {
				Sample.INSTANCE.play( Assets.SND_WATER, 1, 1, Random.Float( 0.8f, 1.25f ) );
			} else {
				Sample.INSTANCE.play( Assets.SND_STEP );
			}
			Dungeon.level.press(pos, this);
		}
	}
	
	@Override
	public void onMotionComplete() {
		Dungeon.observe();
		search(false);
			
		super.onMotionComplete();
	}
	
	@Override
	public void onAttackComplete() {
		
		AttackIndicator.target(enemy);
		
		attack( enemy );
		curAction = null;
		
		Invisibility.dispel();

		super.onAttackComplete();
	}
	
	@Override
	public void onOperateComplete() {
		
		if (curAction instanceof HeroAction.Unlock) {
			
			if (theKey != null) {
				theKey.detach( belongings.backpack );
				theKey = null;
			}
			
			int doorCell = ((HeroAction.Unlock)curAction).dst;
			int door = Dungeon.level.map[doorCell];
			
			Level.set( doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR : Terrain.UNLOCKED_EXIT );
			GameScene.updateMap( doorCell );
			
		} else if (curAction instanceof HeroAction.Rummage) {

			int cell = ((HeroAction.Rummage)curAction).dst;

			Bookshelf.rummage(cell);

		} else if (curAction instanceof HeroAction.OpenChest) {
			
			if (theKey != null) {
				theKey.detach( belongings.backpack );
				theKey = null;
			}
			
			Heap heap = Dungeon.level.heaps.get( ((HeroAction.OpenChest)curAction).dst );
			if (heap.type == Type.SKELETON || heap.type == Type.REMAINS) {
				Sample.INSTANCE.play( Assets.SND_BONES );
			}
			heap.open( this );
		}
		curAction = null;

		super.onOperateComplete();
	}
	
	public boolean search( boolean intentional ) {
		
		boolean smthFound = false;
		int distance = 1;
		int bonus = 0;
		if (intentional) {
			RingOfSearching.EasySearch bonusSearch = buff( RingOfSearching.EasySearch.class );
			if (bonusSearch != null) {
				if (bonusSearch.level >= 0) {
					bonus = bonusSearch.level + 1;
					distance = 2;
				} else {
					bonus = -1;
				}
			}
		}

		float level = intentional ? (2 * awareness - awareness * awareness) : awareness;

		int cx = pos % Level.WIDTH;
		int cy = pos / Level.WIDTH;
		int ax = cx - distance;
		if (ax < 0) {
			ax = 0;
		}
		int bx = cx + distance;
		if (bx >= Level.WIDTH) {
			bx = Level.WIDTH - 1;
		}
		int ay = cy - distance;
		if (ay < 0) {
			ay = 0;
		}
		int by = cy + distance;
		if (by >= Level.HEIGHT) {
			by = Level.HEIGHT - 1;
		}

		TalismanOfForesight.Foresight foresight = buff( TalismanOfForesight.Foresight.class );

		//cursed talisman of foresight makes unintentionally finding things impossible.
		if (foresight != null && foresight.isCursed()){
			level = -1;
		}
		
		for (int y = ay; y <= by; y++) {
			for (int x = ax, p = ax + y * Level.WIDTH; x <= bx; x++, p++) {
				
				if (Dungeon.visible[p]) {
					
					if (intentional) {
						sprite.parent.addToBack( new CheckedCell( p ) );
					}
					
					if (Level.secret[p] && (intentional || Random.Float() < level)) {
						
						int oldValue = Dungeon.level.map[p];
						
						GameScene.discoverTile( p, oldValue );
						
						Dungeon.level.discover( p );
						
						ScrollOfMagicMapping.discover( p );
						
						smthFound = true;

						if (foresight != null && !foresight.isCursed())
							foresight.charge();
					}
				}
			}
		}

		
		if (intentional) {
			sprite.showStatus(CharSprite.DEFAULT, TXT_SEARCH);
			sprite.operate( pos );
			float myTimeToSearch = TIME_TO_SEARCH;
			if (intentional) {
				if (bonus < 0) {
					myTimeToSearch *= 2;
				} else if (bonus > 0) {
					myTimeToSearch = TIME_TO_SEARCH + 1 - (bonus * .1f);
				}
			}
			if (foresight != null && foresight.isCursed()){
				GLog.n("You can't concentrate, searching takes a while.");
				spendAndNext(myTimeToSearch * 3);
			} else {
				spendAndNext(myTimeToSearch);
			}
		}
		
		if (smthFound) {
			GLog.w( TXT_NOTICED_SMTH );
			Sample.INSTANCE.play( Assets.SND_SECRET );
			interrupt();
		}
		
		return smthFound;
	}
	
	public void resurrect( int resetLevel ) {
		
		HP = HT;
		Dungeon.gold = 0;
		exp = 0;
		
		belongings.resurrect( resetLevel );

		live();
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		RingOfElements.Resistance r = buff( RingOfElements.Resistance.class );
		return r == null ? super.resistances() : r.resistances();
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		HashSet<Class<?>> immunities = new HashSet<>();
		for (Buff buff : buffs()){
			for (Class<?> immunity : buff.immunities)
				immunities.add(immunity);
		}
		return immunities;
	}

	@Override
	public void next() {
		super.next();
	}

	public interface Doom {
		void onDeath();
	}
}
