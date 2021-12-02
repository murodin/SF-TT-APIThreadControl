package executors;

import java.util.concurrent.*;

class MyCallableTask implements Callable<String> {
  private static int nextId = 0;
  private int myId = nextId++;

  @Override
  public String call() throws Exception {
    String taskIdString = "MyCallableTask[" + myId + "]";
    System.out.println("Starting " + taskIdString
        + " in thread " + Thread.currentThread().getName());
    try {
      Thread.sleep((int) (Math.random() * 3000) + 500);
    } catch (InterruptedException ie) {
      System.out.println("Shutting down task " + taskIdString);
      return "oops, I finished early, but ...";
    }
    System.out.println("Completed " + taskIdString
        + " in thread " + Thread.currentThread().getName());
    return taskIdString + " results";
  }
}

public class UseExecutorService2 {
  public static void main(String[] args) throws Throwable {
    ExecutorService es = Executors.newFixedThreadPool(2);

    Future<String> handle = es.submit(new MyCallableTask());
    if (handle.isDone()) {
      System.out.println("It's ready, already");
    } else {
      System.out.println("Not ready yet");
    }
    Thread.sleep(200);
//    handle.cancel(false); // remove from task queue if not yet started
//    handle.cancel(true); // as above plus will send interrupt if already started
    es.shutdownNow();
    try {
      String result = handle.get();
      System.out.println("result is: " + result);
    } catch(InterruptedException ie) {
      System.out.println("Odd, someone interrupted the main thread?");
    } catch (ExecutionException e) {
      System.out.println("The task failed with an exception, which was"
      + e.getCause());
    } catch (CancellationException ce) {
      System.out.println("can't get from a canceled task (even if " +
          "it actually returned a value!!!");
    }
    es.shutdown();
  }
}
