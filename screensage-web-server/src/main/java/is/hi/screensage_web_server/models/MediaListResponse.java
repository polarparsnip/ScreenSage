package is.hi.screensage_web_server.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaListResponse {
  int page;

  @JsonProperty("results")
  private List<Media> results;

  int total_pages;

  int total_results;

  public int getPage() {
    return page;
  };

  public List<Media> getResults() {
    return results;
  }

  public void setResults(List<Media> results) {
    this.results = results;
  }

  public int getTotal_pages() {
    return total_pages;
  };

  public int getTotal_results() {
    return total_results;
  };

}