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

import com.shatteredpixel.pixeldungeonunleashed.items.Dewdrop;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.Armor;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.ClothArmor;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.HornOfPlenty;
import com.shatteredpixel.pixeldungeonunleashed.items.food.Food;
import com.shatteredpixel.pixeldungeonunleashed.items.food.OverpricedRation;
import com.shatteredpixel.pixeldungeonunleashed.items.food.Yumyuck;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfHealing;

public class Challenges {

	public static final int NO_FOOD				= 1;
	public static final int NO_ARMOR			= 2;
	public static final int NO_HEALING			= 4;
	public static final int NO_HERBALISM		= 8;
	public static final int SWARM_INTELLIGENCE	= 16;
	public static final int DARKNESS			= 32;
	public static final int NO_SCROLLS		    = 64;

	public static final String[] NAMES = {
			"On diet",
			"Faith is my armor",
			"Pharmacophobia",
			"Barren land",
			"Swarm intelligence",
			"Into darkness",
			"Forbidden runes"
	};

	public static final int[] MASKS = {
			NO_FOOD, NO_ARMOR, NO_HEALING, NO_HERBALISM, SWARM_INTELLIGENCE, DARKNESS, NO_SCROLLS
	};

    public static boolean isItemBlocked( Item item ){
        if (Dungeon.isChallenged(NO_FOOD)){
            if (item instanceof Food && !(item instanceof OverpricedRation)) {
                return true;
            } else if (item instanceof HornOfPlenty){
                return true;
            }
        }

        if (Dungeon.isChallenged(NO_ARMOR)){
            if (item instanceof Armor && !(item instanceof ClothArmor)) {
                return true;
            }
        }

        if (Dungeon.isChallenged(NO_HEALING)){
            if (item instanceof PotionOfHealing){
                return true;
            } else if (item instanceof Yumyuck
                    && ((Yumyuck) item).potionAttrib instanceof PotionOfHealing){
                return true;
            }
        }

        if (Dungeon.isChallenged(NO_HERBALISM)){
            if (item instanceof Dewdrop) {
                return true;
            }
        }

        return false;

    }
}