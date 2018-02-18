package jtwod.engine.timing;

import jtwod.engine.Engine;
import jtwod.engine.Scene;

public abstract class RecurringTimer<ParentEngine extends Engine> {
    /**
     * How often this RecurringTimer should run in seconds.
     */
    private double seconds = 0;

    /**
     * The current tick in seconds.
     */
    private int currentTickInSecond = 0;

    /**
     * The parent Scene for this RecurringTimer.
     */
    private Scene<ParentEngine> scene;

    /**
     * Construct a new RecurringTimer.
     *
     * @param seconds How often this RecurringTimer should run in seconds.
     * @param scene The parent Scene for this RecurringTimer.
     */
    public RecurringTimer(double seconds, Scene<ParentEngine> scene)
    {
        this.seconds = seconds;
        this.scene = scene;
    }

    /**
     * Notify this RecurringTimer to run an Update.
     */
    public final void notifyUpdate()
    {
        if (scene.getTps() > 0 && ((double)currentTickInSecond / (double)scene.getTps()) >= this.seconds) {
            update();
            currentTickInSecond=0;
        }

        currentTickInSecond++;
    }

    /**
     * Called when the timer is ready to Update.
     */
    public abstract void update();
}
