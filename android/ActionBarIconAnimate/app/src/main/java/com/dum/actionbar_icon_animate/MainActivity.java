package com.dum.actionbar_icon_animate;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MenuItem menuItemSync;
    private boolean isAnimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(view -> {
            isAnimate = true;
            menuItemSyncAnimate(isAnimate);
        });

        Button btnStop = findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(view -> {
            isAnimate = false;
            menuItemSyncAnimate(isAnimate);
        });

        Button btnInvalidateMenu = findViewById(R.id.btn_invalidate_menu);
        btnInvalidateMenu.setOnClickListener(view -> {
            invalidateOptionsMenu();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuItemSyncAnimate(false);
        menuItemSync = menu.findItem(R.id.action_sync);
        menuItemSync.getActionView().setOnClickListener(view -> {
            onOptionsItemSelected(menuItemSync);
        });
        menuItemSyncAnimate(isAnimate);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_sync:
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void menuItemSyncAnimate(boolean animate) {
        if (menuItemSync != null) {
            View imageView = menuItemSync.getActionView().findViewById(R.id.menu_img_sync);
            if (animate) {
                if (imageView.getAnimation() == null) {
                    rotateAnimation(imageView);
                }
            } else {
                stopAnimation(imageView);
            }
        }
    }

    private void rotateAnimation(View view) {
        Animation animation = (Animation) AnimationUtils.loadAnimation(this, R.anim.rotate_sync);
        animation.setRepeatCount(Animation.INFINITE);
        view.startAnimation(animation);
    }

    private void stopAnimation(View view) {
        Animation animation = view.getAnimation();
        if (animation != null) {
            animation.cancel();
        }
        view.clearAnimation();
    }
}
