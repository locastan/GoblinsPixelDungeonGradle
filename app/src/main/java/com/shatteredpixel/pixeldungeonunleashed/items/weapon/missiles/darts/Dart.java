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

package com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.darts;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.melee.Crossbow;
import com.shatteredpixel.pixeldungeonunleashed.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Dart extends MissileWeapon {

    private Crossbow bow;
    public static final String AC_STAB	= "STAB";

	{
		name = "dart";
        image = ItemSpriteSheet.DART;
        MIN = min();
        MAX = max();
        STR = 9;
	}

	public int min() {
		return (bow != null ? 4 + bow.level : 1);
	}

	public int max() {
		return (bow != null ? 12 + 3*bow.level : 2);
	}

    public Dart() {
        this( 1 );
    }

    public Dart( int number ) {
        super();
        quantity = number;
    }

	@Override
	public String desc() {
		return
				"These simple metal spikes are weighted to fly true and " +
						"sting their prey with a flick of the wrist.\n\n" +
                        "You can also choose to stab yourself if you prefer.";
	}

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(AC_STAB);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        if (action.equals(AC_STAB)) {
            hero.spend(1f);
            // remove crossbow damage and enchantments from stabbing yourself.
            bow = null;
            int dmg = damageRoll(hero);
            proc(hero,hero,dmg);
            int effectiveDamage = hero.attackProc( hero, dmg );
            effectiveDamage = hero.defenseProc( hero, effectiveDamage );
            Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
            hero.damage(effectiveDamage,hero);
            // put crossbow back
            updateCrossbow();
        } else
            super.execute(hero, action);
    }
	
	public void updateCrossbow(){
		if (Dungeon.hero.belongings.weapon instanceof Crossbow){
			bow = (Crossbow) Dungeon.hero.belongings.weapon;
		} else {
			bow = null;
		}
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {
		if (bow != null && bow.enchantment != null){
			bow.enchantment.proc(bow, attacker, defender, damage);
		}
		super.proc(attacker, defender, damage);
	}
	
	@Override
	protected void onThrow(int cell) {
		updateCrossbow();
		super.onThrow(cell);
	}
	
	@Override
	public String info() {
		updateCrossbow();
		return super.info();
	}

	@Override
	public int damageRoll( Hero owner ) {
		return Random.NormalIntRange( min(), max() );
	}

    @Override
    public Item random() {
        quantity = Random.Int( 5, 15 );
        return this;
    }
	
	@Override
	public int price() {
		return 4 * quantity;
	}
}
