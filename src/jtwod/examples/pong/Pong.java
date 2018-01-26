package jtwod.examples.pong;

import jtwod.examples.pong.game.PongEngine;

public class Pong {
    /**
     * Primary application entry point.
     *
     * @param args The arguments passed to the JVM.
     */
    public static void main(String[] args)
    {
        /*
         * Initialize the Pong envine. 
         */
        (new PongEngine()).start();
    }
}
