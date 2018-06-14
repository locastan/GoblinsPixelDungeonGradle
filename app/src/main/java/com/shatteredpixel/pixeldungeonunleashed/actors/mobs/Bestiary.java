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
package com.shatteredpixel.pixeldungeonunleashed.actors.mobs;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.watabou.utils.Random;

public class Bestiary {

	public static Mob mob( int depth ) {
		@SuppressWarnings("unchecked")
		Class<? extends Mob> cl = (Class<? extends Mob>)mobClass( depth );
		try {
			return cl.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Mob mutable( int depth ) {
		@SuppressWarnings("unchecked")
		Class<? extends Mob> cl = (Class<? extends Mob>)mobClass( depth );
		
		if (Random.Int( 30 ) == 0) {
			if (cl == Rat.class) {
				cl = Albino.class;
			} else if (cl == Thief.class) {
				cl = Bandit.class;
			} else if (cl == Brute.class) {
				cl = Shielded.class;
			} else if (cl == Monk.class) {
				cl = Senior.class;
			} else if (cl == Scorpio.class) {
				cl = Acidic.class;
			}
		}
		
		try {
			return cl.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
	private static Class<?> mobClass( int depth ) {

        float[] chances;
        Class<?>[] classes;

        if (Dungeon.difficultyLevel != Dungeon.DIFF_ENDLESS) {

            switch (depth) {
                case 1:
                    chances = new float[]{3, 1};
                    classes = new Class<?>[]{Rat.class, GreenSnake.class};
                    break;
                case 2:
                    chances = new float[]{1, 1, 1, 0.05f};
                    classes = new Class<?>[]{Rat.class, Gnoll.class, GreenSnake.class, RedSnake.class};
                    break;
                case 3:
                    chances = new float[]{1, 2, 1, 0.5f, 0.5f, 0.05f, 0.2f};
                    classes = new Class<?>[]{Rat.class, Gnoll.class, GreenSnake.class, Crab.class, Velocirooster.class, SewerFly.class, RedSnake.class};
                    break;
                case 4:
                    chances = new float[]{1, 2, 1, 0.5f, 1, 0.05f, 1, 0.5f};
                    classes = new Class<?>[]{Rat.class, Gnoll.class, Crab.class, Velocirooster.class, SewerFly.class, Slime.class, SlimeBrown.class, RedSnake.class};
                    break;
                case 5:
                    chances = new float[]{2, 1, 2, 1, 5, 2, 0.5f, 1, 0.01f};
                    classes = new Class<?>[]{Slime.class, SlimeRed.class, SlimeBrown.class, Rat.class, Gnoll.class, Crab.class, Velocirooster.class, SewerFly.class, Skeleton.class};
                    break;

                case 6:
                    chances = new float[]{1};
                    classes = new Class<?>[]{Goo.class};
                    break;

                case 7:
                    chances = new float[]{2, 3, 1, 1, 0.2f, 0.5f, 0.5f};
                    classes = new Class<?>[]{Skeleton.class, Zombie.class, Thief.class, Swarm.class, Shaman.class, SewerFly.class, BodFly.class};
                    break;
                case 8:
                    chances = new float[]{2, 1, 1, 1, 1, 0.5f};
                    classes = new Class<?>[]{Skeleton.class, Zombie.class, Shaman.class, Thief.class, Swarm.class, BodFly.class};
                    break;
                case 9:
                    chances = new float[]{2, 1, 2, 1, 1, 1, 0.02f, 0.5f};
                    classes = new Class<?>[]{Skeleton.class, Zombie.class, Shaman.class, Gnoll.class, Thief.class, Swarm.class, Bat.class, BodFly.class};
                    break;
                case 10:
                    chances = new float[]{3, 3, 1, 1, 0.02f, 0.01f, 0.1f, 0.5f};
                    classes = new Class<?>[]{Skeleton.class, Shaman.class, Thief.class, Swarm.class, Bat.class, Brute.class, Assassin.class, BodFly.class};
                    break;
                case 11:
                    chances = new float[]{3, 3, 1, 1, 1, 0.02f, 0.01f, 0.5f};
                    classes = new Class<?>[]{Skeleton.class, Shaman.class, Thief.class, Assassin.class, Swarm.class, Bat.class, Brute.class, BodFly.class};
                    break;

                case 12:
                    chances = new float[]{1};
                    classes = new Class<?>[]{Tengu.class};
                    break;

                case 13:
                    chances = new float[]{1, 0.2f};
                    classes = new Class<?>[]{Bat.class, Brute.class};
                    break;
                case 14:
                    chances = new float[]{1, 1, 0.2f, 0.5f};
                    classes = new Class<?>[]{Bat.class, Brute.class, Spinner.class, ClayGolem.class};
                    break;
                case 15:
                    chances = new float[]{1, 3, 1, 1, 0.02f, 1};
                    classes = new Class<?>[]{Bat.class, Brute.class, Shaman.class, Spinner.class, Elemental.class, ClayGolem.class};
                    break;
                case 16:
                    chances = new float[]{1, 3, 1, 4, 0.02f, 0.01f, 1, 1};
                    classes = new Class<?>[]{Bat.class, Brute.class, Shaman.class, Spinner.class, Elemental.class, Monk.class, SpiderBot.class, ClayGolem.class};
                    break;
                case 17:
                    chances = new float[]{1, 3, 1, 1, 2, 0.02f, 0.01f, 2, 0.5f};
                    classes = new Class<?>[]{Bat.class, Brute.class, Shielded.class, Shaman.class, Spinner.class, Elemental.class, Monk.class, SpiderBot.class, ClayGolem.class};
                    break;

                case 18:
                    chances = new float[]{1};
                    classes = new Class<?>[]{DM300.class};
                    break;

                case 19:
                    chances = new float[]{1, 1, 0.2f};
                    classes = new Class<?>[]{Elemental.class, Warlock.class, Monk.class};
                    break;
                case 20:
                    chances = new float[]{3, 1, 2};
                    classes = new Class<?>[]{Elemental.class, Monk.class, Warlock.class};
                    break;
                case 21:
                    chances = new float[]{1, 1, 1, 1};
                    classes = new Class<?>[]{Elemental.class, Monk.class, Golem.class, Warlock.class};
                    break;
                case 22:
                    chances = new float[]{2, 4, 5, 2, 0.04f};
                    classes = new Class<?>[]{Elemental.class, Monk.class, Golem.class, Warlock.class, Succubus.class};
                    break;
                case 23:
                    chances = new float[]{1, 2, 0.5f, 3, 1, 0.02f};
                    classes = new Class<?>[]{Elemental.class, Monk.class, Senior.class, Golem.class, Warlock.class, Succubus.class};
                    break;
                case 24:
                    chances = new float[]{1};
                    classes = new Class<?>[]{King.class};
                    break;

                case 25:
                    chances = new float[]{1, 1};
                    classes = new Class<?>[]{BrownWolf.class, GrayWolf.class};
                    break;
                case 26:
                    chances = new float[]{1, 1, 1};
                    classes = new Class<?>[]{BrownWolf.class, GrayWolf.class, AirElemental.class};
                    break;
                case 27:
                    chances = new float[]{1, 1, 2};
                    classes = new Class<?>[]{BrownWolf.class, GrayWolf.class, AirElemental.class};
                    break;
                case 28:
                    chances = new float[]{1, 1, 1, 0.5f};
                    classes = new Class<?>[]{BrownWolf.class, GrayWolf.class, AirElemental.class, Yeti.class};
                    break;
                case 29:
                    chances = new float[]{1, 1, 2, 2, 0.5f};
                    classes = new Class<?>[]{BrownWolf.class, GrayWolf.class, AirElemental.class, Yeti.class, IceDemon.class};
                    break;
                case 30:
                    chances = new float[]{1};
                    classes = new Class<?>[]{DemonLord.class};
                    break;
                case 31:
                    chances = new float[]{1, 1};
                    classes = new Class<?>[]{Succubus.class, Eye.class};
                    break;
                case 32:
                    chances = new float[]{1, 2, 1};
                    classes = new Class<?>[]{Succubus.class, Eye.class, Scorpio.class};
                    break;
                case 33:
                case 34:
                    chances = new float[]{1, 2, 3, 1};
                    classes = new Class<?>[]{Succubus.class, Eye.class, Scorpio.class, LostSoul.class};
                    break;
                case 35:
                    chances = new float[]{2, 4, 6, 2, 3};
                    classes = new Class<?>[]{Succubus.class, Eye.class, Scorpio.class, Acidic.class, LostSoul.class};
                    break;

                case 36:
                    chances = new float[]{1};
                    classes = new Class<?>[]{Yog.class};
                    break;

                default:
                    chances = new float[]{1};
                    classes = new Class<?>[]{Eye.class};
                    break;
            }
            return classes[ Random.chances( chances )];
        } else {
            int mobChoice;
            int subDepth = depth % 5;

            if (subDepth == 1) mobChoice = Random.Int(2);
            else if (subDepth == 2) mobChoice = Random.Int(3);
            else if (subDepth == 3) mobChoice = Random.Int(3);
            else if (subDepth == 4) mobChoice = Random.Int(4);
            else mobChoice = Random.Int(4) + 1;


            if (InfiniteBestiary.currentTheme == null) {
                InfiniteBestiary.pickNewTheme();
            }

            switch (InfiniteBestiary.currentTheme) {
                case THEME_SLIMES:
                    switch (mobChoice) {
                        case 0:  return SlimeBrown.class;
                        case 1:  return Slime.class;
                        case 2:  return GreenSnake.class;
                        case 3:  return SewerFly.class;
                        case 4:  return RedSnake.class;
                        default: return SlimeRed.class;
                    }
                case THEME_UNDEAD:
                    switch (mobChoice) {
                        case 0:  return Zombie.class;
                        case 1:  return Skeleton.class;
                        case 2:  return Wraith.class;
                        case 3:  return Undead.class;
                        default: return LostSoul.class;
                    }
                case THEME_NATURAL:
                    switch (mobChoice) {
                        case 0:  return Rat.class;
                        case 1:  return GreenSnake.class;
                        case 2:  return Swarm.class;
                        case 3:  return Bat.class;
                        case 4:  return RedSnake.class;
                        default: return Spinner.class;
                    }
                case THEME_BANDITS:
                    switch (mobChoice) {
                        case 0:  return Bandit.class;
                        case 1:  return Monk.class;
                        case 2:  return Thief.class;
                        case 3:  return Brute.class;
                        default: return Shielded.class;
                    }
                case THEME_EARTH:
                    switch (mobChoice) {
                        case 0:  return ClayGolem.class;
                        case 1:  return Gnoll.class;
                        case 2:  return SpiderBot.class;
                        case 3:  return Shaman.class;
                        default: return Golem.class;
                    }
                case THEME_AIR:
                    switch (mobChoice) {
                        case 0:  return Bat.class;
                        case 1:  return Velocirooster.class;
                        case 2:  return Swarm.class;
                        case 3:  return Wraith.class;
                        default: return Eye.class;
                    }
                case THEME_FIRE:
                    switch (mobChoice) {
                        case 0:  return SlimeRed.class;
                        case 1:  return Senior.class;
                        case 2:  return LostSoul.class;
                        case 3:  return Elemental.class;
                        default: return Succubus.class;
                    }
                case THEME_WATER:
                    switch (mobChoice) {
                        case 0:  return GreenSnake.class;
                        case 1:  return Swarm.class;
                        case 2:  return Crab.class;
                        case 3:  return Spinner.class;
                        case 4:  return RedSnake.class;
                        default: return Succubus.class;
                    }
                case THEME_SPIRIT:
                    switch (mobChoice) {
                        case 0:  return Wraith.class;
                        case 1:  return Shaman.class;
                        case 2:  return LostSoul.class;
                        case 3:  return Warlock.class;
                        default: return Eye.class;
                    }
                case THEME_DEMONS:
                    switch (mobChoice) {
                        case 0:  return Zombie.class;
                        case 1:  return Skeleton.class;
                        case 2:  return Larva.class;
                        case 3:  return Succubus.class;
                        default: return Warlock.class;
                    }
                case THEME_SEWERS:
                    switch (mobChoice) {
                        case 0:  return Rat.class;
                        case 1:  return GreenSnake.class;
                        case 2:  return Gnoll.class;
                        case 3:  return SewerFly.class;
                        case 4:  return RedSnake.class;
                        default: return Slime.class;
                    }
                case THEME_PRISON:
                    switch (mobChoice) {
                        case 0:  return Skeleton.class;
                        case 1:  return Shaman.class;
                        case 2:  return Spinner.class;
                        case 3:  return Assassin.class;
                        default: return Swarm.class;
                    }
                case THEME_CAVES:
                    switch (mobChoice) {
                        case 0:  return ClayGolem.class;
                        case 1:  return Bat.class;
                        case 2:  return Brute.class;
                        case 3:  return Shaman.class;
                        case 4:  return BodFly.class;
                        default: return Spinner.class;
                    }
                case THEME_CITY:
                    switch (mobChoice) {
                        case 0:  return Golem.class;
                        case 1:  return Elemental.class;
                        case 2:  return Monk.class;
                        case 3:  return Warlock.class;
                        default: return Succubus.class;
                    }
                case THEME_FROZEN:
                    switch (mobChoice) {
                        case 0:  return BrownWolf.class;
                        case 1:  return GrayWolf.class;
                        case 2:  return AirElemental.class;
                        case 3:  return Yeti.class;
                        default: return Eye.class;
                    }
                case THEME_HALLS:
                default:
                    switch (mobChoice) {
                        case 0:  return Succubus.class;
                        case 1:  return Eye.class;
                        case 2:  return Scorpio.class;
                        case 3:  return LostSoul.class;
                        default: return Warlock.class;
                    }
            }
        }
	}

	public static boolean isUnique( Char mob ) {
		return mob instanceof Goo || mob instanceof Tengu || mob instanceof DM300 || mob instanceof King
				|| mob instanceof BurningFist || mob instanceof RottingFist || mob instanceof FetidRat
				|| mob instanceof GnollTrickster || mob instanceof GreatCrab
				|| mob instanceof Minotaur || mob instanceof Necromancer || mob instanceof ChaosMage
				|| mob instanceof Tinkerer;
	}
}
