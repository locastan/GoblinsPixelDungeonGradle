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
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.Bunny;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.Fairy;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.PET;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.SugarplumFairy;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.Velocirooster;
import com.shatteredpixel.pixeldungeonunleashed.effects.Pushing;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.SparkParticle;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.LightningTrap;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Calendar;

public class EasterEgg extends Item {

	private static final String TXT_NOTREADY = "Something tells you it's not ready yet.";
	private static final String TXT_YOLK = "Ewww. Gross, uncooked egg of a random creature.";
	private static final String TXT_HATCH = "Something hatches!";
	private static final String TXT_SCRATCH = "Something scratches back!";
	private static final String TXT_KICKS = "Something powerful kicks back!";
	private static final String TXT_SLOSH = "Just some sloshing around.";
	private static final String TXT_ZAP = "Ouch! Something zaps you back!.";
	
	public static final float TIME_TO_USE = 1;

	public static final String AC_BREAK = "BREAK OPEN";
	public static final String AC_SHAKE = "SHAKE";
	

	public static final int VELOCIROOSTER = 1;
	public static final int FAIRY = 1;
	public static final int BUNNY = 10;
	

		{
		name = "egg";
		image = ItemSpriteSheet.EASTEREGG;
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
				
				GLog.w("The egg likes to be warm in your pack.");
				
				EasterEgg egg = hero.belongings.getItem(EasterEgg.class);
				if (egg!=null){
					GLog.w("You can probably only keep one egg warm at a time.");
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
			if (checkSummons()>=FAIRY) {
				if (Calendar.getInstance().get(Calendar.MONTH)==11){
					SugarplumFairy pet = new SugarplumFairy();
					eggHatch(pet);
					hatch=true;
				} else {
					Fairy pet = new Fairy();
					eggHatch(pet);
					hatch=true;
				}
				//spawn fairy
            } else if (checkBurns()>=VELOCIROOSTER) {
				  Velocirooster pet = new Velocirooster();
				  eggHatch(pet);
				  hatch=true;
                //spawn velocirooster
			} else if (checkMoves()>=BUNNY) {
				  Bunny pet = new Bunny();
				  eggHatch(pet);
				  hatch=true;
				  //spawn bat
			  }	
		           
		  if (!hatch)	{
			  detach(Dungeon.hero.belongings.backpack);		 			 
			  GLog.w(TXT_YOLK);  			  
		  }
		  
		  hero.next();
		
		}
		
		else if (action == AC_SHAKE) {
						            
			 boolean alive = false;
			if (checkSummons()>=FAIRY) {
				GLog.w(TXT_ZAP);
				Dungeon.hero.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
				Dungeon.hero.sprite.flash();
				Dungeon.hero.damage(1, LightningTrap.LIGHTNING);
				alive = true;
				//spawn fairy
            } else if (checkBurns()>=VELOCIROOSTER) {
                GLog.w(TXT_SCRATCH);
                alive = true;
                //spawn velocirooster
			} else if (checkMoves()>=BUNNY) {
				  GLog.w(TXT_KICKS);
				  alive = true;
				  //spawn bunny
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
				Dungeon.hero.haspet=true;
				assignPet(pet);
				
		  } else {
			  
			  Dungeon.hero.spend(EasterEgg.TIME_TO_USE);
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
		return "A colorful egg of some creature. It's rather large and hard. Who knows what it contains?";
	}

}
