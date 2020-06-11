package cz.apolicky.summercampapp;

import android.view.View;

/**
 * OnClickListener that helps with passing an argument to functions. Uses only int as an argument
 * Makes it possible to pass an argument to OnClickeListener
 */
public class ArgumentedOnClickListener implements View.OnClickListener
{
    int argument;
    public ArgumentedOnClickListener(int arg) {
        argument = arg;
    }

    @Override
    public void onClick(View view) {}
};
