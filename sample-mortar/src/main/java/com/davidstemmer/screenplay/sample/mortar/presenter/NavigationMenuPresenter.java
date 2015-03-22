package com.davidstemmer.screenplay.sample.mortar.presenter;

import com.davidstemmer.screenplay.sample.mortar.scene.PagedScene1;
import com.davidstemmer.screenplay.sample.mortar.scene.StackedScene;
import com.davidstemmer.screenplay.sample.mortar.scene.WelcomeScene;
import com.davidstemmer.screenplay.scene.Scene;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import javax.inject.Inject;
import javax.inject.Singleton;

import flow.Flow;

@Singleton
public class NavigationMenuPresenter extends ViewPresenter<ViewGroup> implements Drawer.OnDrawerItemClickListener {

    private final Flow mFlow;

    private final WelcomeScene mWelcomeScene;

    private final PagedScene1 mPagedScene;

    private final StackedScene mStackedScene;

    private final Handler mDrawerHandler = new Handler();

    private Drawer.Result mDrawer;
    
    @Inject
    public NavigationMenuPresenter(Flow flow, WelcomeScene welcomeScene, PagedScene1 pagedScene, StackedScene stackedScene) {
        mFlow = flow;
        mWelcomeScene = welcomeScene;
        mPagedScene = pagedScene;
        mStackedScene = stackedScene;
    }

    private void showNextSceneAfterDelay(final Scene nextScene) {
        // Clears any previously posted runnables, for double clicks
        mDrawerHandler.removeCallbacksAndMessages(null);
        mDrawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFlow.replaceTo(nextScene);
            }
        }, 250);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem drawerItem) {
        if (drawerItem != null) {
            if (drawerItem.getIdentifier() == 1) {
                showNextSceneAfterDelay(mWelcomeScene);
                mDrawer.closeDrawer();
            } else if (drawerItem.getIdentifier() == 2) {
                showNextSceneAfterDelay(mPagedScene);
                mDrawer.closeDrawer();
            } else if (drawerItem.getIdentifier() == 3) {
                showNextSceneAfterDelay(mStackedScene);
                mDrawer.closeDrawer();
            }
        }
    }

    public Drawer.Result getDrawer() {
        return mDrawer;
    }

    public void setDrawer(Drawer.Result drawer) {
        mDrawer = drawer;
        mDrawer.setOnDrawerItemClickListener(this);
    }
}
