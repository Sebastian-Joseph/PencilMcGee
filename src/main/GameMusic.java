package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class GameMusic {

    Clip clip;
    URL soundURL[] = new URL[30];

    public GameMusic() {

        soundURL[0] = getClass().getResource("Music/lvl1.wav");
        soundURL[1] = getClass().getResource("Music/jump.wav");
        soundURL[2] = getClass().getResource("Music/coin.wav");
        soundURL[3] = getClass().getResource("Music/main_menu.wav");
        soundURL[4] = getClass().getResource("Music/lvl2.wav");
        soundURL[5] = getClass().getResource("Music/lvl3.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f);

        } catch (Exception e) {
        }
    }

    public void notsetFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-5.0f);
            long length = clip.getMicrosecondLength() - 31200000;



        } catch (Exception e) {
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
        clip.flush();
        clip.close();

    }

    public void looponce() {
        clip.loop(0);
    }
}















