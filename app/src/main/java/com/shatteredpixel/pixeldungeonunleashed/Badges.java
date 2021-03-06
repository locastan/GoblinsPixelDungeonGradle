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
package com.shatteredpixel.pixeldungeonunleashed;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.Artifact;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.DartBelt;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.PotionBandolier;
import com.watabou.noosa.Game;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Acidic;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Albino;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Bandit;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Senior;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Shielded;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.ScrollHolder;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.SeedPouch;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.WandHolster;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.Potion;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.Ring;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.Scroll;
import com.shatteredpixel.pixeldungeonunleashed.scenes.PixelScene;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class Badges {
	// to add a new badge you need to do the following:
	// 1. open up badges.png and create a new badge in an available slot.  If there are no available slots
	//    create a new row at the bottom and add it there.  count the badge entry number, top-left starts
	//    at 0, count left, then down.
	// 2. add a new entry to the ENUM table below, this entry should contain the text you want to appear
	//    and the icon number for your graphic.  The order of this table doesn't matter, feel free to
	//    rearange it as needed.
	// 3. create a function to validate receipt of your badge
	//       	public static void validateMyBadge() {
	//               // add any logic to verify receipt of the badge should happen
	//               if (!local.contains( Badge.MY_BADGE ) && validateDifficulty(true)) {
	//	                 Badge badge = Badge.MY_BADGE;
	//	                 local.add( badge );
	//	                 displayBadge( badge );
	//               }
    //          }
    // 4. find the point in your code where you want to award the badge and call this function.
	//
	// that should be it... at this point your badge should be fully enabled.


public enum Badge {
		// the order of these badges don't matter in this list
		// the format is:  BADGE_ALIAS( "badge description", graphic-in-badges.png, optional meta tag)
		// entries without arguments are not displayed in the badge list
		// graphics are counted similar to other sprites, top-left image is 0, count left then down
		// meta tag determines what type of messages are reported to the player
		MONSTERS_SLAIN_1( "10 enemies slain", 0 ),  // previously badges were 0 through 3 - I didn't like the graphics
		MONSTERS_SLAIN_2( "50 enemies slain", 0 ),  // if we are going to have seperate graphics for each level of the
		MONSTERS_SLAIN_3( "150 enemies slain", 0 ), // badge I will want something other than the "notched" badges
		MONSTERS_SLAIN_4( "250 enemies slain", 0 ),
		MONSTERS_SLAIN_5( "500 enemies slain", 0 ),
		GOLD_COLLECTED_1( "100 gold collected", 4 ),
		GOLD_COLLECTED_2( "500 gold collected", 4 ),
		GOLD_COLLECTED_3( "2500 gold collected", 4 ),
		GOLD_COLLECTED_4( "7500 gold collected", 4 ),
		GOLD_COLLECTED_5( "15000 gold collected", 4 ),
		LEVEL_REACHED_1( "Level 6 reached", 8 ),
		LEVEL_REACHED_2( "Level 12 reached", 8 ),
		LEVEL_REACHED_3( "Level 20 reached", 8 ),
		LEVEL_REACHED_4( "Level 30 reached", 8 ),
		LEVEL_REACHED_5( "Level 50 reached", 8 ),
		STRENGTH_ATTAINED_1( "13 points of Strength attained", 40 ),
		STRENGTH_ATTAINED_2( "15 points of Strength attained", 40 ),
		STRENGTH_ATTAINED_3( "17 points of Strength attained", 40 ),
		STRENGTH_ATTAINED_4( "19 points of Strength attained", 40 ),
		FOOD_EATEN_1( "10 pieces of food eaten", 44 ),
		FOOD_EATEN_2( "25 pieces of food eaten", 44 ),
		FOOD_EATEN_3( "50 pieces of food eaten", 44 ),
		FOOD_EATEN_4( "100 pieces of food eaten", 44 ),
		ITEM_LEVEL_1( "Item of level 3 acquired", 48 ),
		ITEM_LEVEL_2( "Item of level 6 acquired", 48 ),
		ITEM_LEVEL_3( "Item of level 8 acquired", 48 ),
		ITEM_LEVEL_4( "Item of level 10 acquired", 48 ),
		POTIONS_COOKED_1( "3 potions cooked", 52 ),
		POTIONS_COOKED_2( "6 potions cooked", 52 ),
		POTIONS_COOKED_3( "10 potions cooked", 52 ),
		POTIONS_COOKED_4( "15 potions cooked", 52 ),
		POTIONS_COOKED_5( "20 potions cooked", 52 ),
		NO_MONSTERS_SLAIN( "Level completed without killing any monsters", 28 ),
		GAMES_PLAYED_1( "10 games played", 60, true ),
		GAMES_PLAYED_2( "100 games played", 60, true ),
		GAMES_PLAYED_3( "500 games played", 60, true ),
		GAMES_PLAYED_4( "2000 games played", 60, true ),

		ALL_POTIONS_IDENTIFIED( "All potions identified", 16 ),
		ALL_SCROLLS_IDENTIFIED( "All scrolls identified", 17 ),
		ALL_RINGS_IDENTIFIED( "All rings identified", 18 ),
		ALL_WANDS_IDENTIFIED( "All wands identified", 19 ),
		ALL_ITEMS_IDENTIFIED( "All potions, scrolls, rings & wands identified", 35, true ),
		BAG_BOUGHT_SEED_POUCH,
		BAG_BOUGHT_SCROLL_HOLDER,
		BAG_BOUGHT_POTION_BANDOLIER,
		BAG_BOUGHT_WAND_HOLSTER,
        BAG_BOUGHT_DART_BELT,
		ALL_BAGS_BOUGHT( "All bags bought", 23 ),
		DEATH_FROM_FIRE( "Death from fire", 24 ),
		DEATH_FROM_POISON( "Death from poison", 25 ),
		DEATH_FROM_GAS( "Death from toxic gas", 26 ),
		DEATH_FROM_HUNGER( "Death from hunger", 27 ),
		DEATH_FROM_GLYPH( "Death from a glyph", 57 ),
		DEATH_FROM_FALLING( "Death from falling down", 59 ),
		DEATH_FROM_SOW( "Death by shield of wonder", 2 ),
	    DEATH_FROM_INF( "Eaten alive!", 3 ),
		YASD( "Death from fire, poison, toxic gas & hunger", 34, true ),

		BOSS_SLAIN_1( "1st boss slain", 12 ),
		BOSS_SLAIN_2( "2nd boss slain", 12 ),
		BOSS_SLAIN_3( "3rd boss slain", 12 ),
		BOSS_SLAIN_4( "4th boss slain", 12 ),
	    BOSS_SLAIN_5( "5th boss slain", 12 ),
		BOSS_SLAIN_1_WARRIOR,
		BOSS_SLAIN_1_MAGE,
		BOSS_SLAIN_1_ROGUE,
		BOSS_SLAIN_1_HUNTRESS,
		BOSS_SLAIN_1_ALL_CLASSES( "1st boss slain by Complains, Chief, Fumbles & Thaco", 32, true ),
		BOSS_SLAIN_3_GLADIATOR,
		BOSS_SLAIN_3_BERSERKER,
		BOSS_SLAIN_3_WARLOCK,
		BOSS_SLAIN_3_BATTLEMAGE,
		BOSS_SLAIN_3_FREERUNNER,
		BOSS_SLAIN_3_ASSASSIN,
		BOSS_SLAIN_3_SNIPER,
		BOSS_SLAIN_3_WARDEN,
		BOSS_SLAIN_3_ALL_SUBCLASSES(
			"3rd boss slain by Gladiator, Berserker, Exorcist, War Cleric, " +
			"Freerunner, Assassin, Sniper & Warden", 33, true ),
		RING_OF_HAGGLER( "Ring of Haggler obtained", 20 ),
		RING_OF_THORNS( "Ring of Thorns obtained", 21 ),
		MASTERY_WARRIOR,
		MASTERY_MAGE,
		MASTERY_ROGUE,
		MASTERY_HUNTRESS,
		RARE_ALBINO,
		RARE_BANDIT,
		RARE_SHIELDED,
		RARE_SENIOR,
		RARE_ACIDIC,
		RARE( "All rare monsters slain", 37, true ),
		VICTORY_WARRIOR,
		VICTORY_MAGE,
		VICTORY_ROGUE,
		VICTORY_HUNTRESS,
		VICTORY( "Amulet of Yendor obtained", 22 ),
		VICTORY_ALL_CLASSES( "Amulet of Yendor obtained by Complains, Chief, Fumbles & Thaco", 36, true ),
		MASTERY_COMBO( "7-hit combo", 56 ),
		GRIM_WEAPON( "Monster killed by a Grim weapon", 29 ),
		PIRANHAS( "6 piranhas killed", 30 ),
		NIGHT_HUNTER( "15 monsters killed at nighttime", 58 ),
		HAPPY_END( "Happy end", 38 ),
		CHAMPION( "Challenge won", 39, true ),
		SUPPORTER( "Thanks for your support!", 51, true ),
		BETA_TESTER( "Beta Tester", 1 ); // starting from the top-left with 0, the next badge is 1
		
		public boolean meta;
		
		public String description;
		public int image;
		
		Badge( String description, int image ) {
			this( description, image, false );
		}
		
		Badge( String description, int image, boolean meta ) {
			this.description = description;
			this.image = image;
			this.meta = meta;
		}
		
		Badge() {
			this( "", -1 );
		}
	}
	
	private static HashSet<Badge> global;
	private static HashSet<Badge> local = new HashSet<Badges.Badge>();
	
	private static boolean saveNeeded = false;

	public static Callback loadingListener = null;

	public static void reset() {
		local.clear();
		loadGlobal();
	}
	
	private static final String BADGES_FILE	= "badges.dat";
	private static final String BADGES		= "badges";
	
	private static HashSet<Badge> restore( Bundle bundle ) {
		HashSet<Badge> badges = new HashSet<>();
		
		String[] names = bundle.getStringArray( BADGES );
		for (int i=0; i < names.length; i++) {
			try {
				badges.add( Badge.valueOf( names[i] ) );
			} catch (Exception e) {
			}
		}
	
		return badges;
	}
	
	private static void store( Bundle bundle, HashSet<Badge> badges ) {
		int count = 0;
		String names[] = new String[badges.size()];
		
		for (Badge badge:badges) {
			names[count++] = badge.toString();
		}
		bundle.put( BADGES, names );
	}
	
	public static void loadLocal( Bundle bundle ) {
		local = restore( bundle );
	}
	
	public static void saveLocal( Bundle bundle ) {
		store( bundle, local );
	}
	
	public static void loadGlobal() {
		if (global == null) {
			try {
				InputStream input = Game.instance.openFileInput( BADGES_FILE );
				Bundle bundle = Bundle.read( input );
				input.close();
				
				global = restore( bundle );
				
			} catch (Exception e) {
				global = new HashSet<>();
			}
		}
		// the following applies the badge as soon as we load our global data
		global.add(Badge.BETA_TESTER); // DSM-xxxx remove this after BETA testing is complete...
	}
	
	public static void saveGlobal() {
		if (saveNeeded) {
			
			Bundle bundle = new Bundle();
			store( bundle, global );
			
			try {
				OutputStream output = Game.instance.openFileOutput( BADGES_FILE, Game.MODE_PRIVATE );
				Bundle.write( bundle, output );
				output.close();
				saveNeeded = false;
			} catch (IOException e) {
				
			}
		}
	}

	public static boolean validateDifficulty( boolean alertPlayer ) {
		if (Dungeon.difficultyLevel == Dungeon.DIFF_TUTOR) {
			if (alertPlayer) {
				GLog.i("Badges not earned in Tutorial Mode");
			}
			return false;
		} else if (Dungeon.difficultyLevel == Dungeon.DIFF_EASY) {
			if (alertPlayer) {
				GLog.i("Badges not earned in Easy Mode");
			}
			return false;
		} else {
			return true;
		}
	}
	public static void validateMonstersSlain() {
		Badge badge = null;
		
		if (!local.contains( Badge.MONSTERS_SLAIN_1 ) && Statistics.enemiesSlain >= 10) {
			badge = Badge.MONSTERS_SLAIN_1;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_2 ) && Statistics.enemiesSlain >= 50) {
			badge = Badge.MONSTERS_SLAIN_2;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_3 ) && Statistics.enemiesSlain >= 150) {
			badge = Badge.MONSTERS_SLAIN_3;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_4 ) && Statistics.enemiesSlain >= 250) {
			badge = Badge.MONSTERS_SLAIN_4;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_5 ) && Statistics.enemiesSlain >= 500) {
			badge = Badge.MONSTERS_SLAIN_5;
			local.add( badge );
		}

		displayBadge( badge );
	}
	
	public static void validateGoldCollected() {
		if (!validateDifficulty(false)) return;

		Badge badge = null;
		
		if (!local.contains( Badge.GOLD_COLLECTED_1 ) && Statistics.goldCollected >= 100) {
			badge = Badge.GOLD_COLLECTED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_2 ) && Statistics.goldCollected >= 500) {
			badge = Badge.GOLD_COLLECTED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_3 ) && Statistics.goldCollected >= 2500) {
			badge = Badge.GOLD_COLLECTED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_4 ) && Statistics.goldCollected >= 7500) {
			badge = Badge.GOLD_COLLECTED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_5 ) && Statistics.goldCollected >= 15000) {
			badge = Badge.GOLD_COLLECTED_5;
			local.add( badge );
		}

		displayBadge( badge );
	}
	
	public static void validateLevelReached() {
		if (!validateDifficulty(false)) return;

		Badge badge = null;
		
		if (!local.contains( Badge.LEVEL_REACHED_1 ) && Dungeon.hero.lvl >= 6) {
			badge = Badge.LEVEL_REACHED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_2 ) && Dungeon.hero.lvl >= 12) {
			badge = Badge.LEVEL_REACHED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_3 ) && Dungeon.hero.lvl >= 20) {
			badge = Badge.LEVEL_REACHED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_4 ) && Dungeon.hero.lvl >= 30) {
			badge = Badge.LEVEL_REACHED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_5 ) && Dungeon.hero.lvl >= 50) {
			badge = Badge.LEVEL_REACHED_5;
			local.add( badge );
		}

		displayBadge( badge );
	}
	
	public static void validateStrengthAttained() {
		if (!validateDifficulty(false)) return;

		Badge badge = null;
		
		if (!local.contains( Badge.STRENGTH_ATTAINED_1 ) && Dungeon.hero.STR >= 13) {
			badge = Badge.STRENGTH_ATTAINED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_2 ) && Dungeon.hero.STR >= 15) {
			badge = Badge.STRENGTH_ATTAINED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_3 ) && Dungeon.hero.STR >= 17) {
			badge = Badge.STRENGTH_ATTAINED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_4 ) && Dungeon.hero.STR >= 19) {
			badge = Badge.STRENGTH_ATTAINED_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateFoodEaten() {
		if (!validateDifficulty(false)) return;

		Badge badge = null;
		
		if (!local.contains( Badge.FOOD_EATEN_1 ) && Statistics.foodEaten >= 10) {
			badge = Badge.FOOD_EATEN_1;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_2 ) && Statistics.foodEaten >= 25) {
			badge = Badge.FOOD_EATEN_2;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_3 ) && Statistics.foodEaten >= 50) {
			badge = Badge.FOOD_EATEN_3;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_4 ) && Statistics.foodEaten >= 100) {
			badge = Badge.FOOD_EATEN_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validatePotionsCooked() {
		if (!validateDifficulty(false)) return;

		Badge badge = null;
		
		if (!local.contains( Badge.POTIONS_COOKED_1 ) && Statistics.potionsCooked >= 3) {
			badge = Badge.POTIONS_COOKED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_2 ) && Statistics.potionsCooked >= 6) {
			badge = Badge.POTIONS_COOKED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_3 ) && Statistics.potionsCooked >= 10) {
			badge = Badge.POTIONS_COOKED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_4 ) && Statistics.potionsCooked >= 15) {
			badge = Badge.POTIONS_COOKED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_5 ) && Statistics.potionsCooked >= 20) {
			badge = Badge.POTIONS_COOKED_5;
			local.add( badge );
		}

		displayBadge( badge );
	}
	
	public static void validatePiranhasKilled() {
		if (!validateDifficulty(false)) return;

		Badge badge = null;
		
		if (!local.contains( Badge.PIRANHAS ) && Statistics.piranhasKilled >= 6) {
			badge = Badge.PIRANHAS;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateItemLevelAquired( Item item ) {
		if (!validateDifficulty(false)) return;

		// This method should be called:
		// 1) When an item is obtained (Item.collect)
		// 2) When an item is upgraded (ScrollOfUpgrade, ScrollOfWeaponUpgrade, ShortSword, WandOfMagicMissile)
		// 3) When an item is identified

		// Note that artifacts should never trigger this badge as they are alternatively upgraded
		if (!item.levelKnown || item instanceof Artifact) {
			return;
		}
		
		Badge badge = null;
		if (!local.contains( Badge.ITEM_LEVEL_1 ) && item.level >= 3) {
			badge = Badge.ITEM_LEVEL_1;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_2 ) && item.level >= 6) {
			badge = Badge.ITEM_LEVEL_2;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_3 ) && item.level >= 8) {
			badge = Badge.ITEM_LEVEL_3;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_4 ) && item.level >= 10) {
			badge = Badge.ITEM_LEVEL_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateAllPotionsIdentified() {
		if (Dungeon.hero != null && Dungeon.hero.isAlive() &&
			!local.contains( Badge.ALL_POTIONS_IDENTIFIED ) && Potion.allKnown() &&
				validateDifficulty(true)) {
			
			Badge badge = Badge.ALL_POTIONS_IDENTIFIED;
			local.add( badge );
			displayBadge( badge );
			
			validateAllItemsIdentified();
		}
	}
	
	public static void validateAllScrollsIdentified() {
		if (Dungeon.hero != null && Dungeon.hero.isAlive() &&
			!local.contains( Badge.ALL_SCROLLS_IDENTIFIED ) && Scroll.allKnown() &&
				validateDifficulty(true)) {
			
			Badge badge = Badge.ALL_SCROLLS_IDENTIFIED;
			local.add( badge );
			displayBadge( badge );
			
			validateAllItemsIdentified();
		}
	}
	
	public static void validateAllRingsIdentified() {
		if (Dungeon.hero != null && Dungeon.hero.isAlive() &&
			!local.contains( Badge.ALL_RINGS_IDENTIFIED ) && Ring.allKnown() &&
				validateDifficulty(true)) {
			
			Badge badge = Badge.ALL_RINGS_IDENTIFIED;
			local.add( badge );
			displayBadge( badge );
			
			validateAllItemsIdentified();
		}
	}

	public static void validateAllBagsBought( Item bag ) {
		if (!validateDifficulty(true)) return;

		Badge badge = null;
		if (bag instanceof SeedPouch) {
			badge = Badge.BAG_BOUGHT_SEED_POUCH;
		} else if (bag instanceof ScrollHolder) {
			badge = Badge.BAG_BOUGHT_SCROLL_HOLDER;
		} else if (bag instanceof PotionBandolier) {
			badge = Badge.BAG_BOUGHT_POTION_BANDOLIER;
		} else if (bag instanceof WandHolster) {
			badge = Badge.BAG_BOUGHT_WAND_HOLSTER;
		} else if (bag instanceof DartBelt) {
            badge = Badge.BAG_BOUGHT_DART_BELT;
        }
		
		if (badge != null) {
			
			local.add( badge );
			
			if (!local.contains( Badge.ALL_BAGS_BOUGHT ) &&
				local.contains( Badge.BAG_BOUGHT_SEED_POUCH ) &&
				local.contains( Badge.BAG_BOUGHT_SCROLL_HOLDER ) &&
				local.contains( Badge.BAG_BOUGHT_POTION_BANDOLIER ) &&
                local.contains( Badge.BAG_BOUGHT_DART_BELT ) &&
				local.contains( Badge.BAG_BOUGHT_WAND_HOLSTER )) {
						
					badge = Badge.ALL_BAGS_BOUGHT;
					local.add( badge );
					displayBadge( badge );
			}
		}
	}
	
	public static void validateAllItemsIdentified() {
		if (!validateDifficulty(false)) return;

		if (!global.contains( Badge.ALL_ITEMS_IDENTIFIED ) &&
			global.contains( Badge.ALL_POTIONS_IDENTIFIED ) &&
			global.contains( Badge.ALL_SCROLLS_IDENTIFIED ) &&
			global.contains( Badge.ALL_RINGS_IDENTIFIED )) {
			Badge badge = Badge.ALL_ITEMS_IDENTIFIED;
			displayBadge( badge );
		}
	}
	
	public static void validateDeathFromFire() {
		Badge badge = Badge.DEATH_FROM_FIRE;
		local.add( badge );
		displayBadge( badge );
		
		validateYASD();
	}
	
	public static void validateDeathFromPoison() {
		Badge badge = Badge.DEATH_FROM_POISON;
		local.add( badge );
		displayBadge( badge );
		
		validateYASD();
	}
	
	public static void validateDeathFromGas() {
		Badge badge = Badge.DEATH_FROM_GAS;
		local.add( badge );
		displayBadge( badge );
		
		validateYASD();
	}
	
	public static void validateDeathFromHunger() {
		Badge badge = Badge.DEATH_FROM_HUNGER;
		local.add( badge );
		displayBadge( badge );
		
		validateYASD();
	}
	
	public static void validateDeathFromGlyph() {
		Badge badge = Badge.DEATH_FROM_GLYPH;
		local.add( badge );
		displayBadge( badge );
	}

    public static void validateDeathBySoW() {
        Badge badge = Badge.DEATH_FROM_SOW;
        local.add( badge );
        displayBadge( badge );
    }

    public static void validateDeathByInfestation() {
        Badge badge = Badge.DEATH_FROM_INF;
        local.add( badge );
        displayBadge( badge );
    }
	
	public static void validateDeathFromFalling() {
		Badge badge = Badge.DEATH_FROM_FALLING;
		local.add( badge );
		displayBadge( badge );
	}
	
	private static void validateYASD() {
		if (global.contains( Badge.DEATH_FROM_FIRE ) &&
			global.contains( Badge.DEATH_FROM_POISON ) &&
			global.contains( Badge.DEATH_FROM_GAS ) &&
			global.contains( Badge.DEATH_FROM_HUNGER)) {
			
			Badge badge = Badge.YASD;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateBossSlain() {
		if (!validateDifficulty(true)) return;

		Badge badge = null;
		switch (Dungeon.depth) {
		case 6:
			badge = Badge.BOSS_SLAIN_1;
			break;
		case 12:
			badge = Badge.BOSS_SLAIN_2;
			break;
		case 18:
			badge = Badge.BOSS_SLAIN_3;
			break;
		case 24:
			badge = Badge.BOSS_SLAIN_4;
			break;
		case 30:
			badge = Badge.BOSS_SLAIN_5;
			break;
		}
		
		if (badge != null) {
			local.add( badge );
			displayBadge( badge );
			
			if (badge == Badge.BOSS_SLAIN_1) {
				switch (Dungeon.hero.heroClass) {
				case COMPLAINS:
					badge = Badge.BOSS_SLAIN_1_WARRIOR;
					break;
				case CHIEF:
					badge = Badge.BOSS_SLAIN_1_MAGE;
					break;
				case FUMBLES:
					badge = Badge.BOSS_SLAIN_1_ROGUE;
					break;
				case THACO:
					badge = Badge.BOSS_SLAIN_1_HUNTRESS;
					break;
				}
				local.add( badge );
				if (!global.contains( badge )) {
					global.add( badge );
					saveNeeded = true;
				}
				
				if (global.contains( Badge.BOSS_SLAIN_1_WARRIOR ) &&
					global.contains( Badge.BOSS_SLAIN_1_MAGE ) &&
					global.contains( Badge.BOSS_SLAIN_1_ROGUE ) &&
					global.contains( Badge.BOSS_SLAIN_1_HUNTRESS)) {
					
					badge = Badge.BOSS_SLAIN_1_ALL_CLASSES;
					if (!global.contains( badge )) {
						displayBadge( badge );
						global.add( badge );
						saveNeeded = true;
					}
				}
			} else
			if (badge == Badge.BOSS_SLAIN_3) {
				switch (Dungeon.hero.subClass) {
				case GLADIATOR:
					badge = Badge.BOSS_SLAIN_3_GLADIATOR;
					break;
				case BERSERKER:
					badge = Badge.BOSS_SLAIN_3_BERSERKER;
					break;
				case WARLOCK:
					badge = Badge.BOSS_SLAIN_3_WARLOCK;
					break;
				case BATTLEMAGE:
					badge = Badge.BOSS_SLAIN_3_BATTLEMAGE;
					break;
				case FREERUNNER:
					badge = Badge.BOSS_SLAIN_3_FREERUNNER;
					break;
				case ASSASSIN:
					badge = Badge.BOSS_SLAIN_3_ASSASSIN;
					break;
				case SNIPER:
					badge = Badge.BOSS_SLAIN_3_SNIPER;
					break;
				case WARDEN:
					badge = Badge.BOSS_SLAIN_3_WARDEN;
					break;
				default:
					return;
				}
				local.add( badge );
				if (!global.contains( badge )) {
					global.add( badge );
					saveNeeded = true;
				}
				
				if (global.contains( Badge.BOSS_SLAIN_3_GLADIATOR ) &&
					global.contains( Badge.BOSS_SLAIN_3_BERSERKER ) &&
					global.contains( Badge.BOSS_SLAIN_3_WARLOCK ) &&
					global.contains( Badge.BOSS_SLAIN_3_BATTLEMAGE ) &&
					global.contains( Badge.BOSS_SLAIN_3_FREERUNNER ) &&
					global.contains( Badge.BOSS_SLAIN_3_ASSASSIN ) &&
					global.contains( Badge.BOSS_SLAIN_3_SNIPER ) &&
					global.contains( Badge.BOSS_SLAIN_3_WARDEN )) {
					
					badge = Badge.BOSS_SLAIN_3_ALL_SUBCLASSES;
					if (!global.contains( badge )) {
						displayBadge( badge );
						global.add( badge );
						saveNeeded = true;
					}
				}
			}
		}
	}
	
	public static void validateMastery() {
		if (!validateDifficulty(true)) return;

		Badge badge = null;
		switch (Dungeon.hero.heroClass) {
		case COMPLAINS:
			badge = Badge.MASTERY_WARRIOR;
			break;
		case CHIEF:
			badge = Badge.MASTERY_MAGE;
			break;
		case FUMBLES:
			badge = Badge.MASTERY_ROGUE;
			break;
		case THACO:
			badge = Badge.MASTERY_HUNTRESS;
			break;
		}
		
		if (!global.contains( badge )) {
			global.add( badge );
			saveNeeded = true;
		}
	}
	
	public static void validateMasteryCombo( int n ) {
		if (!validateDifficulty(false)) return;

		if (!local.contains( Badge.MASTERY_COMBO ) && n == 7) {
			Badge badge = Badge.MASTERY_COMBO;
			local.add( badge );
			displayBadge( badge );
		}
	}

	public static void validateRare( Mob mob ) {
		if (!validateDifficulty(false)) return;

		Badge badge = null;
		if (mob instanceof Albino) {
			badge = Badge.RARE_ALBINO;
		} else if (mob instanceof Bandit) {
			badge = Badge.RARE_BANDIT;
		} else if (mob instanceof Shielded) {
			badge = Badge.RARE_SHIELDED;
		} else if (mob instanceof Senior) {
			badge = Badge.RARE_SENIOR;
		} else if (mob instanceof Acidic) {
			badge = Badge.RARE_ACIDIC;
		}
		if (!global.contains( badge )) {
			global.add( badge );
			saveNeeded = true;
		}
		
		if (global.contains( Badge.RARE_ALBINO ) &&
			global.contains( Badge.RARE_BANDIT ) &&
			global.contains( Badge.RARE_SHIELDED ) &&
			global.contains( Badge.RARE_SENIOR ) &&
			global.contains( Badge.RARE_ACIDIC )) {
			
			badge = Badge.RARE;
			displayBadge( badge );
		}
	}
	
	public static void validateVictory() {
		if (!validateDifficulty(true)) return;

		Badge badge = Badge.VICTORY;
		displayBadge( badge );

		switch (Dungeon.hero.heroClass) {
		case COMPLAINS:
			badge = Badge.VICTORY_WARRIOR;
			break;
		case CHIEF:
			badge = Badge.VICTORY_MAGE;
			break;
		case FUMBLES:
			badge = Badge.VICTORY_ROGUE;
			break;
		case THACO:
			badge = Badge.VICTORY_HUNTRESS;
			break;
		}
		local.add( badge );
		if (!global.contains( badge )) {
			global.add( badge );
			saveNeeded = true;
		}
		
		if (global.contains( Badge.VICTORY_WARRIOR ) &&
			global.contains( Badge.VICTORY_MAGE ) &&
			global.contains( Badge.VICTORY_ROGUE ) &&
			global.contains( Badge.VICTORY_HUNTRESS )) {
			
			badge = Badge.VICTORY_ALL_CLASSES;
			displayBadge( badge );
		}
	}
	
	public static void validateNoKilling() {
		if (!local.contains( Badge.NO_MONSTERS_SLAIN ) && Statistics.completedWithNoKilling) {
			Badge badge = Badge.NO_MONSTERS_SLAIN;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateGrimWeapon() {
		if (!local.contains( Badge.GRIM_WEAPON )) {
			Badge badge = Badge.GRIM_WEAPON;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateNightHunter() {
		if (!validateDifficulty(false)) return;

		if (!local.contains( Badge.NIGHT_HUNTER ) && Statistics.nightHunt >= 15) {
			Badge badge = Badge.NIGHT_HUNTER;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateSupporter() {
		global.add( Badge.SUPPORTER );
		saveNeeded = true;
		
		PixelScene.showBadge( Badge.SUPPORTER );
	}
	
	public static void validateGamesPlayed() {
		Badge badge = null;
		if (Rankings.INSTANCE.totalNumber >= 10) {
			badge = Badge.GAMES_PLAYED_1;
		}
		if (Rankings.INSTANCE.totalNumber >= 100) {
			badge = Badge.GAMES_PLAYED_2;
		}
		if (Rankings.INSTANCE.totalNumber >= 500) {
			badge = Badge.GAMES_PLAYED_3;
		}
		if (Rankings.INSTANCE.totalNumber >= 2000) {
			badge = Badge.GAMES_PLAYED_4;
		}
		
		displayBadge( badge );
	}
	
	public static void validateHappyEnd() {
		if (!validateDifficulty(true)) return;

		displayBadge( Badge.HAPPY_END );
	}

	public static void validateChampion() {
		if (!validateDifficulty(true)) return;

		displayBadge(Badge.CHAMPION);
	}
	
	private static void displayBadge( Badge badge ) {
		
		if (badge == null) {
			return;
		}
		
		if (global.contains( badge )) {
			
			if (!badge.meta) {
				GLog.h( "Badge endorsed: %s", badge.description );
			}
			
		} else {
			
			global.add( badge );
			saveNeeded = true;
			
			if (badge.meta) {
				GLog.h( "New super badge: %s", badge.description );
			} else {
				GLog.h( "New badge: %s", badge.description );
			}
			PixelScene.showBadge( badge );
		}
	}
	
	public static boolean isUnlocked( Badge badge ) {
		return global.contains( badge );
	}
	
	public static void disown( Badge badge ) {
		loadGlobal();
		global.remove( badge );
		saveNeeded = true;
	}
	
	public static List<Badge> filtered( boolean global ) {
		
		HashSet<Badge> filtered = new HashSet<>( global ? Badges.global : Badges.local );
		
		if (!global) {
			Iterator<Badge> iterator = filtered.iterator();
			while (iterator.hasNext()) {
				Badge badge = iterator.next();
				if (badge.meta) {
					iterator.remove();
				}
			}
		}
		
		leaveBest( filtered, Badge.MONSTERS_SLAIN_1, Badge.MONSTERS_SLAIN_2, Badge.MONSTERS_SLAIN_3, Badge.MONSTERS_SLAIN_4, Badge.MONSTERS_SLAIN_5 );
		leaveBest( filtered, Badge.GOLD_COLLECTED_1, Badge.GOLD_COLLECTED_2, Badge.GOLD_COLLECTED_3, Badge.GOLD_COLLECTED_4, Badge.GOLD_COLLECTED_5 );
		leaveBest( filtered, Badge.BOSS_SLAIN_1, Badge.BOSS_SLAIN_2, Badge.BOSS_SLAIN_3, Badge.BOSS_SLAIN_4, Badge.BOSS_SLAIN_5 );
		leaveBest( filtered, Badge.LEVEL_REACHED_1, Badge.LEVEL_REACHED_2, Badge.LEVEL_REACHED_3, Badge.LEVEL_REACHED_4, Badge.LEVEL_REACHED_5 );
		leaveBest( filtered, Badge.STRENGTH_ATTAINED_1, Badge.STRENGTH_ATTAINED_2, Badge.STRENGTH_ATTAINED_3, Badge.STRENGTH_ATTAINED_4 );
		leaveBest( filtered, Badge.FOOD_EATEN_1, Badge.FOOD_EATEN_2, Badge.FOOD_EATEN_3, Badge.FOOD_EATEN_4 );
		leaveBest( filtered, Badge.ITEM_LEVEL_1, Badge.ITEM_LEVEL_2, Badge.ITEM_LEVEL_3, Badge.ITEM_LEVEL_4 );
		leaveBest( filtered, Badge.POTIONS_COOKED_1, Badge.POTIONS_COOKED_2, Badge.POTIONS_COOKED_3, Badge.POTIONS_COOKED_4, Badge.POTIONS_COOKED_5 );
		leaveBest( filtered, Badge.BOSS_SLAIN_1_ALL_CLASSES, Badge.BOSS_SLAIN_3_ALL_SUBCLASSES );
		leaveBest( filtered, Badge.DEATH_FROM_FIRE, Badge.YASD );
		leaveBest( filtered, Badge.DEATH_FROM_GAS, Badge.YASD );
		leaveBest( filtered, Badge.DEATH_FROM_HUNGER, Badge.YASD );
		leaveBest( filtered, Badge.DEATH_FROM_POISON, Badge.YASD );
		leaveBest( filtered, Badge.VICTORY, Badge.VICTORY_ALL_CLASSES );
		leaveBest( filtered, Badge.GAMES_PLAYED_1, Badge.GAMES_PLAYED_2, Badge.GAMES_PLAYED_3, Badge.GAMES_PLAYED_4 );
		
		ArrayList<Badge> list = new ArrayList<Badge>( filtered );
		Collections.sort( list );
		
		return list;
	}
	
	private static void leaveBest( HashSet<Badge> list, Badge...badges ) {
		for (int i=badges.length-1; i > 0; i--) {
			if (list.contains( badges[i])) {
				for (int j=0; j < i; j++) {
					list.remove( badges[j] );
				}
				break;
			}
		}
	}
}
