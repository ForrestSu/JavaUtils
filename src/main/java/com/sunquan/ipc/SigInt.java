package com.sunquan.ipc;

import sun.misc.Signal;

/**
 * Utility to allow the registration of a SIGINT handler that hides the unsupported {@link Signal} class.
 */
public class SigInt
{
    /**
     * Register a task to be run when a SIGINT is received.
     *
     * @param task to run on reception of the signal.
     */
    public static void register(final Runnable task)
    {
        Signal.handle(new Signal("INT"), (signal) -> task.run());
    }
}
