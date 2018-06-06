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
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.MagicalSleep;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Ooze;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Poison;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Sleep;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Terror;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Venom;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Vertigo;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Acidic;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.AirElemental;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.RottingFist;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.SlimeRed;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Statue;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Wraith;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.LightningTrap;
import com.shatteredpixel.pixeldungeonunleashed.mechanics.Ballistica;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CharSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.PoisonKlikSprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class PoisonKlik extends PET implements Callback{
	
	{
		name = "poison klik";
		spriteClass = PoisonKlikSprite.class;
		flying=true;
		state = HUNTING;
		level = 1;
		type = 6;
		cooldown=1000;

	}
	private static final float TIME_TO_ZAP = 1f;

	//Frames 1-4 are idle, 5-8 are moving, 9-12 are attack and the last are for death 

	//flame on!
	//spits fire
	//feed meat
			
	protected int regen = 1;	
	protected float regenChance = 0.2f;	
	
	@Override
	protected float attackDelay() {
		return 0.8f;
	}
	
	

	@Override
	public int dr(){
		return level*5;
	}
		

	@Override
	public void adjustStats(int level) {
		this.level = level;
		HT = (level) * 14;
		defenseSkill = 3 + level*level;
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
			if (cooldown==0) {GLog.w("Your klik friend is dripping poison!");}
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

			return super.doAttack(enemy);

		} else {

			boolean visible = Level.fieldOfView[pos]
					|| Level.fieldOfView[enemy.pos];
			if (visible) {
				((PoisonKlikSprite) sprite).zap(enemy.pos);
			} else {
				zap();
			}

			return !visible;
		}
	}

	
	private void zap() {
		spend(TIME_TO_ZAP);

		cooldown = super.calccooldown(1000, this.level);
		yell("KLIK!");
		
		if (hit(this, enemy, true)) {			

			int dmg = damageRoll()*2;
			enemy.damage(dmg, this);
			
			Buff.affect(enemy,Poison.class).set(Poison.durationFactor(enemy) * (level + 1));
			
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
	return "A freshly hatched green venomous klik. Do not lick its skin!\n\n"+
			"Will in turn lick anything poisonous or yucky...";
}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Sleep.class );
		IMMUNITIES.add( Terror.class );
		IMMUNITIES.add( Poison.class );
        IMMUNITIES.add( Venom.class );
        IMMUNITIES.add( Ooze.class );
		IMMUNITIES.add( Vertigo.class );
		IMMUNITIES.add( Acidic.class );
        IMMUNITIES.add( SlimeRed.class );
        IMMUNITIES.add( RottingFist.class );
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}

    private static final HashSet<Class<?>> VULNERABLE = new HashSet<Class<?>>();
    static {
        VULNERABLE.add( LightningTrap.Electricity.class );
        VULNERABLE.add(AirElemental.class );
        VULNERABLE.add( Statue.class );
        VULNERABLE.add( Wraith.class );
    }

    @Override
    public HashSet<Class<?>> vulnerable() {
        return VULNERABLE;
    }

}