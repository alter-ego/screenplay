package com.davidstemmer.screenplay.sample.mortar;

import com.davidstemmer.screenplay.SimpleActivityDirector;
import com.davidstemmer.screenplay.flow.Screenplay;
import com.davidstemmer.screenplay.sample.mortar.module.ApplicationComponent;
import com.davidstemmer.screenplay.sample.mortar.presenter.DrawerPresenter;
import com.davidstemmer.screenplay.sample.mortar.presenter.NavigationMenuPresenter;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import flow.Flow;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.drawer_parent)
    DrawerLayout mNavigationDrawer;

    @InjectView(R.id.main)
    ViewGroup mMain;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    private Flow mFlow;

    private DrawerPresenter mDrawerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationComponent component = MainApplication.getComponent();
        // TODO move some of this logic to its own class
        mFlow = component.flow();
        mDrawerPresenter = component.drawerPresenter();

        NavigationMenuPresenter navigationPresenter = component.menuPresenter();

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this, this);

        setSupportActionBar(mToolbar);

        View navigationMenu = getLayoutInflater().inflate(R.layout.navigation_menu, mNavigationDrawer, false);
        navigationMenu.addOnAttachStateChangeListener(navigationPresenter);
        mNavigationDrawer.addView(navigationMenu);

        SimpleActivityDirector director = component.director();
        director.bind(this, mMain);

        mDrawerPresenter.onViewAttachedToWindow(mNavigationDrawer);

        Screenplay screenplay = component.screenplay();
        screenplay.enter(mFlow);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerPresenter.syncToggleState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerPresenter.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (!mFlow.goBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (mDrawerPresenter.isLockedOpen() || mDrawerPresenter.isLockedShut()) {
                return true;
            } else if (mDrawerPresenter.onOptionsItemSelected(item)) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDrawerPresenter.onViewDetachedFromWindow(mNavigationDrawer);
    }
}
