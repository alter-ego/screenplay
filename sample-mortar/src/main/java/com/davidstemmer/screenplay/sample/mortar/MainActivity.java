package com.davidstemmer.screenplay.sample.mortar;

import com.davidstemmer.screenplay.SimpleActivityDirector;
import com.davidstemmer.screenplay.flow.Screenplay;
import com.davidstemmer.screenplay.sample.mortar.module.ApplicationComponent;
import com.davidstemmer.screenplay.sample.mortar.presenter.NavigationMenuPresenter;
import com.davidstemmer.screenplay.scene.Scene;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import flow.Flow;

public class MainActivity extends ActionBarActivity {

    private final Handler mDrawerHandler = new Handler();

    DrawerLayout mNavigationDrawer;

    @InjectView(R.id.main)
    ViewGroup mMain;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    Scene mWelcomeScene;

    @Inject
    Scene mPagedScene;

    @Inject
    Scene mStackedScene;

    private Flow mFlow;

    private Drawer.Result mDrawer;

    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        ApplicationComponent component = MainApplication.getComponent();
        // TODO move some of this logic to its own class
        mFlow = component.flow();

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this, this);

        setSupportActionBar(mToolbar);
        createDrawer();

        NavigationMenuPresenter navigationMenuPresenter = component.menuPresenter();
        navigationMenuPresenter.setDrawer(mDrawer);

        SimpleActivityDirector director = component.director();
        director.bind(this, mMain);

        Screenplay screenplay = component.screenplay();
        screenplay.enter(mFlow);
    }

    private void createDrawer() {
        AccountHeader.Result headerResult = new AccountHeader()
                .withActivity(mActivity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Ivan Morgillo").withEmail("ivan@alterego.solutions")
                                .withIcon(mActivity.getResources().getDrawable(R.drawable.profile))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public void onProfileChanged(View view, IProfile profile) {
                    }
                })
                .build();

        mDrawer = new Drawer()
                .withActivity(mActivity)
                .withToolbar(mToolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.nav_item_simple_screen).withIcon(R.drawable.ic_home_grey600_24dp).withIdentifier(1),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.nav_item_paged_scenes).withIcon(R.drawable.ic_settings_grey600_24dp).withIdentifier(2),
                        new SecondaryDrawerItem().withName(R.string.nav_item_modal_scenes).withIcon(R.drawable.ic_launcher).withIdentifier(3)
                )
                .build();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else if (!mFlow.goBack()) {
            super.onBackPressed();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
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
        // The millisecond delay is arbitrary and was arrived at through trial and error
        mDrawer.closeDrawer();
    }
}
