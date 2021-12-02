package executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UseExecutorService1 {
  public static void main(String[] args) {
//    Executor ex = Executors.newFixedThreadPool(2);
    ExecutorService ex = Executors.newFixedThreadPool(2);
    ex.execute(() -> {
      System.out.println("Task 1 starting in thread "
          + Thread.currentThread().getName());
      if (Thread.interrupted()) {
        System.out.println("polling found shutdown request");
        return;
      }
      try {
        Thread.sleep((int)(Math.random() * 3000) + 500);
      } catch (InterruptedException e) {
        System.out.println("Premature shutdown t1 requested");
        return;
      }
      System.out.println("Task 1 completed...");
    });
    ex.execute(() -> {
      System.out.println("Task2 starting in thread "
          + Thread.currentThread().getName());
      try {
        Thread.sleep((int)(Math.random() * 3000) + 500);
      } catch (InterruptedException e) {
        System.out.println("Premature shutdown t2 requested");
        return;
      }
      System.out.println("Task 2 completed...");
    });
    ex.execute(() -> {
      System.out.println("Task 3 starting in thread "
          + Thread.currentThread().getName());
      try {
        Thread.sleep((int)(Math.random() * 3000) + 500);
      } catch (InterruptedException e) {
        System.out.println("Premature shutdown t3 requested");
        return;
      }      System.out.println("Task 3 completed...");
    });
    System.out.println("All tasks submitted");
    ex.shutdownNow();
    System.out.println("executor has been shutdown...");
    try {
      ex.execute(() -> {
        System.out.println("Task 3 starting in thread "
            + Thread.currentThread().getName());
        System.out.println("Task 3 completed...");
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
