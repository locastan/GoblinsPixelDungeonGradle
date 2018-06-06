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
package com.shatteredpixel.pixeldungeonunleashed.actors.buffs;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Badges;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.ResultDescriptions;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.BodFly;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Skeleton;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Zombie;
import com.shatteredpixel.pixeldungeonunleashed.effects.CellEmitter;
import com.shatteredpixel.pixeldungeonunleashed.effects.Speck;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.BloodParticle;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.PoisonParticle;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.ui.BuffIndicator;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Infested extends Buff implements Hero.Doom {

	private int damage = 1;
    protected float left;

	private static final String DAMAGE	= "damage";

	{
		type = buffType.NEGATIVE;
	}

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target) && target.sprite != null){
			CellEmitter.center(target.pos).burst(BloodParticle.BURST, 3 );
			return true;
		} else
			return false;
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( DAMAGE, damage );

	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		damage = bundle.getInt( DAMAGE );
	}

	public void set(float duration, int damage) {
        this.left = duration;
		this.damage = damage;
	}

	@Override
	public int icon() {
		return BuffIndicator.INFESTED;
	}

	@Override
	public String toString() {
		return "Infested";
	}

	@Override
	public String desc() {
		return "A Bod Fly has laid eggs into your skin.\n" +
				"\n" +
				"As the larva grows inside you, you gradually loose hitpoints until either you or the larva is killed, or it hatches.\n" +
				"\n" +
				"The larva will grow for " + dispTurns(left) + ", and is currently feasting for " + damage + " damage.";
	}

	@Override
	public boolean act() {
		if (target.isAlive()) {
			target.damage(damage, this);
			if (damage < ((target.HT/3)))
				damage += (target.HT/18) ;

			//want it to act after the fly it came from.
			spend( TICK+0.1f );
			if ((left -= TICK) <= 0) {
				hatch();
			}
		} else {
			hatch();
		}

		return true;
	}

    private boolean hatch() {
        Mob DUMMY = new Mob() {};

        Sample.INSTANCE.play(Assets.SND_BEE);
        GLog.w("A bod fly has hatched!");

        int nMobs = 1;

        ArrayList<Integer> candidates = new ArrayList<>();

        for (int i = 0; i < Level.NEIGHBOURS8.length; i++) {
            int p = target.pos + Level.NEIGHBOURS8[i];
            if (Actor.findChar( p ) == null && (Level.passable[p] || Level.avoid[p])) {
                candidates.add( p );
            }
        }

        ArrayList<Integer> respawnPoints = new ArrayList<>();

        while (nMobs > 0 && candidates.size() > 0) {
            int index = Random.index( candidates );

            DUMMY.pos = candidates.get( index );

            respawnPoints.add( candidates.remove( index ) );
            nMobs--;
        }

        for (Integer point : respawnPoints) {
            Mob mob = new BodFly();
            if (Dungeon.difficultyLevel == Dungeon.DIFF_ENDLESS) {
                mob.infiniteScaleMob(Dungeon.depth + 5);
            } else {
                mob.scaleMob();
            }

            mob.pos = point;
            mob.state = mob.WANDERING;
            GameScene.add( mob);
            CellEmitter.center(mob.pos).burst(BloodParticle.BURST, 5 );
        }
        detach();
        return true;
    }


	@Override
	public void onDeath() {
		Badges.validateDeathByInfestation();

		Dungeon.fail( ResultDescriptions.INFEST );
		GLog.n( "You died from infestation..." );
	}
}
