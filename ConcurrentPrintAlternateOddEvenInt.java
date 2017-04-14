import java.util.concurrent.locks.ReentrantLock;
import java.lang.InterruptedException;

public class ThreadDemo1
{
  static final Lock lock = new ReentrantLock();
  static final Condition oddDone = lock.newCondition();
  static final Condition evenDone = lock.newCondition();
  static final Condition done = lock.newCondition();
  static int count = 1;
  static void printOdd()
  {
    lock.lock();
    try {
      while (count % 2 == 0) {
        done.await();
      }
      System.out.println(count + " ");
      count++;
      done.signal();
    } catch (InterruptedException ie) {
      System.out.println("Odd Thread Interrupted");
      Thread.currentThread().interrupt();
    } finally {
      lock.unlock();
    }
  }
  static void printEven()
  {
    lock.lock();
    try {
      while (count % 2 == 1) {
        done.await();
      }
      System.out.println(count + " ");
      count++;
      done.signal();
    } catch(InterruptedException ie) {
      System.out.println("Even Thread Interrupted");
      Thread.currentThread().interrupt();
    } finally {
      lock.unlock();
    }
  }
  public static void main(String[] args)
  {
    Thread oddThread = new Thread(ThreadDemo1::printOdd);
    Thread evenThread = new Thread(ThreadDemo1::printEven);
    while (count <= 100) {
      oddThread.run();
      evenThread.run();
    }
  }
}
