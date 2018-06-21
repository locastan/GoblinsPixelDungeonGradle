/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
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

package com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.shatteredpixel.pixeldungeonunleashed.ui.QuickSlotButton;


public class Crossbow extends MeleeWeapon {
	
	{
		name = "crossbow";
		image = ItemSpriteSheet.CROSSBOW;
		MAX = (4*(tier+1) + level*(tier));     //20 base, down from 25 +4 per level, down from +5
		
		//check Dart.class for additional properties
	}

    public Crossbow() {
        super( 4, 1f, 1f );
    }

    @Override
    public Item upgrade(int n ) {
        Item temp = super.upgrade(n);
        QuickSlotButton.crossbow(true);
        for (int i = 0; i < 2; i++) {
            if (Dungeon.quickslot.getItem(i) instanceof Dart) {
                ((Dart) Dungeon.quickslot.getItem(i)).updateCrossbow();
            }
        }
        return temp;
    }

	@Override
	public boolean doEquip( Hero hero ) {
		if (super.doEquip(hero)){
			QuickSlotButton.crossbow(true);
			for (int i = 0; i < 2; i++) {
				if (Dungeon.quickslot.getItem(i) instanceof Dart) {
					((Dart) Dungeon.quickslot.getItem(i)).updateCrossbow();
				}
			}
			return true;
		} else	return false;
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){
			QuickSlotButton.crossbow(false);
			for (int i = 0; i < 2; i++) {
				if (Dungeon.quickslot.getItem(i) instanceof Dart) {
					((Dart) Dungeon.quickslot.getItem(i)).updateCrossbow();
				}
			}
			return true;
		} else	return false;
	}

    @Override
    public String desc() {
        return
                "A powerful device capable of propelling darts " +
                        "with a lot more force than anyone can throw.";
    }
}
