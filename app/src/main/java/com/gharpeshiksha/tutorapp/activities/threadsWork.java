package com.gharpeshiksha.tutorapp.activities;

public class threadsWork extends Thread{

    @Override
    public void run() {
      //  super.run();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
