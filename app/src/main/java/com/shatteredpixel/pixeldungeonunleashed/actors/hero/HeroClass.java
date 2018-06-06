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

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Badges;
import com.shatteredpixel.pixeldungeonunleashed.Challenges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.GoblinsPixelDungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Fury;
import com.shatteredpixel.pixeldungeonunleashed.items.TomeOfMastery;
import com.shatteredpixel.pixeldungeonunleashed.items.Torch;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.PlateArmor;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.CloakOfShadows;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.ShieldOfWonders;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.AnkhChain;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.PotionBandolier;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.SeedPouch;
import com.shatteredpixel.pixeldungeonunleashed.items.food.Food;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfExperience;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfHealing;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfMight;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfMindVision;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfToxicGas;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.Crossbow;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.Glaive;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.MagesStaff;
import com.shatteredpixel.pixeldungeonunleashed.items.wands.WandOfMagicMissile;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.Dagger;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.ShortSword;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.Boomerang;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.darts.HealingDart;
import com.watabou.utils.Bundle;

public enum HeroClass {

	COMPLAINS( "complains" ), CHIEF( "chief" ), FUMBLES( "fumbles" ), THACO( "thaco" );
	
	private String title;
	
	HeroClass( String title ) {
		this.title = title;
	}
	
	public static final String[] WAR_PERKS = {
		"Complains starts with 11 points of Strength.",
		"... starts with a short sword +1. This sword can be later \"reforged\" to upgrade another melee weapon.",
		"... is less proficient with missile weapons.",
		"Any piece of food restores some health when eaten.",
		"Complains is less proficient with magic, and reading scrolls can cause psychic feedback."
	};
	
	public static final String[] MAG_PERKS = {
		"Chief starts with a unique Staff, which can be imbued with the properties of a wand.",
		"Chief's staff can be used as a melee weapon or a more powerful wand.",
		"Chief partially identifies wands after using them.",
		"When eaten, any piece of food restores 1 charge for all wands in the inventory.",
		"Scrolls of Upgrade and Identify are identified from the beginning.",
		"Wands charge faster for him."
	};
	// switch go without food longer and thac0s more health from dew.
	public static final String[] ROG_PERKS = {
		"Fumbles starts with the unique Moustache of Idiocy and a magic dagger.",
		"... identifies a type of a ring on equipping it.",
		"... is proficient with light armor, dodging better with excess strength.",
		"... detects hidden doors and traps better by the art of splying.",
		"... gains more health from dewdrops.",
		"Scrolls of Magic Mapping and Potions of Toxic Gas are identified from the beginning."
	};
	
	public static final String[] HUN_PERKS = {
		"Thac0 starts with 18 points of Health and his unique upgradeable swordcane.",
		"... is proficient with missile weapons, getting bonus damage from excess strength.",
		"... is able to recover a single used missile weapon from each enemy.",
		"... can go without food longer.",
		"... senses neighbouring monsters even if they are hidden behind obstacles.",
		"Potions of Mind Vision are identified from the beginning.",
		"Thac0 gets slightly better loot drops from mobs"
	};

	public void initHero( Hero hero ) {

		hero.heroClass = this;

		initCommon( hero );

		switch (this) {
			case COMPLAINS:
				initWarrior( hero );
				break;

			case CHIEF:
				initMage( hero );
				break;

			case FUMBLES:
				initRogue( hero );
				break;

			case THACO:
				initHuntress( hero );
				break;
		}

		if (Badges.isUnlocked( masteryBadge() )) {
			new TomeOfMastery().collect();
		}

		//hero.updateAwareness();
	}

	private static void initCommon( Hero hero ) {
//		if ((!Dungeon.isChallenged(Challenges.NO_ARMOR)) &&
//				((Dungeon.difficultyLevel <= Dungeon.DIFF_HARD) || (Dungeon.difficultyLevel == Dungeon.DIFF_ENDLESS)))
//			(hero.belongings.armor = new ClothArmor()).identify()
		// removed to show goblins in natural state a bit longer until player finds armor.

		if ((!Dungeon.isChallenged(Challenges.NO_FOOD)) &&
				((Dungeon.difficultyLevel <= Dungeon.DIFF_NORM) || (Dungeon.difficultyLevel == Dungeon.DIFF_ENDLESS)))
			new Food().identify().collect();

		if ((!Dungeon.isChallenged(Challenges.NO_FOOD)) && (Dungeon.difficultyLevel <= Dungeon.DIFF_EASY)) {
			for (int i = 0; i < 3; i++) {
				new Food().identify().collect();
				new ScrollOfIdentify().identify().collect();
			}
		}
		if (Dungeon.difficultyLevel == Dungeon.DIFF_TEST) {
			testHero(hero);
		}
	}

	public static void testHero(Hero hero) {
		hero.HT = 80;
		hero.HP = 80;
		new ShieldOfWonders().identify().collect();
		new AnkhChain().collect();
        new PotionBandolier().collect();
        new SeedPouch().collect();
        new PlateArmor().identify().upgrade(10).collect();
        new AlchemistsToolkit().identify().collect();
		new Dart().quantity(14).collect();
		new Crossbow().identify().collect();
		// things we only want a few of..
		for (int i = 0; i < 4; i++) {
			new PotionOfMight().collect();
			new PotionOfHealing().collect();
			new ScrollOfRemoveCurse().collect();
		}
		for (int i = 0; i < 34; i++) {
			new PotionOfExperience().identify().collect();
			new ScrollOfMagicMapping().identify().collect();
		}
		// things we want a bunch of...
		for (int i = 0; i < 8; i++) {
			new Torch().identify().collect();
			new Food().collect();
			new ScrollOfIdentify().identify().collect();
			new ScrollOfUpgrade().collect();
		}
	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case COMPLAINS:
				return Badges.Badge.MASTERY_WARRIOR;
			case CHIEF:
				return Badges.Badge.MASTERY_MAGE;
			case FUMBLES:
				return Badges.Badge.MASTERY_ROGUE;
			case THACO:
				return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		hero.STR = hero.STR + 1;

		(hero.belongings.weapon = new ShortSword()).upgrade(1).identify();
		Dart darts = new Dart( 8 );
		darts.identify().collect();

		Dungeon.quickslot.setSlot(0, darts);

		//new PotionOfStrength().setKnown();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff = new MagesStaff(new WandOfMagicMissile());
		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollOfUpgrade().setKnown();
		new ScrollOfIdentify().setKnown();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify().upgrade(1);

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.misc1 = cloak).identify();
		hero.belongings.misc1.activate( hero );

		Dart darts = new Dart( 8 );
		darts.identify().collect();

		Dungeon.quickslot.setSlot(0, cloak);
		if (GoblinsPixelDungeon.quickSlots() > 1)
			Dungeon.quickslot.setSlot(1, darts);

		new ScrollOfMagicMapping().setKnown();
		new PotionOfToxicGas().identify().collect();
	}

	private static void initHuntress( Hero hero ) {

		hero.HP = (hero.HT -= 2);

		(hero.belongings.weapon = new Dagger()).identify();
		Boomerang boomerang = new Boomerang();
		boomerang.identify().collect();

		Dungeon.quickslot.setSlot(0, boomerang);

		new PotionOfMindVision().setKnown();
	}
	
	public String title() {
		return title;
	}
	
	public String spritesheet() {
		
		switch (this) {
		case COMPLAINS:
            if (Dungeon.hero.buff(Fury.class) != null) {
                return Assets.BERZERK;
            }
            else {
                return Assets.WARRIOR;
            }
		case CHIEF:
			return Assets.MAGE;
		case FUMBLES:
			return Assets.ROGUE;
		case THACO:
			return Assets.HUNTRESS;
		}
		
		return null;
	}
	
	public String[] perks() {
		
		switch (this) {
		case COMPLAINS:
			return WAR_PERKS;
		case CHIEF:
			return MAG_PERKS;
		case FUMBLES:
			return ROG_PERKS;
		case THACO:
			return HUN_PERKS;
		}
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : FUMBLES;
	}
}
