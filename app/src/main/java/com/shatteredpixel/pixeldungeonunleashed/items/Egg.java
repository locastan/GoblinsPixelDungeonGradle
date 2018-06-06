/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
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
package com.shatteredpixel.pixeldungeonunleashed.items;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.FrostKlik;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.PhaseKlik;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.PET;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.FireKlik;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.MetalKlik;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.SpiderKlik;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.PoisonKlik;
import com.shatteredpixel.pixeldungeonunleashed.effects.Pushing;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndMessage;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Calendar;

public class Egg extends Item {

	private static final String TXT_NOTREADY = "Something tells you it's not ready yet.";
	private static final String TXT_YOLK = "Ewww. Gross, its just some slimy goop.";
	private static final String TXT_HATCH = "Something hatches!";
	private static final String TXT_SLITHERS = "Something squirms inside!";
	private static final String TXT_KICKS = "Something klicks back!";
	private static final String TXT_SLOSH = "Just some sloshing around.";
	private static final String TXT_ZAP = "Ouch! Something zaps you!.";
	
	public static final float TIME_TO_USE = 1;

	public static final String AC_BREAK = "BREAK OPEN";
	public static final String AC_SHAKE = "SHAKE";
	
	public static final int FIREKLIK = 15;
	public static final int PHASEKLIK = 5;
	public static final int FROSTKLIK = Calendar.getInstance().get(Calendar.MONTH)==11 ? 1 : 5;
	public static final int POISONKLIK = 5;
	public static final int SPIDER = 200;
	public static final int KLIK = 1000;
	

		{
		name = "klik soulorb";
		image = ItemSpriteSheet.EGG;
		unique = true;
		stackable = false;
		}
		
		public int startMoves = 0;
		public int moves = 0;
		public int burns = 0;
		public int freezes = 0;
		public int poisons = 0;
		public int lits = 0;
		public int summons = 0;
		
		private static final String STARTMOVES = "startMoves";
		private static final String MOVES = "moves";
		private static final String BURNS = "burns";
		private static final String FREEZES = "freezes";
		private static final String POISONS = "poisons";
		private static final String LITS = "lits";
		private static final String SUMMONS = "summons";
		
		
		
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(STARTMOVES, startMoves);
			bundle.put(MOVES, moves);
			bundle.put(BURNS, burns);
			bundle.put(FREEZES, freezes);
			bundle.put(POISONS, poisons);
			bundle.put(LITS, lits);
			bundle.put(SUMMONS, summons);
			
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			startMoves = bundle.getInt(STARTMOVES);
			moves = bundle.getInt(MOVES);
			burns = bundle.getInt(BURNS);
			freezes = bundle.getInt(FREEZES);
			poisons = bundle.getInt(POISONS);
			lits = bundle.getInt(LITS);
			summons = bundle.getInt(SUMMONS);
			
		}
						
		public int checkMoves () {
			return moves;
		}
		public int checkBurns () {
			return burns;
		}
		public int checkFreezes () {
			return freezes;
		}
		public int checkPoisons () {
			return poisons;
		}
		public int checkLits () {
			return lits;
		}
		public int checkSummons () {
			return summons;
		}
		
		@Override
		public boolean doPickUp(Hero hero) {
				
				GLog.w("The orb likes to be in your pack.");
				
				Egg egg = hero.belongings.getItem(Egg.class);
				if (egg!=null){
					GLog.w("You can probably only keep one orb at a time.");
				}
						 
			 return super.doPickUp(hero);				
		}	
		
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_BREAK);
		actions.add(AC_SHAKE);
		
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {


		if (action == AC_BREAK) {	
			
			boolean hatch = false;
			
			if (checkFreezes()>= FROSTKLIK) {
				  FrostKlik pet = new FrostKlik();
				  eggHatch(pet);
				  hatch=true;
				  //spawn ice klik
			  } else if (checkPoisons()>= POISONKLIK) {
				  PoisonKlik pet = new PoisonKlik();
				  eggHatch(pet);
				  hatch=true;
				  //spawn poison klik
			  } else if (checkLits()>= PHASEKLIK) {
				  PhaseKlik pet = new PhaseKlik();
				  eggHatch(pet);
				  hatch=true;
				  //spawn phase klik
			  } else if (checkBurns()>= FIREKLIK) {
				  FireKlik pet = new FireKlik();
				  eggHatch(pet);
				  hatch=true;
				 //spawn red klik
			  } else if (checkMoves()>= KLIK) {
				  MetalKlik pet = new MetalKlik();
				  eggHatch(pet);
				  hatch=true;
				  //spawn metal klik
			  } else if (checkMoves()>=SPIDER) {
				  SpiderKlik pet = new SpiderKlik();
				  eggHatch(pet);
				  hatch=true;
				  //spawn spidersilk klik
			  }	
		           
		  if (!hatch)	{
			  detach(Dungeon.hero.belongings.backpack);		 			 
			  GLog.w(TXT_YOLK);  			  
		  }
		  
		  hero.next();
		
		}
		
		else if (action == AC_SHAKE) {
						            
			 boolean alive = false;
			  
			  if (checkFreezes()>= FROSTKLIK) {
				 GLog.w(TXT_KICKS);
				 alive = true;
				  //spawn ice klik
			  } else if (checkPoisons()>= POISONKLIK) {
				   GLog.w(TXT_KICKS);
				   alive = true;
				  //spawn poison klik
			  } else if (checkLits()>= PHASEKLIK) {
				  GLog.w(TXT_KICKS);
				  alive = true;
				  //spawn phase klik
			  } else if (checkBurns()>= FIREKLIK) {
				  GLog.w(TXT_KICKS);
				  alive = true;
				  //spawn red klik
			  } else if (checkMoves()>= KLIK) {
				  GLog.w(TXT_KICKS);
				  alive = true;
				  //spawn metal klik
			  } else if (checkMoves()>=SPIDER) {
				  GLog.w(TXT_SLITHERS);
				  alive = true;
				  //spawn spidersilk klik
			  }
			  
			  if (!alive)	{				  
				  GLog.w(TXT_SLOSH);  			  
			  }
			  				
		} else {

			super.execute(hero, action);

		}
		
		
		
	}	
	
	public int getSpawnPos(){
		int newPos = -1;
		int pos = Dungeon.hero.pos;
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			boolean[] passable = Level.passable;

			for (int n : Level.NEIGHBOURS8) {
				int c = pos + n;
				if (passable[c] && Actor.findChar(c) == null) {
					candidates.add(c);
				}
			}

			newPos = candidates.size() > 0 ? Random.element(candidates) : -1;
			
		return newPos;
	}
	
	
	public void eggHatch (PET pet) {		
		
		  int spawnPos = getSpawnPos();
		  if (spawnPos != -1 && !Dungeon.hero.haspet) {
				
				pet.spawn(1);
				pet.HP = pet.HT;
				pet.pos = spawnPos;
				pet.state = pet.HUNTING;

				GameScene.add(pet);
				Actor.addDelayed(new Pushing(pet, Dungeon.hero.pos, spawnPos), -1f);

				pet.sprite.alpha(0);
				pet.sprite.parent.add(new AlphaTweener(pet.sprite, 1, 0.15f));
				
				detach(Dungeon.hero.belongings.backpack);		 			 
				GLog.w(TXT_HATCH);
			    if (Dungeon.hero.petCount == 0) {
                    GameScene.show( new WndMessage( "Hello,\n\n so you got yourself a pet. Please ensure the proper feeding"+
                            " and keeping of your pet via the new pets tab.\n(Accessible through clicking your hero image top left.)\n\n"+
                            "- Pixel pet owners society." ) );
                }
				Dungeon.hero.haspet=true;
				
				assignPet(pet);
				
		  } else {
			  
			  Dungeon.hero.spend(Egg.TIME_TO_USE);
			  GLog.w(TXT_NOTREADY);

		  }
	}
	
	
	private void assignPet(PET pet){
		
		  Dungeon.hero.petType=pet.type;
		  Dungeon.hero.petLevel=pet.level;
		  Dungeon.hero.petKills=pet.kills;	
		  Dungeon.hero.petHP=pet.HP;
		  Dungeon.hero.petExperience=pet.experience;
		  Dungeon.hero.petCooldown=pet.cooldown;		
	}
		
	@Override
	public int price() {
		return 500 * quantity;
	}
	
	
	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

		
	@Override
	public String info() {
		return "A soulorb of the mysterious klik species. You can see it started to develop, but then stopped at some point. Maybe you can help it hatch completely?";
	}

}
