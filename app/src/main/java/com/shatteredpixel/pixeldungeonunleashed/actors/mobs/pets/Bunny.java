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
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Paralysis;
import com.shatteredpixel.pixeldungeonunleashed.sprites.BunnySprite;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.utils.Random;

public class Bunny extends PET{
	
	{
		name = "bunny";
		spriteClass = BunnySprite.class;       
		state = HUNTING;
		level = 1;
		type = 9;
		cooldown=1000;
	}
	protected int regen = 20;	
	protected float regenChance = 0.1f;	
		

	@Override
	public void adjustStats(int level) {
		this.level = level;
		HT = (2 + level) * 10;
		defenseSkill = 1 + level*4;
	}
	


	@Override
	public int dr(){
		return level*5;
	}
	
	@Override
	public int attackSkill(Char target) {
		return defenseSkill;
	}

	@Override
	public int damageRoll() {
		
		int dmg;
		if (cooldown==0){
			dmg=Random.NormalIntRange(HT, HT*2); 
			yell("Bite!");
			cooldown=1000;
		} else {
			dmg=Random.NormalIntRange(HT/5, HT/2) ;
		}
		return dmg;
			
	}

	@Override
	protected boolean act() {
		
		if (cooldown>0){
			cooldown=Math.max(cooldown-(level*level),0);
			if (cooldown==0) {yell("ROAR!");}
		}
		
		if (Random.Float()<regenChance && HP<HT){HP+=regen; super.checkHP();}

		return super.act();
	}			
	

@Override
public String description() {
	return "Death by sharp pointy fangs!";
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



}