/*
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015  David Mitchell
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
import com.watabou.utils.Random;

public class InfiniteBestiary extends Bestiary {

    public enum Themes { THEME_SLIMES, THEME_UNDEAD, THEME_NATURAL, THEME_BANDITS, THEME_EARTH, THEME_AIR,
                  THEME_FIRE, THEME_WATER, THEME_SPIRIT, THEME_DEMONS, THEME_SEWERS, THEME_PRISON,
                  THEME_CAVES, THEME_CITY, THEME_HALLS, THEME_FROZEN
    }

    public static Themes currentTheme;

    public static void pickNewTheme() {
        // 1-5 = sewers, 6-10 = prison, 11-15 = caves, 16-20 = city, 21-25 = frozen, 26-30 = halls...
        int Tileset = (((Dungeon.depth -1) / 5) % 6);
        switch (Tileset) {
            case 0: // sewer levels
                switch (Random.Int(4)) {
                    case 0: currentTheme = Themes.THEME_SLIMES; break;
                    case 1: currentTheme = Themes.THEME_UNDEAD; break;
                    case 2: currentTheme = Themes.THEME_WATER; break;
                    default: currentTheme = Themes.THEME_SEWERS; break;
                }
                break;
            case 1: // prison levels
                switch (Random.Int(4)) {
                    case 0: currentTheme = Themes.THEME_UNDEAD; break;
                    case 1: currentTheme = Themes.THEME_BANDITS; break;
                    case 2: currentTheme = Themes.THEME_EARTH; break;
                    default: currentTheme = Themes.THEME_PRISON; break;
                }
                break;
            case 2: // caves levels
                switch (Random.Int(4)) {
                    case 0: currentTheme = Themes.THEME_NATURAL; break;
                    case 1: currentTheme = Themes.THEME_SLIMES; break;
                    case 2: currentTheme = Themes.THEME_AIR; break;
                    default: currentTheme = Themes.THEME_CAVES; break;
                }
                break;
            case 3: // city levels
                switch (Random.Int(4)) {
                    case 1: currentTheme = Themes.THEME_UNDEAD; break;
                    case 2: currentTheme = Themes.THEME_FIRE; break;
                    case 3: currentTheme = Themes.THEME_BANDITS; break;
                    default: currentTheme = Themes.THEME_SEWERS; break;
                }
                break;
            case 4: // frozen levels
                switch (Random.Int(4)) {
                    case 1: currentTheme = Themes.THEME_WATER; break;
                    case 2: currentTheme = Themes.THEME_AIR; break;
                    case 3: currentTheme = Themes.THEME_FROZEN; break;
                    default: currentTheme = Themes.THEME_EARTH; break;
                }
                break;
            default: // halls levels
                switch (Random.Int(4)) {
                    case 0: currentTheme = Themes.THEME_DEMONS; break;
                    case 1: currentTheme = Themes.THEME_UNDEAD; break;
                    case 2: currentTheme = Themes.THEME_SPIRIT; break;
                    default: currentTheme = Themes.THEME_HALLS; break;
                }
                break;
        }
    }

}