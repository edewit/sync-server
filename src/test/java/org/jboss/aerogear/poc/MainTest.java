
package org.jboss.aerogear.poc;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import junit.framework.TestCase;

import javax.persistence.OptimisticLockException;
import java.util.Comparator;


public class MainTest extends TestCase {

  private SelectorThread threadSelector;

  private WebResource r;

  public MainTest(String testName) {
    super(testName);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    threadSelector = ServerStarter.startServer();

    Client c = Client.create();
    r = c.resource(ServerStarter.BASE_URI);
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();

    threadSelector.stopEndpoint();
  }

  /**
   * Test to see that the message "Got it!" is sent in the response.
   */
  public void testConflictHandling() {
    // given
    final Car car = new Car();
    car.setMake("Toyota");
    car.setModel("Prius");
    car.setNumberOfDoors(5);
    String uuid = r.path("cars").path("uuid").get(String.class);
    Car savedCar = r.path("cars").path(uuid).post(Car.class, car);

    assertCarEqual(savedCar);

    savedCar.setModel("Aygo");
    savedCar.setNumberOfDoors(3);
    savedCar = r.path("cars").path(uuid).put(Car.class, savedCar);

    assertCarEqual(savedCar);

    // when
    car.setId(savedCar.getId());
    car.setVersion(0);
    car.setModel("an old with changed model");
    try {
      r.path("cars").path(uuid).put(car);
      fail("can't update the wrong version");
    } catch (UniformInterfaceException e) {
      // success
    }
  }

  private void assertCarEqual(Car car) {
    Car response = r.path("cars/").path(String.valueOf(car.getId())).get(Car.class);
    final Comparator<Car> carComparator = new Comparator<Car>() {

      @Override
      public int compare(Car car1, Car car2) {
        if (car1.getNumberOfDoors() != car2.getNumberOfDoors()) return -1;
        if (!car1.getId().equals(car2.getId())) return -1;
        if (car1.getMake() != null ? !car2.getMake().equals(car2.getMake()) : car2.getMake() != null) return -1;
        if (car1.getModel() != null ? !car1.getModel().equals(car2.getModel()) : car2.getModel() != null) return -1;
        if (car1.getVersion() != null ? !car1.getVersion().equals(car2.getVersion()) : car2.getVersion() != null) return -1;

        return 0;
      }
    };

    if (carComparator.compare(response, car) != 0) {
      failNotEquals("cars are not equal", response, car);
    }
  }
}
