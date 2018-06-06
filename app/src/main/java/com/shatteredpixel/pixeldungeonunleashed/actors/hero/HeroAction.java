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
package com.shatteredpixel.pixeldungeonunleashed.actors.hero;

import com.shatteredpixel.pixeldungeonunleashed.actors.Char;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.npcs.NPC;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.PET;


public class HeroAction {
	
	public int dst;
	
	public static class Move extends HeroAction {
		public Move( int dst ) {
			this.dst = dst;
		}
	}
	
	public static class PickUp extends HeroAction {
		public PickUp( int dst ) {
			this.dst = dst;
		}
	}
	
	public static class OpenChest extends HeroAction {
		public OpenChest( int dst ) {
			this.dst = dst;
		}
	}
	
	public static class Buy extends HeroAction {
		public Buy( int dst ) {
			this.dst = dst;
		}
	}
	
	public static class Interact extends HeroAction {
		public NPC npc;
		public Interact( NPC npc ) {
			this.npc = npc;
		}
	}

	public static class InteractPet extends HeroAction {
		public PET pet;
		public InteractPet(PET pet) {
			this.pet = pet;
		}
	}

	public static class Rummage extends HeroAction {
		public Rummage( int cell ) {
			this.dst = cell;
		}
	}
	
	public static class Unlock extends HeroAction {
		public Unlock( int door ) {
			this.dst = door;
		}
	}
	
	public static class Descend extends HeroAction {
		public Descend( int stairs ) {
			this.dst = stairs;
		}
	}
	
	public static class Ascend extends HeroAction {
		public Ascend( int stairs){
			this.dst = stairs;
		}
	}

	public static class Alchemy extends HeroAction {
		public Alchemy( int pot ) {
			this.dst = pot;
		}
	}

	public static class Donate extends HeroAction {
		public Donate( int altar ) { this.dst = altar; }
	}
	
	public static class Attack extends HeroAction {
		public Char target;
		public Attack( Char target ) {
			this.target = target;
		}
	}
}
