package is.hi.screensage_web_server.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the response for a media list, including pagination information and the list of media items.
 */
public class MediaPageResponse {
  int page;

  @JsonProperty("results")
  private List<Media> results;

  int total_pages;

  int total_results;

  /**
   * Retrieves the current page number of the media list.
   *
   * @return the current page number
   */
  public int getPage() {
    return page;
  };

  /**
   * Retrieves the list of media items in the response.
   *
   * @return a {@link List} of {@link Media} objects representing the media items
   */
  public List<Media> getResults() {
    return results;
  }

  /**
   * Sets the list of media items in the response.
   *
   * @param results a {@link List} of {@link Media} objects to be set as the media items
   */
  public void setResults(List<Media> results) {
    this.results = results;
  }

  /**
   * Retrieves the total number of pages available for the media list.
   *
   * @return the total number of pages
   */
  public int getTotal_pages() {
    return total_pages;
  };

  /**
   * Retrieves the total number of results available for the media list.
   *
   * @return the total number of results
   */
  public int getTotal_results() {
    return total_results;
  };
}