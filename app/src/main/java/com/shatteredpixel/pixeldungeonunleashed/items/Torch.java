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
package com.shatteredpixel.pixeldungeonunleashed.items;

import java.util.ArrayList;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Blob;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Fire;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.scenes.CellSelector;
import com.shatteredpixel.pixeldungeonunleashed.scenes.GameScene;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Buff;
import com.shatteredpixel.pixeldungeonunleashed.actors.buffs.Light;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.effects.particles.FlameParticle;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSpriteSheet;

public class Torch extends Item {

	public static final String AC_LIGHT	= "LIGHT";
	public static final String AC_BURN	= "BURN";
	
	public static final float TIME_TO_LIGHT = 1;
	
	{
		name = "torch";
		image = ItemSpriteSheet.TORCH;
		
		stackable = true;
		
		defaultAction = AC_LIGHT;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_LIGHT );
		actions.add(AC_BURN);
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		
		if (action.equals( AC_LIGHT )) {
			
			hero.spend( TIME_TO_LIGHT );
			hero.busy();
			
			hero.sprite.operate( hero.pos );
			
			detach( hero.belongings.backpack );
			Buff.affect( hero, Light.class, Light.DURATION );
			
			Emitter emitter = hero.sprite.centerEmitter();
			emitter.start( FlameParticle.FACTORY, 0.2f, 3 );
			
		} else if (action.equals( AC_BURN )) {
            GameScene.selectCell( burnee );
		} else {
			
			super.execute( hero, action );
			
		}
	}

    private static CellSelector.Listener burnee = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null && (Level.passable[target] || Level.flamable[target])) {
				if (Level.adjacent(Dungeon.hero.pos, target)) {
					Item torch = Dungeon.hero.belongings.getItem(Torch.class);
					torch.detach(Dungeon.hero.belongings.backpack);
					Sample.INSTANCE.play( Assets.SND_BURNING );
					GameScene.add(Blob.seed(target, 1, Fire.class));
				} else {
                    GLog.w( "You can only burn stuff next to you." );
                }
            }
        }
        @Override
        public String prompt() {
            return "Select burn location";
        }
    };

	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int price() {
		return 10 * quantity;
	}
	
	@Override
	public String info() {
		return
			"An adventuring staple, when a dungeon goes dark, a torch can help lead the way." +
            "\n\nIt can also be used to burn stuff...";
	}
}
