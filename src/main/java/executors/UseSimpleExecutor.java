package executors;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UseSimpleExecutor {
  public static void delay() {
    try {
      Thread.sleep((int)(Math.random() * 3000) + 500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  public static void main(String[] args) {
//    Executor ex = Executors.newFixedThreadPool(2);
    ExecutorService ex = Executors.newFixedThreadPool(2);
    ex.execute(() -> {
      System.out.println("Task starting in thread "
          + Thread.currentThread().getName());
      delay();
      System.out.println("Task completed...");
    });
    ex.execute(() -> {
      System.out.println("Task2  starting in thread "
          + Thread.currentThread().getName());
      delay();
      System.out.println("Task 2 completed...");
    });
    ex.execute(() -> {
      System.out.println("Task 3 starting in thread "
          + Thread.currentThread().getName());
      delay();
      System.out.println("Task 3 completed...");
    });
    System.out.println("All tasks submitted");
    ex.shutdownNow();
    System.out.println("executor has been shutdown...");
    try {
      ex.execute(() -> {
        System.out.println("Task 3 starting in thread "
            + Thread.currentThread().getName());
        delay();
        System.out.println("Task 3 completed...");
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
