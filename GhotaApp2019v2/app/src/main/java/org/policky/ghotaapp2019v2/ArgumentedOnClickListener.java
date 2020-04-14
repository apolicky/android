package org.policky.ghotaapp2019v2;

import android.view.View;

/**
 * Makes it possible to pass an argument to OnClickeListener
 */
public class ArgumentedOnClickListener implements View.OnClickListener
{
    int argument;
    public ArgumentedOnClickListener(int arg) {
        argument = arg;
    }

    @Override
    public void onClick(View v)
    {

    }

};
