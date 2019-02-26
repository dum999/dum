package com.charoentim.tts;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by dum on 6/11/2017 AD.
 */

public class Speaker implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;

    private boolean ready = false;

    public Speaker(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            tts = new TextToSpeech(context, this, "com.google.android.tts");
        } else {
            tts = new TextToSpeech(context, this);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Change this to match your
            // locale
            //tts.setLanguage(Locale.US);
            //tts.setLanguage(new Locale("th"));
            tts.setLanguage(Locale.getDefault());
            ready = true;
        } else {
            ready = false;
        }
    }

    public void speak(String text) {

        // Speak only if the TTS is ready
        // and the user has allowed speech

        if (ready) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, "");
            } else {
                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                        String.valueOf(AudioManager.STREAM_NOTIFICATION));
                tts.speak(text, TextToSpeech.QUEUE_ADD, hash);
            }
        }
    }

    public void pause(int duration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.playSilentUtterance(duration, TextToSpeech.QUEUE_ADD, null);
        } else {
            tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
        }
    }

    // Free up resources
    public void destroy(){
        tts.shutdown();
    }
}
