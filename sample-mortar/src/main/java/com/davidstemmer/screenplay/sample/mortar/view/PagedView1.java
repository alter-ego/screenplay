package com.davidstemmer.screenplay.sample.mortar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.davidstemmer.screenplay.sample.mortar.scene.PagedScene1;

import javax.inject.Inject;

import mortar.Mortar;

/**
 * Created by weefbellington on 10/19/14.
 */
public class PagedView1 extends RelativeLayout {

    @Inject PagedScene1.Presenter presenter;

    public PagedView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }
}
