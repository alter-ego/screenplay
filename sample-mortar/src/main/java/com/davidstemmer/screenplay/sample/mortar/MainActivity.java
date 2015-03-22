package com.davidstemmer.screenplay.sample.mortar;

import com.davidstemmer.screenplay.SimpleActivityDirector;
import com.davidstemmer.screenplay.flow.Screenplay;
import com.davidstemmer.screenplay.sample.mortar.module.ApplicationComponent;
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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import flow.Flow;

public class MainActivity extends ActionBarActivity {

    DrawerLayout mNavigationDrawer;

    @InjectView(R.id.main)
    ViewGroup mMain;

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

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
                        new PrimaryDrawerItem().withName(R.string.nav_item_simple_screen).withIcon(R.drawable.ic_home_grey600_24dp),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.nav_item_paged_scenes).withIcon(R.drawable.ic_settings_grey600_24dp),
                        new SecondaryDrawerItem().withName(R.string.nav_item_modal_scenes).withIcon(R.drawable.ic_launcher)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            if (drawerItem.getIdentifier() == 1) {

                            } else if (drawerItem.getIdentifier() == 2) {

                            } else if (drawerItem.getIdentifier() == 3) {

                            }
                        }
                    }
                })
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
}
