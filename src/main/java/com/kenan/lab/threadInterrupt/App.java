package com.kenan.lab.threadInterrupt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 * https://dzone.com/articles/understanding-thread-interruption-in-java
 * 
 */
public class App {
    public static void main( final String[] args ) throws InterruptedException {

        // Using the Future value //
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future< ? > future = executor.submit( ( ) -> {
            printNumbers(); // first call
                printNumbers(); // second call
            } );
        Thread.sleep( 3_000 );
        executor.shutdownNow(); // will interrupt the task
        executor.awaitTermination( 3, TimeUnit.SECONDS );

        // An Implementation of the Use Case Using the Executor
        final ExecutorService executor1 = Executors.newSingleThreadExecutor();
        executor1.submit( taskThatFinishesEarlyOnInterruption() ); // requirement
                                                                   // 3
        Thread.sleep( 3_000 ); // requirement 4
        executor1.shutdownNow(); // requirement 5
        executor1.awaitTermination( 1, TimeUnit.SECONDS ); // requirement 6


        // An Implementation of the Use Case Using Thread
        final Thread taskThread = new Thread( taskThatFinishesEarlyOnInterruption() );
        taskThread.start(); // requirement 3
        Thread.sleep( 3_000 ); // requirement 4
        taskThread.interrupt(); // requirement 5
        taskThread.join( 1_000 ); // requirement 6

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
