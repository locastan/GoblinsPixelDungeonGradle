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

import java.util.ArrayList;
import java.util.HashSet;

import com.shatteredpixel.pixeldungeonunleashed.Challenges;
import com.shatteredpixel.pixeldungeonunleashed.GoblinsPixelDungeon;
import com.shatteredpixel.pixeldungeonunleashed.Statistics;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Fire;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Hunger;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.items.Recipe;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.pixeldungeonunleashed.plants.Plant.Seed;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Badges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.effects.Splash;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.ItemStatusHandler;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndOptions;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Potion extends Item {
	
	public static final String AC_DRINK	= "DRINK";
	
	private static final String TXT_HARMFUL		= "Harmful potion!";
	private static final String TXT_BENEFICIAL	= "Beneficial potion";
	private static final String TXT_YES			= "Yes, I know what I'm doing";
	private static final String TXT_NO			= "No, I changed my mind";
	private static final String TXT_R_U_SURE_DRINK =
		"Are you sure you want to drink it? In most cases you should throw such potions at your enemies.";
	private static final String TXT_R_U_SURE_THROW =
		"Are you sure you want to throw it? In most cases it makes sense to drink it.";
	
	private static final float TIME_TO_DRINK = 1f;

	protected String initials;
	
	private static final Class<?>[] potions = {
		PotionOfHealing.class,
		PotionOfExperience.class,
		PotionOfToxicGas.class,
		PotionOfLiquidFlame.class,
		PotionOfStrength.class,
		PotionOfParalyticGas.class,
		PotionOfLevitation.class,
		PotionOfMindVision.class,
		PotionOfPurity.class,
		PotionOfInvisibility.class,
		PotionOfMight.class,
		PotionOfFrost.class,
		PotionOfSpeed.class,
		PotionOfSlowness.class
	};
	private static final String[] colors = {
		"turquoise", "crimson", "azure", "jade", "golden", "magenta",
		"charcoal", "ivory", "amber", "bistre", "indigo", "silver",
		"brisk", "moldy", "speckled", "bubbly"
	};
	private static final Integer[] images = {
		ItemSpriteSheet.POTION_TURQUOISE,
		ItemSpriteSheet.POTION_CRIMSON,
		ItemSpriteSheet.POTION_AZURE,
		ItemSpriteSheet.POTION_JADE,
		ItemSpriteSheet.POTION_GOLDEN,
		ItemSpriteSheet.POTION_MAGENTA,
		ItemSpriteSheet.POTION_CHARCOAL,
		ItemSpriteSheet.POTION_IVORY,
		ItemSpriteSheet.POTION_AMBER,
		ItemSpriteSheet.POTION_BISTRE,
		ItemSpriteSheet.POTION_INDIGO,
		ItemSpriteSheet.POTION_SILVER,
		ItemSpriteSheet.POTION_BRISK,
		ItemSpriteSheet.POTION_MOLDY,
		ItemSpriteSheet.POTION_SPECKLED,
		ItemSpriteSheet.POTION_BUBBLY
	};
	
	private static ItemStatusHandler<Potion> handler;
	
	private String color;

	public boolean ownedByFruit = false;
	
	{
		stackable = true;
		defaultAction = AC_DRINK;
	}
	
	@SuppressWarnings("unchecked")
	public static void initColors() {
		handler = new ItemStatusHandler<Potion>( (Class<? extends Potion>[])potions, colors, images );
	}
	
	public static void save( Bundle bundle ) {
		handler.save(bundle);
	}
	
	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<Potion>( (Class<? extends Potion>[])potions, colors, images, bundle );
	}
	
	public Potion() {
		super();
		syncVisuals();
	}

	@Override
	public void syncVisuals(){
		image = handler.image( this );
		color = handler.label( this );
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_DRINK );
		return actions;
	}
	
	@Override
	public void execute( final Hero hero, String action ) {
		if (action.equals( AC_DRINK )) {
			
			if (isKnown() && (
					this instanceof PotionOfLiquidFlame ||
					this instanceof PotionOfToxicGas ||
					this instanceof PotionOfSlowness ||
					this instanceof PotionOfParalyticGas)) {
				
					GameScene.show(
						new WndOptions( TXT_HARMFUL, TXT_R_U_SURE_DRINK, TXT_YES, TXT_NO ) {
							@Override
							protected void onSelect(int index) {
								if (index == 0) {
									drink( hero );
								}
							}
						}
					);
					
				} else {
					drink(hero);
				}
			
		} else {
			
			super.execute(hero, action);
			
		}
	}
	
	@Override
	public void doThrow( final Hero hero ) {

		if (isKnown() && (
			this instanceof PotionOfExperience ||
			this instanceof PotionOfHealing ||
			this instanceof PotionOfMindVision ||
			this instanceof PotionOfStrength ||
			this instanceof PotionOfInvisibility ||
			this instanceof PotionOfSpeed ||
			this instanceof PotionOfMight)) {
		
			GameScene.show(
				new WndOptions( TXT_BENEFICIAL, TXT_R_U_SURE_THROW, TXT_YES, TXT_NO ) {
					@Override
					protected void onSelect(int index) {
						if (index == 0) {
							Potion.super.doThrow( hero );
						}
					}
				}
			);
			
		} else {
			super.doThrow(hero);
		}
	}
	
	protected void drink( Hero hero ) {
		
		detach( hero.belongings.backpack );
		
		hero.spend( TIME_TO_DRINK );
		hero.busy();
		apply( hero );
		
		Sample.INSTANCE.play( Assets.SND_DRINK );
		
		hero.sprite.operate( hero.pos );

		if (this.hungerMods() > 0) {
			// non-harmful potions can appease hunger to some extent (5%)
			Hunger hunger = hero.buff(Hunger.class);
			hunger.reduceHunger(hunger.HUNGRY * this.hungerMods() / 100);
		} else if (this.hungerMods() < 0) {
			// non-harmful potions can appease hunger to some extent (5%)
			Hunger hunger = hero.buff(Hunger.class);
			hunger.reduceHunger((hunger.HUNGRY * this.hungerMods() / 100));
		}
	}

	public int hungerMods() {
		return 0; // most potions will affect current hunger by 0%
	}
	
	@Override
	protected void onThrow( int cell ) {
		if (Dungeon.level.map[cell] == Terrain.WELL || Level.pit[cell]) {
			
			super.onThrow( cell );
			
		} else  {
			
			shatter( cell );
			
		}
	}
	
	public void apply( Hero hero ) {
		shatter( hero.pos );
	}
	
	public void shatter( int cell ) {
		if (Dungeon.visible[cell]) {
			GLog.i( "The flask shatters and " + color() + " liquid splashes harmlessly" );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
			splash( cell );
		}
	}

	@Override
	public void cast( final Hero user, int dst ) {
			super.cast(user, dst);
	}
	
	public boolean isKnown() {
		return handler.isKnown( this );
	}
	
	public void setKnown() {
		if (!ownedByFruit) {
			if (!isKnown()) {
				handler.know(this);
			}

			Badges.validateAllPotionsIdentified();
		}
	}
	
	@Override
	public Item identify() {

		setKnown();
		return this;
	}
	
	protected String color() {
		return color;
	}
	
	@Override
	public String name() {
		return isKnown() ? name : color + " potion";
	}
	
	@Override
	public String info() {
		return isKnown() ?
			desc() :
			"This flask contains a swirling " + color + " liquid. " +
			"Who knows what it will do when drunk or thrown?";
	}

	public String initials(){
		return isKnown() ? initials : null;
	}
	
	@Override
	public boolean isIdentified() {
		return isKnown();
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	public static HashSet<Class<? extends Potion>> getKnown() {
		return handler.known();
	}
	
	public static HashSet<Class<? extends Potion>> getUnknown() {
		return handler.unknown();
	}
	
	public static boolean allKnown() {
		return handler.known().size() == potions.length;
	}
	
	protected void splash( int cell ) {
		final int color = ItemSprite.pick( image, 8, 10 );
		Splash.at( cell, color, 5 );

		Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );
		if (fire != null)
			fire.clear( cell );

		Char ch = Actor.findChar(cell);
		if (ch != null)
			Buff.detach( ch, Burning.class );
	}
	
	@Override
	public int price() {
		return 20 * quantity;
	}

	public static class RandomPotion extends Recipe {


		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			AlchemistsToolkit.alchemy alchemy = Dungeon.hero.buff(AlchemistsToolkit.alchemy.class);
			int bonus = alchemy != null ? alchemy.level() : -1;
            int count = 0;
            for (Item item : ingredients) {
                if (item instanceof Seed) {
                    count += item.quantity();
                }  else{
                    count = 0;
                    break;
                }
            }
			for (Item ingredient : ingredients){
				if (!(ingredient instanceof Seed && ingredient.quantity() >= 1)){
					return false;
				}
			}
			return bonus != -1 ? alchemy.tryCook(count) : count >= 3;
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 1;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			for (Item ingredient : ingredients){
				ingredient.quantity(ingredient.quantity() - 1);
			}

			Item result;

			if (Random.Int( 3 ) == 0) {

				result = Generator.random( Generator.Category.POTION );

			} else {

				Class<? extends Item> itemClass = ((Seed)Random.element(ingredients)).alchemyClass;
				try {
					result = itemClass.newInstance();
				} catch (Exception e) {
					GoblinsPixelDungeon.reportException(e);
					result = Generator.random( Generator.Category.POTION );
				}

			}

			while (result instanceof PotionOfHealing
					&& (Dungeon.isChallenged(Challenges.NO_HEALING)
					|| Random.Int(10) < Dungeon.limitedDrops.cookingHP.count)) {
				result = Generator.random(Generator.Category.POTION);
			}

			if (result instanceof PotionOfHealing) {
				Dungeon.limitedDrops.cookingHP.count++;
			}

			Statistics.potionsCooked++;
			Badges.validatePotionsCooked();

			return result;
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			return new WndBag.Placeholder(ItemSpriteSheet.BANDOLIER){
				{
					name = "random potion";
				}

				@Override
				public String info() {
					return "";
				}
			};
		}
	}
}
