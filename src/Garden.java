import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Garden {
  final Lock lock = new ReentrantLock();
  final Condition ready  = lock.newCondition();
  final Condition dug = lock.newCondition();
  final Condition planted = lock.newCondition();

  private int dugNum = 0;
  private int plantedNum = 0;
  private int filledNum = 0;


  public void waitToDig() throws InterruptedException {
    lock.lock();
    try {
      while (dugNum - filledNum == 4) {
        System.out.println("Jordan is waiting to dig a hole.");
        ready.await();
      }
    } finally {
      lock.unlock();
    }
  }

  public void dig() throws InterruptedException {
    lock.lock();
    try {
      dugNum++;
      System.out.println("Jordan dug a hole.\t\t\t\t\t\t\t\t\t\t\t\t\t"+dugNum);
      dug.signal();
    } finally {
      lock.unlock();
    }
  }

  public void waitToPlant() throws InterruptedException {
    lock.lock();
    try {
      while (plantedNum == dugNum) {
        System.out.println("Charles is waiting to plant a hole.");
        dug.await();
      }
    } finally {
      lock.unlock();
    }
  }

  public void plant() throws InterruptedException {
    lock.lock();
    try {
      plantedNum++;
      System.out.println("Charles planted a hole.\t\t\t\t\t\t\t"+plantedNum);
      planted.signal();
    } finally {
      lock.unlock();
    }
  }

  public void waitToFill() throws InterruptedException {
    lock.lock();
    try {
      while (plantedNum == filledNum) {
        System.out.println("Tracy is waiting to fill a hole.");
        planted.await();
      }
    } finally {
      lock.unlock();
    }
  }

  public void fill() throws InterruptedException {
    lock.lock();
    try {
      filledNum++;
      System.out.println("Tracy filled a hole.\t\t\t\t\t\t\t\t\t\t"+filledNum);
      ready.signal();
    } finally {
      lock.unlock();
    }
  }

}