package com.kenan.lab.threadInterrupt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( final String[] args ) throws InterruptedException {

        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future< ? > future = executor.submit( ( ) -> {
            printNumbers(); // first call
                printNumbers(); // second call
            } );
        Thread.sleep( 3_000 );
        executor.shutdownNow(); // will interrupt the task
        executor.awaitTermination( 3, TimeUnit.SECONDS );

        /*
         * final ExecutorService executor = Executors.newSingleThreadExecutor();
         * executor.submit( taskThatFinishesEarlyOnInterruption() ); //
         * requirement // 3 Thread.sleep( 3_000 ); // requirement 4
         * executor.shutdownNow(); // requirement 5 executor.awaitTermination(
         * 1, TimeUnit.SECONDS ); // requirement 6
         */

        /*
         * final Thread taskThread = new Thread(
         * taskThatFinishesEarlyOnInterruption() ); taskThread.start(); //
         * requirement 3
         * 
         * Thread.sleep( 3_000 ); // requirement 4
         * 
         * taskThread.interrupt(); // requirement 5
         * 
         * taskThread.join( 1_000 ); // requirement 6
         */
    }

    private static void printNumbers() {

        for ( int i = 0; i < 10; i++ ) {
            System.out.print( i );
            try {
                Thread.sleep( 1_000 );
            } catch ( final InterruptedException e ) {
                Thread.currentThread().interrupt(); // preserve interruption
                                                    // status
                break;
            }
        }
    }

    private static Runnable taskThatFinishesEarlyOnInterruption() {

        return ( ) -> {
            for ( int i = 0; i < 10; i++ ) {
                System.out.print( i ); // requirement 1
                try {
                    Thread.sleep( 1_000 ); // requirement 2
                } catch ( final InterruptedException e ) {
                    break; // requirement 7
                }
            }
        };
    }
}
