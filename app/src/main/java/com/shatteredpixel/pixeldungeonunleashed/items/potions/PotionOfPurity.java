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
package com.shatteredpixel.pixeldungeonunleashed.items.potions;

import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ConfusionGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.StenchGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.VenomGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Infested;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Blob;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ParalyticGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.ToxicGas;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.GasesImmunity;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.effects.CellEmitter;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.utils.BArray;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.utils.PathFinder;

public class PotionOfPurity extends Potion {

	private static final String TXT_FRESHNESS	= "You feel uncommon freshness in the air.";
	private static final String TXT_NO_SMELL	= "You've stopped sensing any smells!";
    private static final String TXT_UNCURSE	= "A malevolent energy dispersed.";
    private static final String TXT_DISINFECT	= "Your infestation is cured.";
	
	private static final int DISTANCE	= 5;
	
	{
		name = "Potion of Purification";
		initials = "Pu";
	}
	
	@Override
	public void shatter( int cell ) {
		
		PathFinder.buildDistanceMap( cell, BArray.not( Level.losBlocking, null ), DISTANCE );
		
		boolean procd = false;
		
		Blob[] blobs = {
			Dungeon.level.blobs.get( ToxicGas.class ),
			Dungeon.level.blobs.get( ParalyticGas.class ),
			Dungeon.level.blobs.get( ConfusionGas.class ),
			Dungeon.level.blobs.get( StenchGas.class ),
			Dungeon.level.blobs.get( VenomGas.class )
		};
		
		for (int j=0; j < blobs.length; j++) {
			
			Blob blob = blobs[j];
			if (blob == null) {
				continue;
			}
			
			for (int i=0; i < Level.LENGTH; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE) {
					
					int value = blob.cur[i];
					if (value > 0) {
						
						blob.cur[i] = 0;
						blob.volume -= value;
						procd = true;

						if (Dungeon.visible[i]) {
							CellEmitter.get( i ).burst( Speck.factory( Speck.DISCOVER ), 1 );
						}
					}

				}
			}
		}
		
		boolean heroAffected = PathFinder.distance[Dungeon.hero.pos] < Integer.MAX_VALUE;

        Heap heap = Dungeon.level.heaps.get( cell );
		
		if (procd) {

			if (Dungeon.visible[cell]) {
                if (heap != null) {
                    for (Item i : heap.items) {
                        if (i != null && i.cursed) {
                            i.cursed = false;
                            GLog.p( TXT_UNCURSE );
                        }
                    }
                }
				splash( cell );
				Sample.INSTANCE.play( Assets.SND_SHATTER );
			}

			setKnown();

			if (heroAffected) {
				GLog.p( TXT_FRESHNESS );
			}
			
		} else {

            if (heap != null) {
                for (Item i : heap.items) {
                    if (i != null && i.cursed) {
                        i.cursed = false;
                        GLog.p( TXT_UNCURSE );
                    }
                }
            }
			super.shatter( cell );
			
			if (heroAffected) {
				GLog.i( TXT_FRESHNESS );
				setKnown();
			}
			
		}
	}
	
	@Override
	public void apply( Hero hero ) {
        if (hero.buff(Infested.class) != null) {
            GLog.p(TXT_DISINFECT);
            Buff.detach(hero, Infested.class);
        } else {
            GLog.w(TXT_NO_SMELL);
            Buff.prolong(hero, GasesImmunity.class, GasesImmunity.DURATION);
        }
		setKnown();
	}

	@Override
	public int hungerMods() {
		return 5; // reduces hunger by 5%
	}

	@Override
	public String desc() {
		return
			"This reagent will quickly neutralize all harmful gases in the area of effect and remove curses from items directly hit. " +
			"Drinking it will give you a temporary immunity to such gases or remove parasites.";
	}
	
	@Override
	public int price() {
		return isKnown() ? 50 * quantity : super.price();
	}
}
