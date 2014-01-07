
package org.jboss.aerogear.poc;

import com.sun.jersey.api.ConflictException;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON)
public class CarResource {
  private static EntityManager entityManager;

  @GET
  @Path("uuid")
  public String uuid() {
    return UUID.randomUUID().toString();
  }

  @GET
  @Path("{id}")
  public Car getIt(@PathParam("id") String id) {
    return getEntityManager().find(Car.class, id);
  }

  @POST
  @Path("{id}")
  public Response save(@PathParam("id")String uuid, Car car) {
    car.setId(uuid);
    getEntityManager().getTransaction().begin();
    getEntityManager().persist(car);
    getEntityManager().getTransaction().commit();
    return Response.ok(car).build();
  }

  @PUT
  @Path("{id}")
  public Response update(@PathParam("id") String id, Car car) {
    final EntityTransaction transaction = getEntityManager().getTransaction();
    try {
      transaction.begin();
      car.setId(id);
      getEntityManager().merge(car);
      getEntityManager().flush();
      transaction.commit();
      return Response.ok(getEntityManager().find(Car.class, car.getId())).build();
    } catch (OptimisticLockException e) {
      transaction.rollback();
      throw new ConflictException("conflict");
    }
  }

  public EntityManager getEntityManager() {
    if (entityManager == null) {
      EntityManagerFactory emf = Persistence.createEntityManagerFactory("h2");
      entityManager = emf.createEntityManager();
    }
    return entityManager;
  }
}
