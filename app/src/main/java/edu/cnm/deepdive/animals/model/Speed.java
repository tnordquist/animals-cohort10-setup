package edu.cnm.deepdive.animals.model;

import com.google.gson.annotations.Expose;

public class Speed {
  @Expose
  private String metric;

  @Expose
  String imperial;

  public String getMetric() {
    return metric;
  }

  public void setMetric(String metric) {
    this.metric = metric;
  }

  public String getImperial() {
    return imperial;
  }

  public void setImperial(String imperial) {
    this.imperial = imperial;
  }
}
