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
package com.shatteredpixel.pixeldungeonunleashed.windows;


import android.util.Log;

import com.shatteredpixel.pixeldungeonunleashed.Assets;
import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.GoblinsPixelDungeon;
import com.shatteredpixel.pixeldungeonunleashed.actors.hero.Hero;
import com.shatteredpixel.pixeldungeonunleashed.items.Heap;
import com.shatteredpixel.pixeldungeonunleashed.items.artifacts.HummingTool;
import com.shatteredpixel.pixeldungeonunleashed.items.keys.Key;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.scenes.PixelScene;
import com.shatteredpixel.pixeldungeonunleashed.ui.RedButton;
import com.shatteredpixel.pixeldungeonunleashed.ui.Window;
import com.shatteredpixel.pixeldungeonunleashed.utils.GLog;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Sample;


public class WndLockpick extends Window {

	private final int WIDTH        = 112;
	private final int HEIGHT_P    = 120;
	private final int HEIGHT_L    = 120;

    public Image[] gear = new Image[6];
    public Integer[] step = new Integer[6];
    public Integer[][] affects = new Integer[6][6];
    protected TouchArea[] hotArea = new TouchArea[6];

    private boolean solved = false;

    private Integer thecell;
    private Hero thehero;
    private HummingTool hummingtool;
    private RedButton autosolve;


	private final String TXT_TITLE	= "Lock Picking";

	private BitmapText txtTitle;
    private BitmapText txtCharge;
    private BitmapText txtSeed;

	public WndLockpick(Hero hero, Integer cell) {

		super();
        thecell = cell;
        Log.e("Cell Number: ", String.valueOf(cell));
        thehero = hero;
        hummingtool = hero.belongings.getItem(HummingTool.class);
		resize(WIDTH, GoblinsPixelDungeon.landscape() ? HEIGHT_L : HEIGHT_P);

		txtTitle = PixelScene.createText(TXT_TITLE, 9, true);
		txtTitle.hardlight(Window.TITLE_COLOR);
		txtTitle.measure();
		txtTitle.x = PixelScene.align(PixelScene.uiCamera, (WIDTH - txtTitle.width()) / 2);
		add(txtTitle);

        BitmapTextMultiline info = PixelScene.createMultiline( 5, false );
        add( info );
        info.text( "Click the gears to align the gaps on top." );
        info.maxWidth = WIDTH;
        info.measure();
        info.x = 2;
        info.y = 10;

        txtCharge = PixelScene.createText("Tool Charge: " + hummingtool.charge(), 5, false);
        txtCharge.hardlight(Window.TITLE_COLOR);
        txtCharge.measure();
        txtCharge.x = 50 - txtCharge.width();
        txtCharge.y = 60;
        add(txtCharge);

        // For debug reasons display the seed number on bottom left corner.
        txtSeed = PixelScene.createText(String.valueOf(cell), 3, false);
        txtSeed.hardlight(0xFFCC00);
        txtSeed.measure();
        txtSeed.x = 2;
        txtSeed.y = 115 - txtSeed.height();
        add(txtSeed);

        Image Humm = new Image( Assets.HUMM );
        Humm.x = 48;
        Humm.y = 40;
        Humm.flipHorizontal = true;
        add(Humm);

        autosolve = new RedButton( "Auto-Solve" ) {
            @Override
            protected void onClick() {
                Autosolve();
                hide();
            }
        };
        checkToolCharge();
        autosolve.setRect( 4, 70, 50, 14 );
        add( autosolve );

        int target = Dungeon.level.map[cell];
        Heap heap = Dungeon.level.heaps.get( cell );

        if (target == Terrain.LOCKED_DOOR || (heap != null && (heap.type != Heap.Type.HEAP && heap.type != Heap.Type.FOR_SALE)) && heap.type == Heap.Type.LOCKED_CHEST) {
        // Standard Door or Chest
            if (isBetween(Dungeon.depth,1,6)) {
                // Simple locks for Sewers
                Log.e("Simple Locks", String.valueOf(crosssum(cell)));
                if (cell % 2 == 0) {
                    addGear(1, 30, 0x009933, new Integer[]{2});
                    addGear(2, -60, 0xDD0000, new Integer[]{});
                    setPuzzle(new Integer[]{6, crosssum(cell)*3});
                } else {
                    addGear(1, 90, 0x009933, new Integer[]{});
                    addGear(2, 180, 0xDD0000, new Integer[]{1});
                    setPuzzle(new Integer[]{crosssum(cell)*5, Dungeon.depth});
                }
            }

            if (isBetween(Dungeon.depth,7,12)) {
                // Simple, but more diverse locks for Prison
                if (cell % 3 == 0) {
                    addGear(1, 30, 0x009933, new Integer[]{2});
                    addGear(2, -60, 0xDD0000, new Integer[]{});
                    setPuzzle(new Integer[]{5, crosssum(cell)*3});
                } else if (cell % 2 == 0) {
                    addGear(1, 90, 0x009933, new Integer[]{2});
                    addGear(2, 180, 0xDD0000, new Integer[]{});
                    addGear(3, 45,0x00CCFF, new Integer[]{1,2});
                    setPuzzle(new Integer[]{crosssum(cell)*7, 1, crosssum(cell)*3});
                } else {
                    addGear(1, 90, 0x009933, new Integer[]{});
                    addGear(2, 30, 0xDD0000, new Integer[]{1});
                    setPuzzle(new Integer[]{crosssum(cell)*3, Dungeon.depth * 2});
                }
            }

            if (isBetween(Dungeon.depth,13,18)) {
                // More challenging locks for Caves
                if (cell % 3 == 0) {
                    addGear(1, 30, 0x009933, new Integer[]{2});
                    addGear(2, -60, 0xDD0000, new Integer[]{1});
                    addGear(3, -45, 0x00CCFF, new Integer[]{2});
                    setPuzzle(new Integer[]{crosssum(crosssum(cell)*cell), Dungeon.depth, crosssum(cell)});
                } else if (cell % 2 == 0) {
                    addGear(1, 90, 0x009933, new Integer[]{2});
                    addGear(2, 180, 0xDD0000, new Integer[]{3});
                    addGear(3, 45, 0x00CCFF, new Integer[]{1,2});
                    setPuzzle(new Integer[]{crosssum(cell)*2, 3, crosssum(cell)});
                } else {
                    addGear(1, 90, 0x009933, new Integer[]{3});
                    addGear(2, 30, 0xDD0000, new Integer[]{1});
                    addGear(3, 45, 0x00CCFF, new Integer[]{1});
                    setPuzzle(new Integer[]{2, 1, crosssum(cell)});
                }
            }
            if (isBetween(Dungeon.depth,19,24)) {
                // More challenging locks for City
                if (cell % 5 == 0) {
                    addGear(1, 30, 0x009933, new Integer[]{3});
                    addGear(2, -60, 0xDD0000, new Integer[]{});
                    addGear(3, -45, 0x00CCFF, new Integer[]{2});
                    addGear(4, -90, 0x9933ff, new Integer[]{});
                    setPuzzle(new Integer[]{5, Dungeon.depth, crosssum(cell), crosssum(Dungeon.depth)});
                } else if (cell % 2 == 0) {
                    addGear(1, 90, 0x009933, new Integer[]{3});
                    addGear(2, 36, 0xDD0000, new Integer[]{});
                    addGear(3, 45, 0x00CCFF, new Integer[]{2});
                    setPuzzle(new Integer[]{crosssum(cell)*3, 7, Dungeon.depth % 10});
                } else {
                    addGear(1, 90, 0x009933, new Integer[]{3,4});
                    addGear(2, 30, 0xDD0000, new Integer[]{1});
                    addGear(3, 45, 0x00CCFF, new Integer[]{1});
                    addGear(4, 36, 0x9933ff, new Integer[]{3});
                    setPuzzle(new Integer[]{crosssum(cell)*2, 4, crosssum(cell), crosssum(cell*Dungeon.depth)});
                }
            }
            if (isBetween(Dungeon.depth,25,30)) {
                // More challenging locks for Ice Plains
                if (cell % 5 == 0) {
                    addGear(1, -30, 0x009933, new Integer[]{3,4});
                    addGear(2, 60, 0xDD0000, new Integer[]{1,4});
                    addGear(3, 45, 0x00CCFF, new Integer[]{});
                    addGear(4, 90, 0x9933ff, new Integer[]{1});
                    setPuzzle(new Integer[]{0, 3, crosssum(cell+12), crosssum(cell+Dungeon.depth)});
                } else if (cell % 2 == 0) {
                    addGear(1, 90, 0x009933, new Integer[]{4});
                    addGear(2, 180, 0xDD0000, new Integer[]{1,3});
                    addGear(3, -45, 0x00CCFF, new Integer[]{2});
                    addGear(4, 90, 0x9933ff, new Integer[]{2});
                    setPuzzle(new Integer[]{crosssum(cell*5), 0, 7, crosssum(15*Dungeon.depth)});
                } else {
                    addGear(1, 90, 0x009933, new Integer[]{4});
                    addGear(2, 30, 0xDD0000, new Integer[]{1,4});
                    addGear(3, 45, 0x00CCFF, new Integer[]{});
                    addGear(4, -36, 0x9933ff, new Integer[]{1,2,3});
                    setPuzzle(new Integer[]{crosssum(cell), 3, crosssum(Dungeon.depth), 5});
                }
            }
            if (isBetween(Dungeon.depth,31,36) || Dungeon.depth > 36) {
                // Hell and beyond, this has serious locks and most likely you will use the Autosolve feature of a max level Hummingtool.
                mostChallenging(cell);
            }


        } else if ((heap != null && (heap.type != Heap.Type.HEAP && heap.type != Heap.Type.FOR_SALE)) && heap.type == Heap.Type.CRYSTAL_CHEST) {
            mostChallenging(cell);
        }

	}

    private boolean checkSolved() {
        solved = true;
        for (Image elem : gear ) {
            if (elem != null && (elem.angle % 360) !=  0) {
                solved = false;
            }
        }
        return solved;
    }

    public int crosssum(int number) {
        int sum = 0;
        while (0 != number) {
            sum += (number % 10);
            number = number / 10;
        }
        return sum;
    }

    public boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    public void unlock() {
        GLog.p("Unlocked!");
        thehero.spend( Key.TIME_TO_UNLOCK );
        thehero.sprite.operate( thecell );
        thehero.onOperateComplete();

        Sample.INSTANCE.play( Assets.SND_UNLOCK );
        solved = true;
        hide();
    }

    public void Autosolve() {
        for (Image elem : gear ) {
            if (elem != null && elem.angle != 0) {
                elem.angle = 0;
            }
        }
        hummingtool.usedAutosolve();
        unlock();
    }

    private void checkToolCharge () {
        if (hummingtool.charge() > 30 && hummingtool.level >= 3) {
            autosolve.enable( true );
        } else {
            autosolve.enable( false );
        }
        txtCharge.text("Tool Charge: " + hummingtool.charge());
        if (hummingtool.charge() == 0) {
            txtCharge.hardlight(0xDD0000);
        } else {
            txtCharge.hardlight(Window.TITLE_COLOR);
        }
    }

    private void addGear(final Integer ID, Integer Step, final Integer color, final Integer[] affect) {
        Log.e("Adding gear: ", String.valueOf(ID));
        gear[ID] = new Image( Assets.GEAR );
        gear[ID].color(0xFFCC00);
        add( gear[ID] );
        step[ID] = Step;

        gear[ID].x = 2+(2*(ID-1)+((ID-1)*gear[ID].width));
        gear[ID].y = 20;
        gear[ID].origin.set(gear[ID].width/2, gear[ID].height/2);
        affects[ID] = affect;
        hotArea[ID] = new TouchArea( 0, 0, 0, 0 ) {
            @Override
            protected void onTouchDown(Touchscreen.Touch touch) {
            }
            @Override
            protected void onTouchUp(Touchscreen.Touch touch) {
                gear[ID].color(color);
            }
            @Override
            protected void onClick( Touchscreen.Touch touch ) {
                if (hummingtool.charge() > 0) {
                    Sample.INSTANCE.play( Assets.SND_CLICK );
                    hummingtool.useCharge();
                    checkToolCharge();
                    gear[ID].angle += step[ID];
                    for (Integer afID : affect) {
                        if (gear[afID] != null) {
                            gear[afID].angle += step[afID];
                            gear[afID].color(color);
                        }
                    }
                    if (checkSolved()) {
                        unlock();
                    }
                } else {
                    GLog.n("The Hummingtool ran out of charge!");
                }
            }
        };
        hotArea[ID].x = gear[ID].x;
        hotArea[ID].y = gear[ID].y;
        hotArea[ID].width = gear[ID].width;
        hotArea[ID].height = gear[ID].height;
        add( hotArea[ID] );
    }

    private void setPuzzle(Integer[] starts) {
        for (int ID = 1; ID < starts.length; ID++) {
            if (gear[ID] != null) {
                for (int i = starts[ID]; i > 0; i--) {
                    gear[ID].angle += step[ID];
                    for (Integer afID : affects[ID]) {
                        if (gear[afID] != null) {
                            gear[afID].angle += step[afID];
                        }
                    }
                }
            }
        }
        if (checkSolved()) {
            gear[1].angle += step[1];
            for (Integer afID : affects[1]) {
                if (gear[afID] != null) {
                    gear[afID].angle += step[afID];
                }
            }
        }

    }

    private void mostChallenging(Integer cell) {
        // Most challenging locks for Hell and Crystal Chests
        if (cell % 5 == 0) {
            addGear(1, -30, 0x009933, new Integer[]{4});
            addGear(2, 60, 0xDD0000, new Integer[]{3});
            addGear(3, 45, 0x00CCFF, new Integer[]{1,2,5});
            addGear(4, -45, 0x9933ff, new Integer[]{1});
            addGear(5, 30, 0xFFFFFF, new Integer[]{});
            setPuzzle(new Integer[]{crosssum(cell)*6, 0, crosssum(cell*Dungeon.depth), 5, 3});
        } else if (cell % 2 == 0) {
            addGear(1, 90, 0x009933, new Integer[]{4});
            addGear(2, 180, 0xDD0000, new Integer[]{1,3});
            addGear(3, -45 ,0x00CCFF, new Integer[]{5});
            addGear(4, 90, 0x9933ff, new Integer[]{});
            addGear(5, 90, 0xFFFFFF, new Integer[]{1,3,4});
            setPuzzle(new Integer[]{crosssum(cell*3), 2, crosssum(cell), crosssum(Dungeon.depth),3});
        } else {
            addGear(1, 90, 0x009933, new Integer[]{2,5});
            addGear(2, 30, 0xDD0000, new Integer[]{4,5});
            addGear(3, 45, 0x00CCFF, new Integer[]{1});
            addGear(4, -36, 0x9933ff, new Integer[]{2});
            addGear(5, -60, 0xFFFFFF, new Integer[]{3});
            setPuzzle(new Integer[]{crosssum(cell), crosssum(Dungeon.depth), Dungeon.depth, 1, 3});
        }
        if (checkSolved()) {
            gear[1].angle += step[1];
            gear[3].angle -= step[3];
            gear[4].angle += step[4];
        }
    }

    @Override
    public void hide() {
        if (!solved) {
            thehero.ready();
        }
        super.hide();
    }

};

