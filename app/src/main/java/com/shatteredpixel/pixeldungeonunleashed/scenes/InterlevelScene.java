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
package com.shatteredpixel.pixeldungeonunleashed.scenes;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.Statistics;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.pets.PET;
import com.shatteredpixel.pixeldungeonunleashed.items.Generator;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndError;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndStory;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;

import java.io.FileNotFoundException;
import java.io.IOException;

public class InterlevelScene extends PixelScene {

	private static final float TIME_TO_FADE = 0.3f;
	
	private static final String TXT_DESCENDING	= "Descending...";
	private static final String TXT_ASCENDING	= "Ascending...";
	private static final String TXT_LOADING		= "Loading...";
	private static final String TXT_SAVING		= "Saving...";
	private static final String TXT_RESURRECTING= "Resurrecting...";
	private static final String TXT_RETURNING	= "Returning...";
    private static final String TXT_WARPING	= "Warping...";
	private static final String TXT_FALLING		= "Falling...";
	
	private static final String ERR_FILE_NOT_FOUND	= "Save file not found. If this error persists after restarting, " +
														"it may mean this save game is corrupted. Sorry about that.";
	private static final String ERR_IO			    = "Cannot read save file. If this error persists after restarting, " +
														"it may mean this save game is corrupted. Sorry about that.";
	
	public enum Mode {
		DESCEND, ASCEND, CONTINUE, SAVE, RESURRECT, RETURN, WARP, FALL
	}
	public static Mode mode;
	
	public static int returnDepth;
	public static int returnPos;
	
	public static boolean noStory = false;

	public static boolean fallIntoPit;
	
	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	}
	private Phase phase;
	private float timeLeft;
	
	private BitmapText message;
	
	private Thread thread;
	private Exception error = null;
	
	@Override
	public void create() {
		super.create();

		String text = "";
		switch (mode) {
		case DESCEND:
			text = TXT_DESCENDING;
			break;
		case ASCEND:
			text = TXT_ASCENDING;
			break;
		case CONTINUE:
			text = TXT_LOADING;
			break;
		case SAVE:
			text = TXT_SAVING;
			break;
		case RESURRECT:
			text = TXT_RESURRECTING;
			break;
		case RETURN:
			text = TXT_RETURNING;
			break;
        case WARP:
            text = TXT_WARPING;
            break;
		case FALL:
			text = TXT_FALLING;
			break;
		}
		
		message = PixelScene.createText( text, 9, true );
		message.measure();
		message.x = (Camera.main.width - message.width()) / 2;
		message.y = (Camera.main.height - message.height()) / 2;
		add( message );
		
		phase = Phase.FADE_IN;
		timeLeft = TIME_TO_FADE;

		thread = new Thread() {
			@Override
			public void run() {
				
				try {
					
					Generator.reset();

					switch (mode) {
					case DESCEND:
						descend();
						break;
					case ASCEND:
						ascend();
						break;
					case CONTINUE:
						restore();
						break;
					case SAVE:
						restore();
						break;
					case RESURRECT:
						resurrect();
						break;
					case RETURN:
						returnTo();
						break;
                    case WARP:
                        warpTo();
                        break;
					case FALL:
						fall();
						break;
					}

					if ((Dungeon.bossLevel())) {
						Sample.INSTANCE.load( Assets.SND_BOSS );
					}
					
				} catch (Exception e) {
					
					error = e;

				}

				if (phase == Phase.STATIC && error == null) {
					phase = Phase.FADE_OUT;
					timeLeft = TIME_TO_FADE;
				}
			}
		};
		thread.start();
	}
	
	@Override
	public void update() {
		super.update();
		
		float p = timeLeft / TIME_TO_FADE;
		
		switch (phase) {
		
		case FADE_IN:
			message.alpha( 1 - p );
			if ((timeLeft -= Game.elapsed) <= 0) {
				if (!thread.isAlive() && error == null) {
					phase = Phase.FADE_OUT;
					timeLeft = TIME_TO_FADE;
				} else {
					phase = Phase.STATIC;
				}
			}
			break;
			
		case FADE_OUT:
			message.alpha( p );

			if (mode == Mode.CONTINUE || (mode == Mode.DESCEND && Dungeon.depth == 1)) {
				Music.INSTANCE.volume( p );
			}
			if ((timeLeft -= Game.elapsed) <= 0) {
				Game.switchScene( GameScene.class );
			}
			break;
			
		case STATIC:
			if (error != null) {
				String errorMsg;
				if (error instanceof FileNotFoundException) errorMsg = ERR_FILE_NOT_FOUND;
				else if (error instanceof IOException) errorMsg = ERR_IO;

				else throw new RuntimeException("fatal error occured while moving between floors", error);

				add( new WndError( errorMsg ) {
					public void onBackPressed() {
						super.onBackPressed();
						Game.switchScene( StartScene.class );
					}
				} );
				error = null;
			}
			break;
		}
	}

	private void descend() throws IOException {

		Actor.fixTime();
		if (Dungeon.hero == null) {
			Dungeon.init();
			if (noStory) {
				Dungeon.chapters.add( WndStory.ID_SEWERS );
				noStory = false;
			}
		} else {
			Dungeon.saveLevel();
		}

		Level level;
        if (Dungeon.difficultyLevel == Dungeon.DIFF_ENDLESS || Dungeon.depth >= Statistics.deepestFloor) {
            level = Dungeon.newLevel();
        } else {
            Dungeon.depth++;
            level = Dungeon.loadLevel( Dungeon.hero.heroClass );
        }
		Dungeon.switchLevel( level, level.entrance );
	}
	
	private void fall() throws IOException {

		Actor.fixTime();
		Dungeon.saveLevel();

		Level level;
		if (Dungeon.difficultyLevel == Dungeon.DIFF_ENDLESS || Dungeon.depth >= Statistics.deepestFloor) {
			level = Dungeon.newLevel();
		} else {
			Dungeon.depth++;
			level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		}
		if (Dungeon.bossLevel(Dungeon.depth)) {
			Dungeon.switchLevel(level, level.entrance);
		} else if (Dungeon.specialLevel(Dungeon.depth)) {
			Dungeon.switchLevel(level, level.randomRespawnCell());
		} else {
			Dungeon.switchLevel(level, fallIntoPit ? level.pitCell() : level.randomRespawnCell());
		}
	}
	
	private void ascend() throws IOException {
		Actor.fixTime();
		
		Dungeon.saveLevel();
		Dungeon.depth--;
		Level level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		Dungeon.switchLevel( level, level.exit );
	}
	
	private void returnTo() throws IOException {
		checkPetPort();
		Actor.fixTime();
		
		Dungeon.saveLevel();
		Dungeon.depth = returnDepth;
		Level level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		Dungeon.switchLevel( level, returnPos ); //Level.resizingNeeded ? level.adjustPos( returnPos ) : returnPos );
	}

    private void warpTo() throws IOException {
        checkPetPort();
        Actor.fixTime();

        Dungeon.saveLevel();
        Dungeon.depth = returnDepth;
        Level level = Dungeon.newLevel();
        Dungeon.switchLevel( level, level.entrance ); //Level.resizingNeeded ? level.adjustPos( returnPos ) : returnPos );
    }
	
	private void restore() throws IOException {
		
		Actor.fixTime();
		
		Dungeon.loadGame( StartScene.curClass );
		if (Dungeon.depth == -1) {
			Dungeon.depth = Statistics.deepestFloor;
			Dungeon.switchLevel( Dungeon.loadLevel( StartScene.curClass ), -1 );
		} else {
			Level level = Dungeon.loadLevel( StartScene.curClass );
			Dungeon.switchLevel( level, Dungeon.hero.pos ); //Level.resizingNeeded ? level.adjustPos( Dungeon.hero.pos ) : Dungeon.hero.pos );
		}
	}
	
	private void resurrect() throws IOException {
		
		Actor.fixTime();
		
		if (Dungeon.level.locked) {
			Dungeon.hero.resurrect( Dungeon.depth );
			Dungeon.depth--;
			Level level = Dungeon.newLevel();
			Dungeon.switchLevel( level, level.entrance );
		} else {
			Dungeon.hero.resurrect( -1 );
			Dungeon.resetLevel();
		}
	}

	private PET checkpet(){
		for (Mob mob : Dungeon.level.mobs) {
			if(mob instanceof PET) {
				return (PET) mob;
			}
		}
		return null;
	}

	private boolean checkpetNear(){
		for (int n : Level.NEIGHBOURS8) {
			int c =  Dungeon.hero.pos + n;
			if (Actor.findChar(c) instanceof PET) {
				return true;
			}
		}
		return false;
	}

	private void checkPetPort(){
		PET pet = checkpet();
		if(pet!=null && checkpetNear()){
			//GLog.i("I see pet");
			Dungeon.hero.petType=pet.type;
			Dungeon.hero.petLevel=pet.level;
			Dungeon.hero.petKills=pet.kills;
			Dungeon.hero.petHP=pet.HP;
			Dungeon.hero.petExperience=pet.experience;
			Dungeon.hero.petCooldown=pet.cooldown;
			pet.destroy();
			Dungeon.hero.petfollow=true;
		} else if (Dungeon.hero.haspet && Dungeon.hero.petfollow) {
			Dungeon.hero.petfollow=true;
		} else {
			Dungeon.hero.petfollow=false;
		}

	}
	
	@Override
	protected void onBackPressed() {
		//Do nothing
	}
}
