package is.hi.screensage_web_server.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaResponse {
  int page;

  @JsonProperty("results")
  private Media[] results;

  int total_pages;

  int total_results;

  public int getPage() {
    return page;
  };

  public Media[] getResults() {
    return results;
  }

  public void setResults(Media[] results) {
    this.results = results;
  }

  public int getTotal_pages() {
    return total_pages;
  };

  public int getTotal_results() {
    return total_results;
  };

}