package com.davidstemmer.screenplay.sample.mortar.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.davidstemmer.screenplay.sample.mortar.R;
import com.davidstemmer.screenplay.sample.mortar.scene.ModalScene;
import com.davidstemmer.screenplay.sample.mortar.scene.PagedScene1;
import com.davidstemmer.screenplay.sample.mortar.scene.SimpleScene;
import com.davidstemmer.screenplay.sample.mortar.view.NavigationMenuView;
import com.davidstemmer.screenplay.scene.Scene;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.ButterKnife;
import butterknife.OnClick;
import flow.Flow;
import mortar.ViewPresenter;

/**
 * Created by weefbellington on 10/25/14.
 */
@Singleton
public class NavigationMenuPresenter extends ViewPresenter<NavigationMenuView> {

    private final DrawerPresenter drawer;
    private final Flow flow;
    private final SimpleScene simpleScene;
    private final PagedScene1 pagedScene;
    private final ModalScene modalScene;

    private int selected = R.id.nav_item_simple_scene;

    @Inject
    public NavigationMenuPresenter(DrawerPresenter drawerPresenter,
                                   Flow flow,
                                   SimpleScene simpleScene,
                                   PagedScene1 pagedScene,
                                   ModalScene modalScene) {

        this.drawer = drawerPresenter;
        this.flow = flow;
        this.simpleScene = simpleScene;
        this.pagedScene = pagedScene;
        this.modalScene = modalScene;
    }

    @OnClick(R.id.nav_item_simple_scene)
    void welcomeClicked(View selected) {
        setSelected(selected);
        showNextSceneAfterDelay(simpleScene);
        drawer.close();
    }

    @OnClick(R.id.nav_item_paged_scenes)
    void pagedScenesClicked(View selected) {
        setSelected(selected);
        showNextSceneAfterDelay(pagedScene);
        drawer.close();
    }

    @OnClick(R.id.nav_item_modal_scenes)
    void modalScenesClicked(View selected) {
        setSelected(selected);
        showNextSceneAfterDelay(modalScene);
        drawer.close();
    }

    private void setSelected(View selectedView) {
        setSelected(selectedView.getId());
    }

    private void setSelected(int id) {
        selected = id;
        for (int i = 0; i < getView().getChildCount(); i++) {
            TextView child = (TextView) getView().getChildAt(i);
            if (id == child.getId()) {
                child.setSelected(true);
            } else {
                child.setSelected(false);
            }
        }
    }

    @Override
    protected void onLoad(Bundle savedInstanceState) {
        super.onLoad(savedInstanceState);
        ButterKnife.inject(this, (Activity) getView().getContext());
        setSelected(selected);
    }

    private final Handler mDrawerHandler = new Handler();
    private void showNextSceneAfterDelay(final Scene nextScene) {
        // Clears any previously posted runnables, for double clicks
        mDrawerHandler.removeCallbacksAndMessages(null);
        mDrawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                flow.replaceTo(nextScene);
            }
        }, 250);
        // The millisecond delay is arbitrary and was arrived at through trial and error
        drawer.close();
    }
}
