package org.jboss.aerogear.poc;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Car {

  @Id
  private String id;

  @Version
  private Integer version;

  private String make;
  private String model;
  private int numberOfDoors;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public String getMake() {
    return make;
  }

  public void setMake(String make) {
    this.make = make;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public int getNumberOfDoors() {
    return numberOfDoors;
  }

  public void setNumberOfDoors(int numberOfDoors) {
    this.numberOfDoors = numberOfDoors;
  }

  @Override
  public String toString() {
    return "Car{" +
        "id=" + id +
        ", version=" + version +
        ", make='" + make + '\'' +
        ", model='" + model + '\'' +
        ", numberOfDoors=" + numberOfDoors +
        '}';
  }
}
