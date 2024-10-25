package is.hi.screensage_web_server.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * The payload for media.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Media {
  private double average_rating;
  private double user_rating;

  private int id;
  private String title;
  private String original_title;
  private String original_language;
  private String overview;
  private boolean adult;
  private List<Integer> genre_ids;
  private double popularity;
  private String backdrop_path;
  private String poster_path;
  private String release_date;
  private boolean video;

  // private double vote_average;
  // private int vote_count;

  // tv variables
  private List<String> origin_country;
  private String name;
  private String original_name;
  private String first_air_date;

  // Getters and Setters
  
  public double getAverage_rating() {
    return average_rating;
  }
  
  public void setAverage_rating(double average_rating) {
    this.average_rating = average_rating;
  }

  public double getUser_rating() {
    return user_rating;
  }
  
  public void setUser_rating(double user_rating) {
    this.user_rating = user_rating;
  }
  

  // public double getVote_average() {
  //   return vote_average;
  // }

  // public void setVote_average(double vote_average) {
  //   this.vote_average = vote_average;
  // }

  // public int getVote_count() {
  //   return vote_count;
  // }

  // public void setVote_count(int vote_count) {
  //   this.vote_count = vote_count;
  // }

  public boolean isAdult() {
    return adult;
  }

  public void setAdult(boolean adult) {
    this.adult = adult;
  }

  public String getBackdrop_path() {
    return backdrop_path;
  }

  public void setBackdrop_path(String backdrop_path) {
    this.backdrop_path = backdrop_path;
  }

  public List<Integer> getGenre_ids() {
    return genre_ids;
  }

  public void setGenre_ids(List<Integer> genre_ids) {
    this.genre_ids = genre_ids;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getOriginal_language() {
    return original_language;
  }

  public void setOriginal_language(String original_language) {
    this.original_language = original_language;
  }

  public String getOriginal_title() {
    return original_title;
  }

  public void setOriginal_title(String original_title) {
    this.original_title = original_title;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public double getPopularity() {
    return popularity;
  }

  public void setPopularity(double popularity) {
    this.popularity = popularity;
  }

  public String getPoster_path() {
    return poster_path;
  }

  public void setPoster_path(String poster_path) {
    this.poster_path = poster_path;
  }

  public String getRelease_date() {
    return release_date;
  }

  public void setRelease_date(String release_date) {
    this.release_date = release_date;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isVideo() {
    return video;
  }

  public void setVideo(boolean video) {
    this.video = video;
  }

  // tv getters and setters

  public void setOrigin_country(List<String> origin_country) {
    this.origin_country = origin_country;
  }

  public List<String> getOrigin_country() {
    return origin_country;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOriginal_name() {
    return original_name;
  }

  public void setOriginal_name(String original_name) {
    this.original_name = original_name;
  }

  public String getFirst_air_date() {
    return first_air_date;
  }

  public void setFirst_air_date(String first_air_date) {
    this.first_air_date = first_air_date;
  }

}