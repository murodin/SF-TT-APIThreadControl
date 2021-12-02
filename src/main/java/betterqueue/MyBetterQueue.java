package betterqueue;

import java.io.FileReader;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBetterQueue<E> {
  private ReentrantLock lock = new ReentrantLock();
  private Condition notFull = lock.newCondition();
  private Condition notEmpty = lock.newCondition();
  private Object rendezvous = new Object();

  private static final int SIZE = 10;
  private E[] data = (E[])new Object[SIZE];
  private int count = 0;

  public void put(E e) throws InterruptedException {
    lock.lock(); try {
      while (count >= SIZE) {
        notFull.await();
      }
      data[count++] = e;
      notEmpty.signal();
    } finally {
      lock.unlock();
    }
  }

  public E take() throws InterruptedException {
    lock.lock(); try {
      while (count <= 0) {
        notEmpty.await();
      }
      E rv = data[0];
      System.arraycopy(data, 1, data, 0, --count);
      notFull.signal();
      return rv;
    } finally {
      lock.unlock();
    }
  }

  public static void main(String[] args) throws Throwable {
    MyBetterQueue<int[]> mqi = new MyBetterQueue<>();
    new Thread(()->{
      try {
        for (int i = 0; i < 1_000; i++) {
          int[] data = new int[2]; // MUST BE a NEW object every time!!!
          data[0] = i;
          if (i < 100) {
            Thread.sleep(1);
          }
          data[1] = i;
          if (i == 500) {
            data[0] = -1; // TEST the TEST!!!
          }
          mqi.put(data); data = null; // we MUST NOT touch this object again!
        }
      } catch (InterruptedException ie) {
        System.out.println("oops, shutdown requested");
      }
      System.out.println("Producer completed");
    }).start();

    new Thread(()->{
      try {
        for (int i = 0; i < 1_000; i++) {
          int[] data = mqi.take();
          if (data[0] != data[1] || data[0] != i) {
            // found an error!!!
            System.out.printf("Bad data at index %d [0] is %d [1] is %d\n",
                i, data[0], data[1]);
            if (i > 900) {
              Thread.sleep(1);
            }
          }
        }
      } catch (InterruptedException ie) {
        System.out.println("huh, something requested shutdown");
      }
      System.out.println("Consumer completed");
    }).start();
    System.out.println("Workers started");
  }
}
