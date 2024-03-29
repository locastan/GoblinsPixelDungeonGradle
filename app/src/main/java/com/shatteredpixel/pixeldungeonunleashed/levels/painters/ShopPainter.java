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
package com.shatteredpixel.pixeldungeonunleashed.levels.painters;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Belongings;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.ImpShopkeeper;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.pixeldungeonunleashed.items.Ankh;
import com.shatteredpixel.pixeldungeonunleashed.items.Bomb;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.Honeypot;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.MerchantsBeacon;
import com.shatteredpixel.pixeldungeonunleashed.items.Stylus;
import com.shatteredpixel.pixeldungeonunleashed.items.Torch;
import com.shatteredpixel.pixeldungeonunleashed.items.Weightstone;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.LeatherArmor;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.MailArmor;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.PlateArmor;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.ScaleArmor;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.AnkhChain;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.DartBelt;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.PotionBandolier;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.ScrollHolder;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.SeedPouch;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.WandHolster;
import com.shatteredpixel.pixeldungeonunleashed.items.food.OverpricedRation;
import com.shatteredpixel.pixeldungeonunleashed.items.keys.Key;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.Potion;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.PotionOfHealing;
import com.shatteredpixel.pixeldungeonunleashed.items.rings.Ring;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.Scroll;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.pixeldungeonunleashed.items.wands.Wand;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.BattleAxe;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.Glaive;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.Longsword;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.Mace;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.Quarterstaff;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.Spear;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.Sword;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.WarHammer;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.darts.Dart;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.darts.ParalyticDart;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.darts.IncendiaryDart;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.Javelin;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.Shuriken;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.Tamahawk;
import com.shatteredpixel.pixeldungeonunleashed.levels.LastShopLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Room;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.plants.Plant;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class ShopPainter extends Painter {

	private static int pasWidth;
	private static int pasHeight;

	private static ArrayList<Item> itemsToSpawn;
	
	public static void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY_SP );
		
		pasWidth = room.width() - 2;
		pasHeight = room.height() - 2;
		int per = pasWidth * 2 + pasHeight * 2;
		
		if (itemsToSpawn == null)
			generateItems();
		
		int pos = xy2p( room, room.entrance() ) + (per - itemsToSpawn.size()) / 2;
		for (Item item : itemsToSpawn) {
			
			Point xy = p2xy( room, (pos + per) % per );
			int cell = xy.x + xy.y * Level.WIDTH;
			
			if (level.heaps.get( cell ) != null) {
				do {
					cell = room.random();
				} while (level.heaps.get( cell ) != null);
			}
			
			level.drop( item, cell ).type = Heap.Type.FOR_SALE;
			
			pos++;
		}
		
		placeShopkeeper( level, room );
		
		for (Room.Door door : room.connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}

		itemsToSpawn = null;
	}
	
	private static void generateItems() {

		itemsToSpawn = new ArrayList<Item>();
		
		switch (Dungeon.depth) {
		case 7:
		case 1:
			itemsToSpawn.add( (Random.Int( 2 ) == 0 ? new Quarterstaff() : new Spear()).identify() );
			itemsToSpawn.add( Random.Int( 2 ) == 0 ?
					new Dart(Random.NormalIntRange(5, 15)) :
					new IncendiaryDart().quantity(Random.NormalIntRange(2, 4)));
			itemsToSpawn.add( new LeatherArmor().identify() );
			break;
			
		case 13:
		case 11:
			itemsToSpawn.add( (Random.Int( 2 ) == 0 ? new Sword() : new Mace()).identify() );
			itemsToSpawn.add( Random.Int( 2 ) == 0 ?
					new ParalyticDart().quantity(Random.NormalIntRange(2, 5)) :
					new Shuriken().quantity(Random.NormalIntRange(3, 6)));
			itemsToSpawn.add( new MailArmor().identify() );
			break;
			
		case 19:
		case 21:
			itemsToSpawn.add( (Random.Int( 2 ) == 0 ? new Longsword() : new BattleAxe()).identify() );
			itemsToSpawn.add( Random.Int( 2 ) == 0 ?
					new Shuriken().quantity(Random.NormalIntRange(4, 7)) :
					new Javelin().quantity(Random.NormalIntRange(3, 6)));
			itemsToSpawn.add( new ScaleArmor().identify() );
			break;
			
		case 25:
		default:
			itemsToSpawn.add( Random.Int( 2 ) == 0 ? new Glaive().identify() : new WarHammer().identify() );
			itemsToSpawn.add( Random.Int(2) == 0 ?
					new Javelin().quantity(Random.NormalIntRange(4, 7)) :
					new Tamahawk().quantity(Random.NormalIntRange(4, 7)));
			itemsToSpawn.add( new PlateArmor().identify() );
			itemsToSpawn.add( new Dart(Random.NormalIntRange(5, 15)) );
			itemsToSpawn.add( new Torch() );
			break;
		}

		itemsToSpawn.add( new MerchantsBeacon() );

		ChooseBag(Dungeon.hero.belongings);

		itemsToSpawn.add( new PotionOfHealing() );
		for (int i=0; i < 3; i++)
			itemsToSpawn.add( Generator.random( Generator.Category.POTION ) );

		itemsToSpawn.add( new ScrollOfIdentify() );
		itemsToSpawn.add( new ScrollOfRemoveCurse() );
		itemsToSpawn.add( new ScrollOfMagicMapping() );
		itemsToSpawn.add( Generator.random( Generator.Category.SCROLL ) );

		for (int i=0; i < 2; i++)
			itemsToSpawn.add( Random.Int(2) == 0 ?
					Generator.random( Generator.Category.POTION ) :
					Generator.random( Generator.Category.SCROLL ) );

		itemsToSpawn.add( new OverpricedRation() );
		itemsToSpawn.add( new OverpricedRation() );

		itemsToSpawn.add( new Bomb().random() );
		switch (Random.Int(5)){
			case 1:
				itemsToSpawn.add( new Bomb() );
				break;
			case 2:
				itemsToSpawn.add( new Bomb().random() );
				break;
			case 3:
			case 4:
				itemsToSpawn.add( new Honeypot() );
				break;
		}


		if (Dungeon.depth == 7) {
			itemsToSpawn.add( new Ankh() );
			itemsToSpawn.add( new Weightstone() );
		} else {
			itemsToSpawn.add(Random.Int(2) == 0 ? new Ankh() : new Weightstone());
		}


		TimekeepersHourglass hourglass = Dungeon.hero.belongings.getItem(TimekeepersHourglass.class);
		if (hourglass != null){
			int bags = 0;
			//creates the given float percent of the remaining bags to be dropped.
			//this way players who get the hourglass late can still max it, usually.
			switch (Dungeon.depth) {
				case 7:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.20f ); break;
				case 13:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.25f ); break;
				case 19:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.50f ); break;
				case 25:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.80f ); break;
				default:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.25f ); break;
			}

			for(int i = 1; i <= bags; i++){
				itemsToSpawn.add( new TimekeepersHourglass.sandBag());
				hourglass.sandBags ++;
			}
		}

		Item rare;
		switch (Random.Int(10)){
			case 0:
				rare = Generator.random( Generator.Category.WAND );
				rare.level = 0;
				break;
			case 1:
				rare = Generator.random(Generator.Category.RING);
				rare.level = 1;
				break;
			case 2:
				rare = Generator.randomArtifact().identify();
				break;
			default:
				rare = new Stylus();
		}
		rare.cursed = rare.cursedKnown = false;
		itemsToSpawn.add( rare );

		//this is a hard limit, level gen allows for at most an 8x5 room, can't fit more than 39 items + 1 shopkeeper.
		if (itemsToSpawn.size() > 39)
			throw new RuntimeException("Shop attempted to carry more than 39 items!");

		Collections.shuffle(itemsToSpawn);
	}

	private static void ChooseBag(Belongings pack) {

        int seeds = 0, scrolls = 0, potions = 0, wands = 0, darts = 0, chainstuff = 0;
        boolean pouch = false, holder = false, bandolier = false, holster = false, belt = false, chain = false;

        if (pack.backpack.items.contains(SeedPouch.class.getSimpleName())) {
            pouch = true;
        }
        if (pack.backpack.items.contains(ScrollHolder.class.getSimpleName())) {
            holder = true;
        }
        if (pack.backpack.items.contains(PotionBandolier.class.getSimpleName())) {
            bandolier = true;
        }
        if (pack.backpack.items.contains(WandHolster.class.getSimpleName())) {
            holster = true;
        }
        if (pack.backpack.items.contains(DartBelt.class.getSimpleName())) {
            belt = true;
        }
        if (pack.backpack.items.contains(AnkhChain.class.getSimpleName())) {
            chain = true;
        }

        //count up items in the main bag, for bags which are not in players inventory or have already dropped in any mode besides endless.
        for (Item item : pack.backpack.items) {
            if (!pouch && !Dungeon.limitedDrops.seedBag.dropped() && item instanceof Plant.Seed)
                seeds++;
            else if (!holder && !Dungeon.limitedDrops.scrollBag.dropped() && item instanceof Scroll)
                scrolls++;
            else if (!bandolier && !Dungeon.limitedDrops.potionBag.dropped() && item instanceof Potion)
                potions++;
            else if (!holster && !Dungeon.limitedDrops.wandBag.dropped() && item instanceof Wand)
                wands++;
            else if (!belt && !Dungeon.limitedDrops.dartBag.dropped() && item instanceof Dart)
                darts++;
            else if (!chain && !Dungeon.limitedDrops.ankhChain.dropped() && (item instanceof Ankh || item instanceof Ring || item instanceof Key))
                chainstuff++;
        }
        //then pick whichever valid bag has the most items available to put into it.
        //note that the order here gives a perference if counts are otherwise equal
        if (seeds >= scrolls && seeds >= potions && seeds >= wands && seeds >= darts && seeds >= chainstuff && !Dungeon.limitedDrops.seedBag.dropped()) {
            Dungeon.limitedDrops.seedBag.drop();
            itemsToSpawn.add(new SeedPouch());

        } else if (scrolls >= potions && scrolls >= wands && scrolls >= darts && scrolls >= chainstuff && !Dungeon.limitedDrops.scrollBag.dropped()) {
            Dungeon.limitedDrops.scrollBag.drop();
            itemsToSpawn.add(new ScrollHolder());

        } else if (potions >= wands && potions >= darts && potions >= chainstuff && !Dungeon.limitedDrops.potionBag.dropped()) {
            Dungeon.limitedDrops.potionBag.drop();
            itemsToSpawn.add(new PotionBandolier());

        } else if (wands >= darts && wands >= chainstuff && !Dungeon.limitedDrops.wandBag.dropped()) {
            Dungeon.limitedDrops.wandBag.drop();
            itemsToSpawn.add(new WandHolster());

        } else if (darts >= chainstuff && !Dungeon.limitedDrops.dartBag.dropped()) {
            Dungeon.limitedDrops.dartBag.drop();
            itemsToSpawn.add(new DartBelt());

        } else if (!Dungeon.limitedDrops.ankhChain.dropped()){
            Dungeon.limitedDrops.ankhChain.drop();
            itemsToSpawn.add(new AnkhChain());
        }
    }


	public static int spaceNeeded(){
		if (itemsToSpawn == null)
			generateItems();

		//plus one for the shopkeeper
		return itemsToSpawn.size() + 1;
	}
	
	private static void placeShopkeeper( Level level, Room room ) {
		
		int pos;
		do {
			pos = room.random();
		} while (level.heaps.get( pos ) != null);
		
		Mob shopkeeper = level instanceof LastShopLevel ? new ImpShopkeeper() : new Shopkeeper();
		shopkeeper.pos = pos;
		level.mobs.add( shopkeeper );
		
		if (level instanceof LastShopLevel) {
			for (int i=0; i < Level.NEIGHBOURS9.length; i++) {
				int p = shopkeeper.pos + Level.NEIGHBOURS9[i];
				if (level.map[p] == Terrain.EMPTY_SP) {
					level.map[p] = Terrain.WATER;
				}
			}
		}
	}
	
	private static int xy2p( Room room, Point xy ) {
		if (xy.y == room.top) {
			
			return (xy.x - room.left - 1);
			
		} else if (xy.x == room.right) {
			
			return (xy.y - room.top - 1) + pasWidth;
			
		} else if (xy.y == room.bottom) {
			
			return (room.right - xy.x - 1) + pasWidth + pasHeight;
			
		} else {
			
			if (xy.y == room.top + 1) {
				return 0;
			} else {
				return (room.bottom - xy.y - 1) + pasWidth * 2 + pasHeight;
			}
			
		}
	}
	
	private static Point p2xy( Room room, int p ) {
		if (p < pasWidth) {
			
			return new Point( room.left + 1 + p, room.top + 1);
			
		} else if (p < pasWidth + pasHeight) {
			
			return new Point( room.right - 1, room.top + 1 + (p - pasWidth) );
			
		} else if (p < pasWidth * 2 + pasHeight) {
			
			return new Point( room.right - 1 - (p - (pasWidth + pasHeight)), room.bottom - 1 );
			
		} else {
			
			return new Point( room.left + 1, room.bottom - 1 - (p - (pasWidth * 2 + pasHeight)) );
			
		}
	}
}
