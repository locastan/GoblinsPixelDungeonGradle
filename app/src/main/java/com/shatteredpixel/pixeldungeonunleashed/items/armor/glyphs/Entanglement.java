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
package com.shatteredpixel.pixeldungeonunleashed.items.armor.glyphs;

import com.watabou.noosa.Camera;
import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Roots;
import com.shatteredpixel.pixeldungeonunleashed.effects.CellEmitter;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.EarthParticle;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.Armor;
import com.shatteredpixel.pixeldungeonunleashed.items.armor.Armor.Glyph;
import com.shatteredpixel.pixeldungeonunleashed.plants.Earthroot;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Entanglement extends Glyph {

	private static final String TXT_ENTANGLEMENT	= "%s of entanglement";
	private static final String TXT_DESCRIPTION = "This armor encases the defender in protective bands of energy, this can make it difficult to move though.";

	private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x448822 );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage ) {

		int level = Math.max( 0, armor.level );
		
		if (Random.Int( 4 ) == 0) {
			if (Random.Int( 2 ) == 0) {
				Buff.prolong(defender, Roots.class, 5 - level / 5);
				Buff.affect(defender, Earthroot.Armor.class).level(5 * (level + 1));
			} else {
				Buff.affect(defender, Earthroot.Armor.class).level(3 * (level + 1));
			}
			CellEmitter.bottom( defender.pos ).start( EarthParticle.FACTORY, 0.05f, 8 );
			Camera.main.shake( 1, 0.4f );
		}

		return damage;
	}

	@Override
	public String glyphDescription() { return TXT_DESCRIPTION; };

	@Override
	public String name( String weaponName) {
		return String.format( TXT_ENTANGLEMENT, weaponName );
	}

	@Override
	public Glowing glowing() {
		return GREEN;
	}
		
}
