package com.davidstemmer.warpzone.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.davidstemmer.warpzone.flow.ModalFlow;
import com.davidstemmer.warpzone.WarpState;
import com.davidstemmer.warpzone.WarpZone;
import com.davidstemmer.warpzone.flow.PageFlow;
import com.davidstemmer.warpzone.sample.module.ActivityModule;
import com.davidstemmer.warpzone.sample.stage.NavigationDrawerStage;
import com.davidstemmer.warpzone.sample.stage.WelcomeStage;

import javax.inject.Inject;

import mortar.Blueprint;
import mortar.Mortar;
import mortar.MortarScope;

public class MainActivity extends Activity implements Blueprint {

    @Inject WarpZone warpZone;
    @Inject NavigationDrawerStage navigationDrawerScene;
    @Inject ModalFlow.Whistle modalWhistle;
    @Inject PageFlow.Whistle pageWhistle;
    @Inject WelcomeStage welcomeStage;

    private MortarScope activityScope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().setHomeButtonEnabled(true);

        MortarScope parentScope = Mortar.getScope(getApplication());
        activityScope = Mortar.requireActivityScope(parentScope, this);
        Mortar.inject(this, this);

        warpZone.forkFlow(welcomeStage, pageWhistle);
    }

    private boolean isNavigationDrawerOpen() {
        return findViewById(R.id.navigation_drawer) != null;
    }

    @Override public void onBackPressed() {
        if (!warpZone.goBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Ignore menu click if stage is transitioning
        if (warpZone.getWarpState() == WarpState.WARPING) return true;

        switch (item.getItemId()) {
            case android.R.id.home:
                if (isNavigationDrawerOpen()) {
                    warpZone.goBack();
                } else {
                    warpZone.forkFlow(navigationDrawerScene, modalWhistle);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public Object getSystemService(String name) {
        if (Mortar.isScopeSystemService(name)) {
            return activityScope;
        }
        return super.getSystemService(name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MortarScope parentScope = Mortar.getScope(getApplication());
        parentScope.destroyChild(activityScope);
        activityScope = null;
    }

    @Override
    public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override
    public Object getDaggerModule() {
        return new ActivityModule(this);
    }


}