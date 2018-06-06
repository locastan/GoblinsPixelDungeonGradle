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
package com.shatteredpixel.pixeldungeonunleashed.levels.features;

import com.shatteredpixel.pixeldungeonunleashed.Challenges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Barkskin;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.HeroSubClass;
import com.shatteredpixel.pixeldungeonunleashed.effects.CellEmitter;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.LeafParticle;
import com.shatteredpixel.pixeldungeonunleashed.items.Dewdrop;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.Torch;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.SandalsOfNature;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.ScrollHolder;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.SeedPouch;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfHealing;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.plants.YumyuckMoss;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.watabou.utils.Random;

public class HighGrass {

	public static void trample( Level level, int pos, Char ch ) {
		//the lower the number, the more likely you will receive a dropped item
		int naturalDrop = 18;  // used for seed and dew drops
		int foodDrop = 80;     // used for food, id scrolls and healing potions
		int unusualDrop = 125; // used for scrolls, potions and rings

		switch (Dungeon.difficultyLevel) {
			case Dungeon.DIFF_TUTOR:
			case Dungeon.DIFF_EASY:
				naturalDrop = 16;
				foodDrop = 30;
				unusualDrop = 80;
				break;
			case Dungeon.DIFF_ENDLESS:
				naturalDrop = 20;
				foodDrop = 110;
				unusualDrop = 200;
				break;
			case Dungeon.DIFF_HARD:
				naturalDrop = 22;
				foodDrop = 130;
				unusualDrop = 800;
				break;
			case Dungeon.DIFF_NTMARE:
				naturalDrop = 28;
				foodDrop = 300;
				unusualDrop = 1200;
				break;
			case Dungeon.DIFF_NORM:
			case Dungeon.DIFF_TEST:
			default:
				naturalDrop = 18;
				foodDrop = 100;
				unusualDrop = 140;
				break;
		}
		Level.set( pos, Terrain.GRASS );
		GameScene.updateMap( pos );

		if (!Dungeon.isChallenged( Challenges.NO_HERBALISM )) {
			int naturalismLevel = 0;

			if (ch != null) {
				SandalsOfNature.Naturalism naturalism = ch.buff( SandalsOfNature.Naturalism.class );
				if (naturalism != null) {
					if (!naturalism.isCursed()) {
						naturalismLevel = naturalism.level() + 1;
						naturalism.charge();
					} else {
						naturalismLevel = -1;
					}
				}
			}

			if (naturalismLevel >= 0) {
				// Seed, scales from 1/16 to 1/4
				if (Random.Int(naturalDrop - ((int) (naturalismLevel * 3))) == 0) {
					Item seed = Generator.random(Generator.Category.SEED);

					if (seed instanceof YumyuckMoss.Seed) {
						if (Random.Int(15) - Dungeon.limitedDrops.blandfruitSeed.count >= 0) {
							level.drop(seed, pos).sprite.drop();
							Dungeon.limitedDrops.blandfruitSeed.count++;
						}
					} else
						level.drop(seed, pos).sprite.drop();
				}

				// Dew, scales from 1/6 to 1/3
				if (Random.Int((naturalDrop + 8) - naturalismLevel*3) <= 3) {
					level.drop(new Dewdrop(), pos).sprite.drop();
				}
			}

			if ((Dungeon.difficultyLevel == Dungeon.DIFF_EASY || Dungeon.difficultyLevel == Dungeon.DIFF_TUTOR)
					&& (Random.Int(60) == 0) && (!Dungeon.limitedDrops.seedBag.dropped())) {
				level.drop(new SeedPouch(), pos).sprite.drop();
				Dungeon.limitedDrops.seedBag.count++;
			}
		}

		if (!Dungeon.isChallenged( Challenges.NO_FOOD )) {
			if (Random.Int(foodDrop) == 0) {
				Item food = Generator.random(Generator.Category.FOOD);
				level.drop(food, pos).sprite.drop();
			}
		}

		if (!Dungeon.isChallenged( Challenges.NO_HEALING )) {
			if (Random.Int(foodDrop) == 0) {
				level.drop(new PotionOfHealing().identify(), pos).sprite.drop();
			}
		}

		if ((Dungeon.difficultyLevel <= Dungeon.DIFF_NORM) && Random.Int(25) == 0) {
			level.drop(new Dart(Random.Int(3)+2), pos).sprite.drop();
		}

		if ((Dungeon.difficultyLevel <= Dungeon.DIFF_NORM) && Random.Int(30) == 0) {
			level.drop(new Torch(), pos).sprite.drop();
		}

		if (!Dungeon.isChallenged( Challenges.NO_SCROLLS )) {
			if ((Dungeon.difficultyLevel == Dungeon.DIFF_EASY || Dungeon.difficultyLevel == Dungeon.DIFF_TUTOR)
					&& (Random.Int(60) == 0) && (!Dungeon.limitedDrops.scrollBag.dropped())) {
				level.drop(new ScrollHolder(), pos).sprite.drop();
				Dungeon.limitedDrops.scrollBag.count++;
			}

			if (Random.Int(foodDrop) == 0) {
				level.drop(new ScrollOfIdentify().identify(), pos).sprite.drop();
			}

			if (Random.Int(unusualDrop) == 0) {
				level.drop(Generator.random(Generator.Category.SCROLL), pos).sprite.drop();
			}
		}

		if (Random.Int(unusualDrop) == 0) {
			level.drop(Generator.random(Generator.Category.POTION), pos).sprite.drop();
		}

		if (Random.Int(unusualDrop + 25) == 0) {
			level.drop(Generator.random(Generator.Category.RING), pos).sprite.drop();
		}

		int leaves = 4;
		
		// Barkskin
		if (ch != null && ch instanceof Hero && ((Hero)ch).subClass == HeroSubClass.WARDEN) {
			Buff.affect( ch, Barkskin.class ).level( ch.HT / 3 );
			leaves = 8;
		}
		
		CellEmitter.get( pos ).burst( LeafParticle.LEVEL_SPECIFIC, leaves );
		Dungeon.observe();
	}
}
