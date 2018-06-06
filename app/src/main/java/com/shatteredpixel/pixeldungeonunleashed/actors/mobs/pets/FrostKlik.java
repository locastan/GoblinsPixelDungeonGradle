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
package com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Freezing;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Burning;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Frost;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.MagicalSleep;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Terror;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.BurningFist;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Elemental;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.IceDemon;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.LostSoul;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Yeti;
import com.shatteredpixel.pixeldungeonunleashed.effects.CellEmitter;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.SnowParticle;
import com.shatteredpixel.pixeldungeonunleashed.items.wands.WandOfFireblast;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.mechanics.Ballistica;
import com.shatteredpixel.pixeldungeonunleashed.sprites.FrostKlikSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CharSprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.Calendar;
import java.util.HashSet;

public class FrostKlik extends PET implements Callback{
	
	{
		name = "frost klik";
		spriteClass = FrostKlikSprite.class;
		flying=true;
		state = HUNTING;
		level = 1;
		type = 7;
		cooldown=1000;

	}
	private static final float TIME_TO_ZAP = 1f;
	final Calendar calendar = Calendar.getInstance();

	//Frames 1-4 are idle, 5-8 are moving, 9-12 are attack and the last are for death 

	//flame on!
	//spits fire
	//feed meat
	

	@Override
	public int dr(){
		return level*4;
	}
			
	protected int regen = 1;	
	protected float regenChance = 0.1f;	
		

	@Override
	public void adjustStats(int level) {
		this.level = level;
		HT = (3 + level) * 12;
		defenseSkill = 1 + level*level;
		cooldown = super.calccooldown(1000, this.level);
	}
	

	@Override
	public int attackSkill(Char target) {
		return defenseSkill;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(HP / 5, HP / 2);
	}

	@Override
	protected boolean act() {
		
		if (cooldown>0){
			cooldown--;
			if (cooldown==0) {
				
				if (calendar.get(Calendar.MONTH)==11) {
					GLog.w("Klik?* - (\"Do you want to build a snow man?\")");
				} else {
					GLog.w("The air grows chilly around your frosty friend!");
				}
			}
		}
		
		if (Random.Float()<regenChance && HP<HT){HP+=regen; super.checkHP();}

		return super.act();
	}
	
	
	@Override
	protected boolean canAttack(Char enemy) {
		if (cooldown>0){
		  return Level.adjacent(pos, enemy.pos);
		} else {
		  return Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos;
		}
	}

	@Override
	protected boolean doAttack(Char enemy) {

		if (Level.adjacent(pos, enemy.pos)) {

			CellEmitter.get(pos).start(SnowParticle.FACTORY, 0.2f, 4);
			return super.doAttack(enemy);

		} else {

			boolean visible = Level.fieldOfView[pos]
					|| Level.fieldOfView[enemy.pos];
			if (visible) {
				((FrostKlikSprite) sprite).zap(enemy.pos);
			} else {
				zap();
			}

			return !visible;
		}
	}

	
	private void zap() {
		spend(TIME_TO_ZAP);

        cooldown = super.calccooldown(1000, this.level);
		if (calendar.get(Calendar.MONTH)==11) {
			yell("KLIK!* - (\"Look! It's beautiful!\"");
		} else {
			yell("KLIK!");
		}
		
		if (hit(this, enemy, true)) {			

			int dmg = damageRoll()*2;
			enemy.damage(dmg, this);
			
			Buff.prolong(enemy, Frost.class, Frost.duration(enemy)* Random.Float(1f, 1.5f));
			CellEmitter.get(enemy.pos).start(SnowParticle.FACTORY, 0.2f, 6);
			
		} else {
			enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
		}
		
	}

	public void onZapComplete() {
		zap();
		next();
	}

	@Override
	public void call() {
		next();
	}
	
	
	
	@Override
	public void interact() {

		if (this.buff(MagicalSleep.class) != null) {
			Buff.detach(this, MagicalSleep.class);
		}
		
		if (state == SLEEPING) {
			state = HUNTING;
		}
		if (buff(Paralysis.class) != null) {
			Buff.detach(this, Paralysis.class);
			GLog.i("You shake your %s out of paralysis.", name);
		}
		
		int curPos = pos;

		moveSprite(pos, Dungeon.hero.pos);
		move(Dungeon.hero.pos);

		Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
		Dungeon.hero.move(curPos);

		Dungeon.hero.spend(1 / Dungeon.hero.speed());
		Dungeon.hero.busy();
	}


@Override
public String description() {
	return "A freshly hatched frosty klik. Super cool!\n\n"+
			"Anything frozen is its favourite.";
}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Freezing.class );
		IMMUNITIES.add( Terror.class );
        IMMUNITIES.add( Yeti.class );
        IMMUNITIES.add( IceDemon.class );
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}

	private static final HashSet<Class<?>> VULNERABLE = new HashSet<Class<?>>();
	static {
        VULNERABLE.add( Burning.class );
        VULNERABLE.add( WandOfFireblast.class );
        VULNERABLE.add( LostSoul.class );
        VULNERABLE.add( BurningFist.class );
        VULNERABLE.add( Elemental.class );
	}

	@Override
	public HashSet<Class<?>> vulnerable() {
		return VULNERABLE;
	}
}