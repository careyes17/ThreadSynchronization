import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Garden {
  final Lock lock = new ReentrantLock();
  final Condition readyToDig  = lock.newCondition();
  final Condition dug = lock.newCondition();
  final Condition planted = lock.newCondition();

  // number of completed actions
  private int numDug = 0;
  private int numPlanted = 0;
  private int numFilled = 0;


  public void waitToDig() throws InterruptedException {
    lock.lock();
    try {
      while (numDug - numFilled == 5) { // while there are 5 unfilled holes
        System.out.println("Jordan is waiting to dig a hole.");
        readyToDig.await(); // waiting for holes to be filled
      }
    } finally {
      lock.unlock();
    }
  }

  public void dig() throws InterruptedException {
    lock.lock();
    try {
      numDug++; // increment the number of holes dug
      System.out.println("Jordan dug a hole.\t\t\t\t\t\t\t\t\t\t\t\t\t"+ numDug);
      dug.signal(); // signal to planter that a hole is ready to be planted
    } finally {
      lock.unlock();
    }
  }

  public void waitToPlant() throws InterruptedException {
    lock.lock();
    try {
      while (numPlanted == numDug) { // while there are no holes to be planted
        System.out.println("Charles is waiting to plant a hole.");
        dug.await(); // wait for holes to be dug
      }
    } finally {
      lock.unlock();
    }
  }

  public void plant() throws InterruptedException {
    lock.lock();
    try {
      numPlanted++; // increment number of holes planted
      System.out.println("Charles planted a hole.\t\t\t\t\t\t\t"+ numPlanted);
      planted.signal(); // signal hole filler that a hole has been planted
    } finally {
      lock.unlock();
    }
  }

  public void waitToFill() throws InterruptedException {
    lock.lock();
    try {
      while (numPlanted == numFilled) { // while there aren't holes to be filled
        System.out.println("Tracy is waiting to fill a hole.");
        planted.await(); // wait for a hole to be planted
      }
    } finally {
      lock.unlock();
    }
  }

  public void fill() throws InterruptedException {
    lock.lock();
    try {
      numFilled++; // increments number of holes filled
      System.out.println("Tracy filled a hole.\t\t\t\t\t\t\t\t\t\t"+ numFilled);
      readyToDig.signal(); // signals the person who digs that another hole can be dug
    } finally {
      lock.unlock();
    }
  }
}