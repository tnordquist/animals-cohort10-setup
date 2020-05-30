package edu.cnm.deepdive.animals.model;

import com.google.gson.annotations.Expose;

public class Taxonomy {

  @Expose
  String kingdom;

  @Expose
  String order;

  @Expose
  String family;

  public String getKingdom() {
    return kingdom;
  }

  public void setKingdom(String kingdom) {
    this.kingdom = kingdom;
  }

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public String getFamily() {
    return family;
  }

  public void setFamily(String family) {
    this.family = family;
  }

}
