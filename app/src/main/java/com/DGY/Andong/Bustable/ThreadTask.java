package com.DGY.Andong.Bustable;

public abstract class ThreadTask<T1, T2, T3> implements Runnable {

    T1 mArgument1;

    T2 mArgument2;

    // Result
    T3 mResult;

    // Execute
    final public void execute(final T1 arg1, final T2 arg2) {
        // Store the argument
        mArgument1 = arg1;
        mArgument2 = arg2;

        // Call onPreExecute
        onPreExecute();

        // Begin thread work
        Thread thread = new Thread(this);
        thread.start();

        // Wait for the thread work
        try {
            thread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            onPostExecute(null);
            return;
        }

        // Call onPostExecute
        onPostExecute(mResult);
    }

    @Override
    public void run() {
        mResult = doInBackground(mArgument1, mArgument2);
    }

    // onPreExecute
    protected abstract void onPreExecute();

    // doInBackground
    protected abstract T3 doInBackground(T1 arg1, T2 arg2);

    // onPostExecute
    protected abstract void onPostExecute(T3 result);
}
