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


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.shatteredpixel.pixeldungeonunleashed.*;
import com.shatteredpixel.pixeldungeonunleashed.items.Honeypot;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.AnkhChain;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.PotionBandolier;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.ScrollHolder;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.SeedPouch;
import com.shatteredpixel.pixeldungeonunleashed.items.bags.WandHolster;
import com.shatteredpixel.pixeldungeonunleashed.items.potions.Potion;
import com.shatteredpixel.pixeldungeonunleashed.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.pixeldungeonunleashed.levels.traps.Trap;
import com.shatteredpixel.pixeldungeonunleashed.sprites.TrapSprite;
import com.shatteredpixel.pixeldungeonunleashed.ui.LootIndicator;
import com.shatteredpixel.pixeldungeonunleashed.ui.PetHealthIndicator;
import com.shatteredpixel.pixeldungeonunleashed.ui.ResumeIndicator;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.noosa.Visual;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.shatteredpixel.pixeldungeonunleashed.GoblinsPixelDungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.Actor;
import com.shatteredpixel.pixeldungeonunleashed.actors.blobs.Blob;
import com.shatteredpixel.pixeldungeonunleashed.actors.mobs.Mob;
import com.shatteredpixel.pixeldungeonunleashed.effects.BannerSprites;
import com.shatteredpixel.pixeldungeonunleashed.effects.BlobEmitter;
import com.shatteredpixel.pixeldungeonunleashed.effects.EmoIcon;
import com.shatteredpixel.pixeldungeonunleashed.effects.Flare;
import com.shatteredpixel.pixeldungeonunleashed.effects.FloatingText;
import com.shatteredpixel.pixeldungeonunleashed.effects.Ripple;
import com.shatteredpixel.pixeldungeonunleashed.effects.SpellSprite;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.Item;
import com.shatteredpixel.pixeldungeonunleashed.levels.Level;
import com.shatteredpixel.pixeldungeonunleashed.levels.RegularLevel;
import com.shatteredpixel.pixeldungeonunleashed.levels.features.Chasm;
import com.shatteredpixel.pixeldungeonunleashed.plants.Plant;
import com.shatteredpixel.pixeldungeonunleashed.sprites.CharSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.DiscardedItemSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.HeroSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.ItemSprite;
import com.shatteredpixel.pixeldungeonunleashed.sprites.PlantSprite;
import com.shatteredpixel.pixeldungeonunleashed.ui.AttackIndicator;
import com.shatteredpixel.pixeldungeonunleashed.ui.Banner;
import com.shatteredpixel.pixeldungeonunleashed.ui.BusyIndicator;
import com.shatteredpixel.pixeldungeonunleashed.ui.GameLog;
import com.shatteredpixel.pixeldungeonunleashed.ui.HealthIndicator;
import com.shatteredpixel.pixeldungeonunleashed.ui.QuickSlotButton;
import com.shatteredpixel.pixeldungeonunleashed.ui.StatusPane;
import com.shatteredpixel.pixeldungeonunleashed.ui.Toast;
import com.shatteredpixel.pixeldungeonunleashed.ui.Toolbar;
import com.shatteredpixel.pixeldungeonunleashed.ui.Window;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndBag.Mode;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndGame;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndBag;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndStory;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public class GameScene extends PixelScene {
	
	private static final String TXT_WELCOME			= "Welcome to the level %d of Goblins Pixel Dungeon!";
	private static final String TXT_WELCOME_BACK	= "Welcome back to the level %d of Goblins Pixel Dungeon!";
	
	private static final String TXT_CHASM	= "Your steps echo across the dungeon.";
	private static final String TXT_WATER	= "You hear water splashing around you.";
	private static final String TXT_GRASS	= "The smell of vegetation is thick in the air.";
	private static final String TXT_DARK	= "You can hear enemies moving in the darkness...";
	private static final String TXT_SECRETS	= "The atmosphere hints that this floor hides many secrets.";
	private static final String TXT_BURNT   = "The smell of burnt grass fills the air...";
	
	static GameScene scene;

	private SkinnedBlock water;
	private DungeonTilemap tiles;
	private FogOfWar fog;
	private HeroSprite hero;

	public static boolean freezeEmitters = false;

	private GameLog log;
	
	private BusyIndicator busy;
	
	private static CellSelector cellSelector;
	
	private Group terrain;
	private Group ripples;
	private Group plants;
	private Group traps;
	private Group heaps;
	private Group mobs;
	private Group emitters;
	private Group effects;
	private Group gases;
	private Group spells;
	private Group statuses;
	private Group emoicons;
	
	private Toolbar toolbar;
	private Toast prompt;

	private AttackIndicator attack;
	private LootIndicator loot;
	private ResumeIndicator resume;

	private boolean sceneCreated = false;
	public static PetHealthIndicator pethealth;
	
	@Override
	public void create() {
		
		Music.INSTANCE.play( Assets.TUNE, true );
		Music.INSTANCE.volume( 1f );
		
		GoblinsPixelDungeon.lastClass(Dungeon.hero.heroClass.ordinal());
		
		super.create();
		Camera.main.zoom( GameMath.gate(minZoom, defaultZoom + GoblinsPixelDungeon.zoom(), maxZoom));
		
		scene = this;
		
		terrain = new Group();
		add( terrain );
		
		water = new SkinnedBlock(
			Level.WIDTH * DungeonTilemap.SIZE,
			Level.HEIGHT * DungeonTilemap.SIZE,
			Dungeon.level.waterTex() );
		terrain.add( water );
		
		ripples = new Group();
		terrain.add( ripples );
		
		tiles = new DungeonTilemap();
		terrain.add( tiles );
		
		Dungeon.level.addVisuals(this);

		traps = new Group();
		add(traps);

		int size = Dungeon.level.traps.size();
		for (int i=0; i < size; i++) {
			addTrapSprite( Dungeon.level.traps.valueAt( i ) );
		}
		
		plants = new Group();
		add( plants );
		
		size = Dungeon.level.plants.size();
		for (int i=0; i < size; i++) {
			addPlantSprite( Dungeon.level.plants.valueAt( i ) );
		}
		
		heaps = new Group();
		add( heaps );
		
		size = Dungeon.level.heaps.size();
		for (int i=0; i < size; i++) {
			addHeapSprite( Dungeon.level.heaps.valueAt( i ) );
		}
		
		emitters = new Group();
		effects = new Group();
		emoicons = new Group();
		
		mobs = new Group();
		add( mobs );
		
		for (Mob mob : Dungeon.level.mobs) {
			addMobSprite( mob );
			if (Statistics.amuletObtained) {
				mob.beckon( Dungeon.hero.pos );
			}
		}
		
		add( emitters );
		add( effects );
		
		gases = new Group();
		add( gases );
		
		for (Blob blob : Dungeon.level.blobs.values()) {
			blob.emitter = null;
			addBlobSprite( blob );
		}
		
		fog = new FogOfWar( Level.WIDTH, Level.HEIGHT );
		fog.updateVisibility( Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped );
		add( fog );
		
		brightness( GoblinsPixelDungeon.brightness() );
		
		spells = new Group();
		add( spells );
		
		statuses = new Group();
		add( statuses );
		
		add( emoicons );
		
		hero = new HeroSprite();
		hero.place( Dungeon.hero.pos );
		hero.updateArmor();
		mobs.add( hero );

		
		add( new HealthIndicator() );
		add(pethealth = new PetHealthIndicator());
		
		add( cellSelector = new CellSelector( tiles ) );
		
		StatusPane sb = new StatusPane();
		sb.camera = uiCamera;
		sb.setSize( uiCamera.width, 0 );
		add( sb );
		
		toolbar = new Toolbar();
		toolbar.camera = uiCamera;
		toolbar.setRect( 0,uiCamera.height - toolbar.height(), uiCamera.width, toolbar.height() );
		add( toolbar );
		
		attack = new AttackIndicator();
		attack.camera = uiCamera;
		attack.setPos(
			uiCamera.width - attack.width(),
			toolbar.top() - attack.height() );
		add( attack );

		loot = new LootIndicator();
		loot.camera = uiCamera;
		add( loot );

		resume = new ResumeIndicator();
		resume.camera = uiCamera;
		add( resume );

		layoutTags();

		log = new GameLog();
		log.camera = uiCamera;
		log.setRect( 0, toolbar.top(), attack.left(),  0 );
		add( log );
		
		if (Dungeon.depth < Statistics.deepestFloor)
			GLog.i( TXT_WELCOME_BACK, Dungeon.depth );
		else
			GLog.i( TXT_WELCOME, Dungeon.depth );
		Sample.INSTANCE.play( Assets.SND_DESCEND );

        //TODO Make this call music fitting to level depth. :)
        //MusiGen.createMusic(null);
        //Music.INSTANCE.volume( 1f );

		switch (Dungeon.level.feeling) {
		case CHASM:
			GLog.w( TXT_CHASM );
			break;
		case WATER:
			GLog.w( TXT_WATER );
			break;
		case GRASS:
			GLog.w( TXT_GRASS );
			break;
		case DARK:
			GLog.w( TXT_DARK );
			break;
		case BURNT:
			GLog.w( TXT_BURNT );
			break;
		default:
		}
		if (Dungeon.level instanceof RegularLevel &&
			((RegularLevel)Dungeon.level).secretDoors > Random.IntRange( 4, 6 )) {
			GLog.w( TXT_SECRETS );
		}

		busy = new BusyIndicator();
		busy.camera = uiCamera;
		busy.x = 1;
		busy.y = sb.bottom() + 1;
		add( busy );
		
		switch (InterlevelScene.mode) {
		case RESURRECT:
			ScrollOfTeleportation.appear( Dungeon.hero, Dungeon.level.entrance );
			new Flare( 8, 32 ).color( 0xFFFF66, true ).show( hero, 2f ) ;
			break;
		case RETURN:
			ScrollOfTeleportation.appear(  Dungeon.hero, Dungeon.hero.pos );
			break;
		case FALL:
			Chasm.heroLand();
			break;
		case DESCEND:
			if (Dungeon.difficultyLevel != Dungeon.DIFF_ENDLESS) {
				switch (Dungeon.depth) {
					case 1:
						if (Dungeon.difficultyLevel == Dungeon.DIFF_TUTOR) {
							WndStory.showChapter(WndStory.ID_TUTOR_1);
						} else {
							final Calendar calendar = Calendar.getInstance();
							if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER && calendar.get(Calendar.WEEK_OF_MONTH) > 2) { // DSM-xxxx
								WndStory.showChapter(WndStory.ID_SPECIAL_1);
							} else {
								WndStory.showChapter(WndStory.ID_SEWERS);
							}
						}
						break;
					case 6:
						if (Dungeon.difficultyLevel == Dungeon.DIFF_TUTOR && !Dungeon.tutorial_boss_found) {
							Dungeon.tutorial_boss_found = true;
							WndStory.showChapter(WndStory.ID_TUTOR_2);
						}
						break;
					case 7:
						WndStory.showChapter(WndStory.ID_PRISON);
						break;
					case 13:
						WndStory.showChapter(WndStory.ID_CAVES);
						break;
					case 19:
						WndStory.showChapter(WndStory.ID_METROPOLIS);
						break;
					case 25:
						WndStory.showChapter(WndStory.ID_FROZEN);
						break;
					case 31:
						WndStory.showChapter(WndStory.ID_HALLS);
						break;
				}
			}
			if ((Dungeon.hero.isAlive() && Dungeon.depth != Level.MAX_DEPTH) || (Dungeon.difficultyLevel == Dungeon.DIFF_ENDLESS))  {
				Badges.validateNoKilling();
			}
			break;
		default:
		}

		ArrayList<Item> dropped = Dungeon.droppedItems.get( Dungeon.depth );
		if (dropped != null) {
			for (Item item : dropped) {
				int pos = Dungeon.level.randomRespawnCell();
				if (item instanceof Potion) {
					((Potion)item).shatter( pos );
				} else if (item instanceof Plant.Seed) {
					Dungeon.level.plant( (Plant.Seed)item, pos );
				} else if (item instanceof Honeypot) {
					Dungeon.level.drop(((Honeypot) item).shatter(null, pos), pos);
				} else {
					Dungeon.level.drop( item, pos );
				}
			}
			Dungeon.droppedItems.remove( Dungeon.depth );
		}

		Camera.main.target = hero;
		fadeIn();

		sceneCreated = true;
	}
	
	public void destroy() {
		
		freezeEmitters = false;

		scene = null;
		Badges.saveGlobal();
		
		super.destroy();
	}
	
	@Override
	public synchronized void pause() {
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
		} catch (IOException e) {
			//
		}
	}

	@Override
	public synchronized void update() {
		if (!sceneCreated){
			return;
		}

		if (Dungeon.hero == null) {
			return;
		}

		super.update();
		
		if (!freezeEmitters) water.offset( 0, -5 * Game.elapsed );
		
		Actor.process();
		
		if (Dungeon.hero.ready && !Dungeon.hero.paralysed) {
			log.newLine();
		}

		if (tagAttack != attack.active || tagLoot != loot.visible || tagResume != resume.visible) {

			boolean atkAppearing = attack.active && !tagAttack;
			boolean lootAppearing = loot.visible && !tagLoot;
			boolean resAppearing = resume.visible && !tagResume;

			tagAttack = attack.active;
			tagLoot = loot.visible;
			tagResume = resume.visible;

			if (atkAppearing || lootAppearing || resAppearing)
				layoutTags();
		}

		cellSelector.enable(Dungeon.hero.ready);
	}

	private boolean tagAttack    = false;
	private boolean tagLoot        = false;
	private boolean tagResume    = false;

	private void layoutTags() {

		float pos = tagAttack ? attack.top() : toolbar.top();

		if (tagLoot) {
			loot.setPos( uiCamera.width - loot.width(), pos - loot.height() );
			pos = loot.top();
		}

		if (tagResume) {
			resume.setPos(uiCamera.width - resume.width(), pos - resume.height());
		}
	}
	
	@Override
	protected void onBackPressed() {
		if (!cancel()) {
			add(new WndGame());
		}
	}
	
	@Override
	protected void onMenuPressed() {
		if (Dungeon.hero.ready) {
			selectItem( null, WndBag.Mode.ALL, null );
		}
	}
	
	public void brightness( boolean value ) {
		water.rm = water.gm = water.bm =
		tiles.rm = tiles.gm = tiles.bm =
			value ? 1.5f : 1.0f;
		if (value) {
			fog.am = +2f;
			fog.aa = -1f;
		} else {
			fog.am = +1f;
			fog.aa =  0f;
		}
	}
	
	private void addHeapSprite( Heap heap ) {
		ItemSprite sprite = heap.sprite = (ItemSprite)heaps.recycle( ItemSprite.class );
		sprite.revive();
		sprite.link( heap );
		heaps.add( sprite );
	}
	
	private void addDiscardedSprite( Heap heap ) {
		heap.sprite = (DiscardedItemSprite)heaps.recycle( DiscardedItemSprite.class );
		heap.sprite.revive();
		heap.sprite.link( heap );
		heaps.add(heap.sprite);
	}
	
	private void addPlantSprite( Plant plant ) {
		(plant.sprite = (PlantSprite)plants.recycle( PlantSprite.class )).reset( plant );
	}

	private void addTrapSprite( Trap trap ) {
		(trap.sprite = (TrapSprite)traps.recycle( TrapSprite.class )).reset( trap );
		trap.sprite.visible = trap.visible;
	}
	
	private void addBlobSprite( final Blob gas ) {
		if (gas.emitter == null) {
			gases.add( new BlobEmitter( gas ) );
		}
	}
	
	private void addMobSprite( Mob mob ) {
		CharSprite sprite = mob.sprite();
		sprite.visible = Dungeon.visible[mob.pos]; // DSM-xxxx this can crash...
		mobs.add(sprite);
		sprite.link(mob);
	}
	
	private void prompt( String text ) {
		
		if (prompt != null) {
			prompt.killAndErase();
			prompt = null;
		}
		
		if (text != null) {
			prompt = new Toast( text ) {
				@Override
				protected void onClose() {
					cancel();
				}
			};
			prompt.camera = uiCamera;
			prompt.setPos( (uiCamera.width - prompt.width()) / 2, uiCamera.height - 60 );
			add( prompt );
		}
	}
	
	private void showBanner( Banner banner ) {
		banner.camera = uiCamera;
		banner.x = align( uiCamera, (uiCamera.width - banner.width) / 2 );
		banner.y = align( uiCamera, (uiCamera.height - banner.height) / 3 );
		add( banner );
	}
	
	// -------------------------------------------------------
	
	public static void add( Plant plant ) {
		if (scene != null) {
			scene.addPlantSprite( plant );
		}
	}

	public static void add( Trap trap ) {
		if (scene != null) {
			scene.addTrapSprite( trap );
		}
	}
	
	public static void add( Blob gas ) {
		Actor.add( gas );
		if (scene != null) {
			scene.addBlobSprite( gas );
		}
	}
	
	public static void add( Heap heap ) {
		if (scene != null) {
			scene.addHeapSprite( heap );
		}
	}
	
	public static void discard( Heap heap ) {
		if (scene != null) {
			scene.addDiscardedSprite( heap );
		}
	}
	
	public static void add( Mob mob ) {
		Dungeon.level.mobs.add( mob );
		Actor.add( mob );
		scene.addMobSprite( mob );
	}
	
	public static void add( Mob mob, float delay ) {
		Dungeon.level.mobs.add( mob );
		Actor.addDelayed( mob, delay );
		scene.addMobSprite( mob );
	}
	
	public static void add( EmoIcon icon ) {
		scene.emoicons.add( icon );
	}
	
	public static void effect( Visual effect ) {
		scene.effects.add( effect );
	}
	
	public static Ripple ripple( int pos ) {
		Ripple ripple = (Ripple)scene.ripples.recycle( Ripple.class );
		ripple.reset( pos );
		return ripple;
	}
	
	public static SpellSprite spellSprite() {
		return (SpellSprite)scene.spells.recycle( SpellSprite.class );
	}
	
	public static Emitter emitter() {
		if (scene != null) {
			Emitter emitter = (Emitter)scene.emitters.recycle( Emitter.class );
			emitter.revive();
			return emitter;
		} else {
			return null;
		}
	}
	
	public static FloatingText status() {
		return scene != null ? (FloatingText)scene.statuses.recycle( FloatingText.class ) : null;
	}
	
	public static void pickUp( Item item ) {
		scene.toolbar.pickup( item );
	}
	
	public static void updateMap() {
		if (scene != null) {
			scene.tiles.updated.set( 0, 0, Level.WIDTH, Level.HEIGHT );
		}
	}
	
	public static void updateMap( int cell ) {
		if (scene != null) {
			scene.tiles.updated.union( cell % Level.WIDTH, cell / Level.WIDTH );
		}
	}
	
	public static void discoverTile( int pos, int oldValue ) {
		if (scene != null) {
			scene.tiles.discover( pos, oldValue );
		}
	}
	
	public static void show( Window wnd ) {
		cancelCellSelector();
		scene.add( wnd );
	}
	
	public static void afterObserve() {
		if (scene != null) {
			scene.fog.updateVisibility( Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped );
			
			for (Mob mob : Dungeon.level.mobs) {
				if(mob.sprite != null) {
					mob.sprite.visible = Dungeon.visible[mob.pos];
				}
			}
		}
	}
	
	public static void flash( int color ) {
		scene.fadeIn( 0xFF000000 | color, true );
	}
	
	public static void gameOver() {
		Banner gameOver = new Banner( BannerSprites.get( BannerSprites.Type.GAME_OVER ) );
		gameOver.show( 0x000000, 1f );
		scene.showBanner( gameOver );
		
		Sample.INSTANCE.play( Assets.SND_DEATH );
	}
	
	public static void bossSlain() {
		if (Dungeon.hero.isAlive()) {
			Banner bossSlain = new Banner( BannerSprites.get( BannerSprites.Type.BOSS_SLAIN ) );
			bossSlain.show( 0xFFFFFF, 0.3f, 5f );
			scene.showBanner( bossSlain );
			
			Sample.INSTANCE.play( Assets.SND_BOSS );
		}
	}
	
	public static void handleCell( int cell ) {
		cellSelector.select( cell );
	}
	
	public static void selectCell( CellSelector.Listener listener ) {
		cellSelector.listener = listener;
		scene.prompt( listener.prompt() );
	}
	
	private static boolean cancelCellSelector() {
		if (cellSelector != null && cellSelector.listener != null && cellSelector.listener != defaultCellListener) {
			cellSelector.cancel();
			return true;
		} else {
			return false;
		}
	}
	
	public static WndBag selectItem( WndBag.Listener listener, WndBag.Mode mode, String title ) {
		cancelCellSelector();
		
		WndBag wnd =
				mode == Mode.SEED ?
					WndBag.getBag( SeedPouch.class, listener, mode, title ) :
				mode == Mode.SCROLL ?
					WndBag.getBag( ScrollHolder.class, listener, mode, title ) :
				mode == Mode.POTION ?
					WndBag.getBag( PotionBandolier.class, listener, mode, title ) :
				mode == Mode.WAND ?
					WndBag.getBag( WandHolster.class, listener, mode, title ) :
				mode == Mode.KEY ?
						WndBag.getBag( AnkhChain.class, listener, mode, title ) :
				WndBag.lastBag( listener, mode, title );

		scene.add( wnd );
		
		return wnd;
	}
	
	static boolean cancel() {
		if (Dungeon.hero.curAction != null || Dungeon.hero.resting) {
			
			Dungeon.hero.curAction = null;
			Dungeon.hero.resting = false;
			return true;
			
		} else {
			
			return cancelCellSelector();
			
		}
	}
	
	public static void ready() {
		selectCell( defaultCellListener );
		QuickSlotButton.cancel();
	}
	
	private static final CellSelector.Listener defaultCellListener = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			if (Dungeon.hero.handle( cell )) {
				Dungeon.hero.next();
			}
		}
		@Override
		public String prompt() {
			return null;
		}
	};
}
