import java.util.Random;

// plants in dug holes
class Charles implements Runnable {

  Garden garden;
  Random rand = new Random();

  public Charles(Garden g) {
    this.garden = g;
  }

  public void run() {
    try {
      Thread.sleep(rand.nextInt(1000)); // makes the execution more random
      for (int i = 0; i < 10; i++) {
        garden.waitToPlant(); // waits to plant
        garden.plant(); // if done waiting, plants
        Thread.sleep(rand.nextInt(500)); // planting
      }
    } catch (InterruptedException e) {
      System.out.println(e);
      e.printStackTrace();
      System.exit(1);
    }
  }
}
