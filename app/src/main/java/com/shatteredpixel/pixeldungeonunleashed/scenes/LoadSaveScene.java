/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2015 Evan Debenham
 *
 * Unleashed Pixel Dungeon
 * Copyright (C) 2015 David Mitchell
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

// NOTE - the idea behind Load/Save functionality was originated in Soft Pixel Dungeon
package com.shatteredpixel.pixeldungeonunleashed.scenes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.shatteredpixel.pixeldungeonunleashed.Dungeon;
import com.shatteredpixel.pixeldungeonunleashed.GamesInProgress;
import com.shatteredpixel.pixeldungeonunleashed.GoblinsPixelDungeon;
import com.shatteredpixel.pixeldungeonunleashed.levels.Terrain;
import com.shatteredpixel.pixeldungeonunleashed.ui.Archs;
import com.shatteredpixel.pixeldungeonunleashed.ui.RedButton;
import com.shatteredpixel.pixeldungeonunleashed.ui.Window;
import com.shatteredpixel.pixeldungeonunleashed.utils.Utils;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndError;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndOptions;
import com.shatteredpixel.pixeldungeonunleashed.windows.WndStory;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;


import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import static android.content.Context.DOWNLOAD_SERVICE;


public class LoadSaveScene extends PixelScene {

    private static final float BUTTON1_WIDTH = 34;
    private static final float BUTTON2_WIDTH = 55;
    private static final float BUTTON_HEIGHT = 20;
    private static final float BUTTON_PADDING = 3;

    private static final String TXT_TITLE	= "Save/Load ";

    private static final String TXT_LOAD	= "Load";
    private static final String TXT_SAVE	= "Save";
    private static final String TXT_SLOTNAME= "Game";

    private static final String HERO		= "hero";
    private static final String DEPTH		= "depth";
    private static final String DIFLEV      = "diflev";
    private static final String LEVEL		= "lvl";

    private static final String TXT_REALLY	= "Load";
    private static final String TXT_WARNING	= "Your current progress will be lost and Difficulty Levels may be changed.";
    private static final String TXT_YES		= "Yes, load " + TXT_SLOTNAME;
    private static final String TXT_NO		= "No, return to main menu";

    private static final String TXT_BKREALLY	= "Import Backup";
    private static final String TXT_BKWARNING	= "All your savegames and current progress will be replaced by the backup from your downloads folder.";
    private static final String TXT_BKYES		= "Yes, Import Backup";
    private static final String TXT_BKNO		= "No, return to main menu";

    private static final String TXT_IMREALLY	= "Create Backup";
    private static final String TXT_IMWARNING	= "All your savegames and game progress will be saved into your devices downloads folder. This will overwrite any existing backup already there.";
    private static final String TXT_IMYES		= "Yes, Create Backup";
    private static final String TXT_IMNO		= "No, return to main menu";

    private static final String TXT_DPTH_LVL	= "Depth: %d, level: %d";

    private static final int CLR_WHITE	= 0xFFFFFF;


    @Override
    public void create() {

        super.create();

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize( w, h );
        add( archs );
        String showClassName;

        if (Dungeon.hero != null) {
            showClassName = capitalizeWord(Dungeon.hero.heroClass.title());
        } else showClassName = "Backup & Restore";

        String diffLevel;
        String saveInstructions;
        switch (Dungeon.difficultyLevel) {
            case Dungeon.DIFF_TUTOR:
                diffLevel = "TUTORIAL";
                saveInstructions = "In Tutorial mode you may save anywhere.";
                break;
            case Dungeon.DIFF_EASY:
                diffLevel = "EASY";
                saveInstructions = "In Easy mode you may save anywhere.";
                break;
            case Dungeon.DIFF_HARD:
                diffLevel = "HARD";
                saveInstructions = "In Hard mode you may save after a boss level.";
                break;
            case Dungeon.DIFF_NTMARE:
                diffLevel = "NIGHTMARE";
                saveInstructions = "Games may not be saved/backed up in Nightmare mode.";
                break;
            case Dungeon.DIFF_ENDLESS:
                diffLevel = "ENDLESS";
                saveInstructions = "Games may not be saved/backed up in Endless mode.";
                break;
            case Dungeon.DIFF_TEST:
                diffLevel = "TEST";
                saveInstructions = "In Test mode you may save anywhere.";
                break;
            default:
                diffLevel = "NORMAL";
                saveInstructions = "In Normal mode you may save at level entrance signs.";
                break;
        }
        BitmapText title;
        if (Dungeon.hero != null) {
            title = PixelScene.createText( TXT_TITLE + showClassName + " - " + diffLevel, 9, true );
        } else {
            title = PixelScene.createText(TXT_TITLE + showClassName, 9, true);
            saveInstructions = "This is for progress transfer.";
        }
        title.hardlight( Window.TITLE_COLOR );
        title.measure();
        title.x = align( (w - title.width()) / 2 );
        title.y = BUTTON_PADDING;
        add( title );

        String currentProgress;
        GamesInProgress.Info info;
        if (Dungeon.hero != null) {
            info = GamesInProgress.check(Dungeon.hero.heroClass);
        } else info = null;
        if (info != null) {
            currentProgress = "Currently " + Utils.format(TXT_DPTH_LVL, info.depth, info.level);
        } else currentProgress = "";
        BitmapText subTitle = PixelScene.createText( currentProgress, 6, false );
        subTitle.hardlight(Window.TITLE_COLOR);
        subTitle.measure();
        subTitle.x = align( (w - title.width()) / 2 );
        subTitle.y =  (BUTTON_HEIGHT / 2) + BUTTON_PADDING ;
        add(subTitle);

        BitmapText saveInfo = PixelScene.createMultiline( saveInstructions, 6, false );
        saveInfo.hardlight( Window.TITLE_COLOR );
        saveInfo.measure();
        saveInfo.x = align( (w - saveInfo.width()) / 2 );
        saveInfo.y = BUTTON_PADDING + BUTTON_HEIGHT;
        add( saveInfo );



        int posY = (int) (BUTTON_HEIGHT + (BUTTON_PADDING * 3.5f));
        int posX2 = w - (int) (BUTTON2_WIDTH + BUTTON_PADDING);
        int posX = (int) (BUTTON1_WIDTH + (BUTTON_PADDING * 3));

        String[] slotList = { "A", "B", "C", "D", "E" };

        String classInfo;

        if (Dungeon.hero != null) {
            classInfo = Dungeon.hero.heroClass.title();

            for (String saveSlot : slotList) {
                // add the row caption..
                BitmapText buttonCapton1 = PixelScene.createText(TXT_SLOTNAME + " " + saveSlot, 9, true);
                buttonCapton1.hardlight(CLR_WHITE);
                buttonCapton1.measure();
                buttonCapton1.x = BUTTON_PADDING;
                buttonCapton1.y = posY + (BUTTON_HEIGHT / 3);
                add(buttonCapton1);

                // add the save button..
                if (Dungeon.hero.isAlive() &&
                        (Dungeon.difficultyLevel <= Dungeon.DIFF_EASY) || (Dungeon.difficultyLevel <= Dungeon.DIFF_TEST) ||
                        (Dungeon.difficultyLevel == Dungeon.DIFF_NORM &&
                                Dungeon.level.isAdjacentTo(Dungeon.hero.pos, Terrain.SIGN)) ||
                        (Dungeon.difficultyLevel == Dungeon.DIFF_HARD &&
                                Dungeon.level.isAdjacentTo(Dungeon.hero.pos, Terrain.SIGN) &&
                                Dungeon.bossLevel(Dungeon.depth - 1))) {

                    GameButton btnSave = new GameButton(this, true, TXT_SAVE, "", classInfo, saveSlot);
                    add(btnSave);
                    btnSave.visible = true;
                    btnSave.setRect(posX, posY, BUTTON1_WIDTH, BUTTON_HEIGHT);
                }

                // add the load button if there are saved files to load..
                String saveSlotFolder = Game.instance.getFilesDir().toString() + "/" + classInfo + saveSlot;

                File backupFolder = new File(saveSlotFolder);
                if (backupFolder.exists()) {
                    FileInputStream input;
                    try {
                        input = new FileInputStream(saveSlotFolder + "/" + classInfo + ".dat");
                        Bundle bundle = Bundle.read(input);
                        input.close();
                        int savedDepth = bundle.getInt(DEPTH, 1);
                        Bundle savedHero = bundle.getBundle(HERO);
                        int savedDif = bundle.getInt(DIFLEV);
                        int savedLevel = savedHero.getInt(LEVEL);
                        String savedProgress = Utils.format(TXT_DPTH_LVL, savedDepth, savedLevel);
                        String loadLevel;
                        switch (savedDif) {
                            case Dungeon.DIFF_TUTOR:
                                loadLevel = TXT_LOAD + " TUTOR";
                                break;
                            case Dungeon.DIFF_EASY:
                                loadLevel = TXT_LOAD + " EASY";
                                break;
                            case Dungeon.DIFF_HARD:
                                loadLevel = TXT_LOAD + " HARD";
                                break;
                            case Dungeon.DIFF_NTMARE:
                                loadLevel = TXT_LOAD + " NTMARE";
                                break;
                            case Dungeon.DIFF_TEST:
                                loadLevel = TXT_LOAD + " TEST";
                                break;
                            case Dungeon.DIFF_ENDLESS:
                                loadLevel = TXT_LOAD + " ENDLESS";
                                break;
                            default:
                                loadLevel = TXT_LOAD + " NORMAL";
                                break;
                        }
                        GameButton btnLoad1A = new GameButton(this, false, loadLevel, savedProgress, classInfo, saveSlot);

                        add(btnLoad1A);
                        btnLoad1A.visible = true;
                        btnLoad1A.setRect(posX2, posY, (int) (BUTTON2_WIDTH), BUTTON_HEIGHT);
                    } catch (FileNotFoundException e) {
                        //e.printStackTrace();
                    } catch (IOException e) {
                        //e.printStackTrace();
                    } catch (NullPointerException e) {
                        //e.printStackTrace();
                    }
                }
                // move down the line now...
                posY += BUTTON_HEIGHT + BUTTON_PADDING;
            }
        }


        // add two more buttons for load and export of all savegames to download folder.
        BitmapText buttonCapton2 = PixelScene.createText("Backups:", 9, true);
        buttonCapton2.hardlight(CLR_WHITE);
        buttonCapton2.measure();
        buttonCapton2.x = BUTTON_PADDING;
        buttonCapton2.y = posY + (BUTTON_HEIGHT / 3);
        add(buttonCapton2);

        // add the Export and Import button..
        // Export only if difficulty allows saving as well.
        int diff = GoblinsPixelDungeon.getDifficulty();
        if ((Dungeon.hero == null) || (!Dungeon.hero.isAlive()) || (diff <= Dungeon.DIFF_EASY) || (diff <= Dungeon.DIFF_TEST) ||
                (diff == Dungeon.DIFF_NORM)) {
            ExImButton btnEx = new ExImButton(this, true, "Backup", "all");
            add(btnEx);
            btnEx.visible = true;
            btnEx.setRect(posX, posY, BUTTON1_WIDTH, BUTTON_HEIGHT);
        }

        ExImButton btnIm = new ExImButton(this, false, "Import", "all");
        add(btnIm);
        btnIm.visible = true;
        btnIm.setRect(posX2, posY, (int) (BUTTON2_WIDTH), BUTTON_HEIGHT);


    fadeIn();
    }

    @Override
    protected void onBackPressed() {
        if (Dungeon.hero == null) {
            Game.switchScene(TitleScene.class);
        } else {
            InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
            Game.switchScene(InterlevelScene.class);
        }
    }

    protected static void exportGames(String classInfo, String saveSlot) {
        ArrayList<String> files = new ArrayList<>();
        String saveSlotFolder = Game.instance.getFilesDir().toString() + "/" + classInfo + saveSlot;
        makeFolder(saveSlotFolder);

        for(String fileName : Game.instance.fileList()){
            if(isGameLevelFile(classInfo, fileName)){
                files.add(fileName);
            }
        }

        // remove previous saved game files..
        File backupFolder = new File(saveSlotFolder);

        for(File backupFile : backupFolder.listFiles()){
            if(isGameLevelFile(classInfo, backupFile.getName())){
                backupFile.delete();
            }
        }

        for (String fileName : files){
            try {
                FileInputStream in = Game.instance.openFileInput(fileName);
                OutputStream out = new FileOutputStream(saveSlotFolder + "/" + fileName);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
            catch(Exception e){
                e.printStackTrace();
                WndStory.showChapter("Failed to save file " + fileName);
                //Log.d("FAILED EXPORT", f);
            }
        }
        InterlevelScene.mode = InterlevelScene.Mode.SAVE;
        Game.switchScene( InterlevelScene.class );
    }

    private static boolean isGameLevelFile(String classInfo, String fileName) {
        return fileName.endsWith(".dat") && (fileName.startsWith(classInfo));
    }

    private static void makeFolder(String saveSlotFolder) {
        File dir = new File(saveSlotFolder);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    protected static void importGames(String classInfo, String saveSlot) {
        ArrayList<String> files = new ArrayList<>();
        String saveSlotFolder = Game.instance.getFilesDir().toString() + "/" + classInfo + saveSlot;
        File backupFolder = new File(saveSlotFolder);

        for(File backupFile : backupFolder.listFiles()){
            if(isGameLevelFile(classInfo, backupFile.getName())){
                files.add(backupFile.getName());
            }
        }

        // remove in progress game files..
        for(String fileName : Game.instance.fileList()){
            if(fileName.startsWith("game_") || isGameLevelFile(classInfo, fileName)){
                Game.instance.deleteFile(fileName);
            }
        }


        for (String fileName : files){
            try {
                FileInputStream in = new FileInputStream(saveSlotFolder + "/" + fileName); //
                OutputStream out = Game.instance.openFileOutput(fileName, Game.MODE_PRIVATE );

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
            catch(Exception e){
                e.printStackTrace();
                WndStory.showChapter("Failed to load file " + fileName);
            }
        }
        InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
        Game.switchScene( InterlevelScene.class );
    }

    public static boolean zipFileAtPath(String sourcePath, String toLocation) {
        final int BUFFER = 2048;

        File sourceFile = new File(sourcePath);
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            if (sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length()+1);
            } else {
                byte data[] = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

/*
 *
 * Zips a subfolder
 *
 */

    private static void zipSubFolder(ZipOutputStream out, File folder,
                              int basePathLength) throws IOException {

        final int BUFFER = 2048;

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else {
                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(basePathLength);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }

    /*
     * gets the last path component
     *
     * Example: getLastPathComponent("downloads/example/fileToZip");
     * Result: "fileToZip"
     */
    public static String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        if (segments.length == 0)
            return "";
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }

    public static void unZip(String zipFile, String targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }
    }

    private static boolean checkPermission() {
        int permit = PackageManager.PERMISSION_GRANTED;
        return (ContextCompat.checkSelfPermission(Game.instance, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == permit) && (ContextCompat.checkSelfPermission(Game.instance, android.Manifest.permission.READ_EXTERNAL_STORAGE) == permit) && (ContextCompat.checkSelfPermission(Game.instance, android.Manifest.permission.INTERNET) == permit);
    }


    protected static void BackupGames() {
        String gameSlotFolder = Game.instance.getFilesDir().toString();
        String downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+ "/" + "GoblinsPDBackup.dat";
        if (checkPermission()) {
            zipFileAtPath(gameSlotFolder, downloadFolder);
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
            File file = new File(downloadFolder);
            DownloadManager downloadManager = (DownloadManager) GoblinsPixelDungeon.instance.getSystemService(DOWNLOAD_SERVICE);
            downloadManager.addCompletedDownload(file.getName(), file.getName(), true, "application/zip", file.getAbsolutePath(), file.length(), true);
            if (Dungeon.hero == null) {
                Game.switchScene(TitleScene.class);
            } else {
                InterlevelScene.mode = InterlevelScene.Mode.SAVE;
                Game.switchScene(InterlevelScene.class);
            }
        } else {
            Game.scene().add(new WndError("Read/Write External Storage & Internet permissions allow us to work with backups. Please allow these permissions in App Settings."));
        }
    }

    protected static void GetGamesBack() {

        String gameSlotFolder = Game.instance.getFilesDir().toString();
        // Need to replace redundant "files" folder to prevent unzip into new subfolder "files" inside the "files" folder we are in already.
        gameSlotFolder = gameSlotFolder.replace("files","");
        String downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+ "/" + "GoblinsPDBackup.dat";
        if (checkPermission()) {
            try {
                unZip(downloadFolder, gameSlotFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Dungeon.hero == null) {
                Game.switchScene(TitleScene.class);
            } else {
                InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
                Game.switchScene(InterlevelScene.class);
            }
        }  else {
            Game.scene().add(new WndError("Read/Write External Storage & Internet permissions allow us to work with backups. Please allow these permissions in App Settings."));
        }
    }

    private static class GameButton extends RedButton {

        private static final int SECONDARY_COLOR	= 0xCACFC2;

        private BitmapText secondary;
        private Boolean isSave = true;
        private String classInfo = "";
        private String saveSlot = "";
        private LoadSaveScene loadSaveScene;

        public GameButton(LoadSaveScene loadSaveScene, Boolean isSave, String primary, String secondary, String classInfo, String saveSlot ) {
            super( primary );
            this.secondary( secondary );
            this.isSave = isSave;
            this.classInfo = classInfo;
            this.saveSlot = saveSlot;
            this.loadSaveScene = loadSaveScene;
        }
        @Override
        protected void onClick() {
            if (isSave) {
                exportGames(classInfo, saveSlot);
            } else {
                loadSaveScene.add( new WndOptions( TXT_REALLY + " " +saveSlot + " " + secondary.text() + "?", TXT_WARNING, TXT_YES + " " + saveSlot, TXT_NO ) {
                    @Override
                    protected void onSelect( int index ) {
                        if (index == 0) {
                            importGames(classInfo, saveSlot);
                        }
                    }
                } );
            }
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            secondary = createText( 6, false );
            secondary.hardlight( SECONDARY_COLOR );
            add( secondary );
        }

        @Override
        protected void layout() {
            super.layout();

            if (secondary.text().length() > 0) {
                text.y = y + (height - text.height() - secondary.baseLine()) / 2;

                secondary.x = align( x + (width - secondary.width()) / 2 );
                secondary.y = align( text.y + text.height() );
            } else {
                text.y = y + (height - text.baseLine()) / 2;
            }
        }

        public void secondary( String text ) {
            secondary.text( text );
            secondary.measure();
        }

    }

    private static class ExImButton extends RedButton {

        private static final int SECONDARY_COLOR	= 0xCACFC2;

        private BitmapText secondary;
        private Boolean isSave = true;
        private String saveSlot = "";
        private LoadSaveScene loadSaveScene;

        public ExImButton(LoadSaveScene loadSaveScene, Boolean isSave, String primary, String secondary) {
            super( primary );
            this.secondary( secondary );
            this.isSave = isSave;
            this.loadSaveScene = loadSaveScene;
        }
        @Override
        protected void onClick() {
            if (isSave) {
                loadSaveScene.add( new WndOptions( TXT_IMREALLY + " " + saveSlot + "?", TXT_IMWARNING, TXT_IMYES, TXT_IMNO ) {
                    @Override
                    protected void onSelect( int index ) {
                        if (index == 0) {
                            BackupGames();
                        }
                    }
                } );
            } else {
                loadSaveScene.add( new WndOptions( TXT_BKREALLY + " " + saveSlot + "?", TXT_BKWARNING, TXT_BKYES, TXT_BKNO ) {
                    @Override
                    protected void onSelect( int index ) {
                        if (index == 0) {
                            GetGamesBack();
                        }
                    }
                } );
            }
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            secondary = createText( 6, false );
            secondary.hardlight( SECONDARY_COLOR );
            add( secondary );
        }

        @Override
        protected void layout() {
            super.layout();

            if (secondary.text().length() > 0) {
                text.y = y + (height - text.height() - secondary.baseLine()) / 2;

                secondary.x = align( x + (width - secondary.width()) / 2 );
                secondary.y = align( text.y + text.height() );
            } else {
                text.y = y + (height - text.baseLine()) / 2;
            }
        }

        public void secondary( String text ) {
            secondary.text( text );
            secondary.measure();
        }

    }
    public static String capitalizeWord(String oneWord)
    {
        return Character.toUpperCase(oneWord.charAt(0)) + oneWord.substring(1);
    }
}