package is.hi.screensage_web_server.models;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

import is.hi.screensage_web_server.entities.Review;

/**
 * The payload for detailed media.
 */
// @JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaDetailed {
  private double average_rating;
  private double user_rating;
  private List<Review> recent_reviews;

  private int id;
  private String title;
  private String original_title;
  private String original_language;
  private String overview;
  private String backdrop_path;
  private String poster_path;
  private double popularity;
  private boolean adult;
  private Collection belongs_to_collection;
  private int budget;
  private List<Genre> genres;
  private String homepage;
  private String imdb_id;
  private List<String> origin_country;
  private List<ProductionCompany> production_companies;
  private List<ProductionCountry> production_countries;
  private String release_date;
  private long revenue;
  private int runtime;
  private List<SpokenLanguage> spoken_languages;
  private String status;
  private String tagline;
  private boolean video;
  private Videos videos;

  // private double vote_average;
  // private int vote_count;

  // tv variables
  private String name;
  private String original_name;
  private String first_air_date;
  private int number_of_episodes;
  private int number_of_seasons;
  
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

  public List<Review> getRecent_reviews() {
    return recent_reviews;
  }
  
  public void setRecent_reviews(List<Review> recent_reviews) {
    this.recent_reviews = recent_reviews;
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

  public Collection getBelongs_to_collection() {
    return belongs_to_collection;
  }

  public void setBelongs_to_collection(Collection belongs_to_collection) {
    this.belongs_to_collection = belongs_to_collection;
  }

  public int getBudget() {
    return budget;
  }

  public void setBudget(int budget) {
    this.budget = budget;
  }

  public List<Genre> getGenres() {
    return genres;
  }

  public void setGenres(List<Genre> genres) {
    this.genres = genres;
  }

  public String getHomepage() {
    return homepage;
  }

  public void setHomepage(String homepage) {
    this.homepage = homepage;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getImdb_id() {
    return imdb_id;
  }

  public void setImdb_id(String imdb_id) {
    this.imdb_id = imdb_id;
  }

  public List<String> getOrigin_country() {
    return origin_country;
  }

  public void setOrigin_country(List<String> origin_country) {
    this.origin_country = origin_country;
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

  public List<ProductionCompany> getProduction_companies() {
    return production_companies;
  }

  public void setProduction_companies(List<ProductionCompany> production_companies) {
    this.production_companies = production_companies;
  }

  public List<ProductionCountry> getProduction_countries() {
    return production_countries;
  }

  public void setProduction_countries(List<ProductionCountry> production_countries) {
    this.production_countries = production_countries;
  }

  public String getRelease_date() {
    return release_date;
  }

  public void setRelease_date(String release_date) {
    this.release_date = release_date;
  }

  public long getRevenue() {
    return revenue;
  }

  public void setRevenue(long revenue) {
    this.revenue = revenue;
  }

  public int getRuntime() {
    return runtime;
  }

  public void setRuntime(int runtime) {
    this.runtime = runtime;
  }

  public List<SpokenLanguage> getSpoken_languages() {
    return spoken_languages;
  }

  public void setSpoken_languages(List<SpokenLanguage> spoken_languages) {
    this.spoken_languages = spoken_languages;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTagline() {
    return tagline;
  }

  public void setTagline(String tagline) {
    this.tagline = tagline;
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


  public Videos getVideos() {
    return videos;
  }

  public void setVideos(Videos videos) {
    this.videos = videos;
  }

  // tv getters and setters
  
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

  public int getNumber_of_episodes() {
    return number_of_episodes;
  }
  
  public void setNumber_of_episodes(int number_of_episodes) {
    this.number_of_episodes = number_of_episodes;
  }

  public int getNumber_of_seasons() {
    return number_of_seasons;
  }
  
  public void setNumber_of_seasons(int number_of_seasons) {
    this.number_of_seasons = number_of_seasons;
  }
}

// Additional classes for nested objects

class Collection {
  private int id;
  private String name;
  private String poster_path;
  private String backdrop_path;

  // Getters and Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPoster_path() {
    return poster_path;
  }

  public void setPoster_path(String poster_path) {
    this.poster_path = poster_path;
  }

  public String getBackdrop_path() {
    return backdrop_path;
  }

  public void setBackdrop_path(String backdrop_path) {
    this.backdrop_path = backdrop_path;
  }
}

class Genre {
  private int id;
  private String name;

  // Getters and Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

class ProductionCompany {
  private int id;
  private String logo_path;
  private String name;
  private String origin_country;

  // Getters and Setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getLogo_path() {
    return logo_path;
  }

  public void setLogo_path(String logo_path) {
    this.logo_path = logo_path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOrigin_country() {
    return origin_country;
  }

  public void setOrigin_country(String origin_country) {
    this.origin_country = origin_country;
  }
}

class ProductionCountry {
  private String iso_3166_1;
  private String name;

  // Getters and Setters
  public String getIso_3166_1() {
    return iso_3166_1;
  }

  public void setIso_3166_1(String iso_3166_1) {
    this.iso_3166_1 = iso_3166_1;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

class SpokenLanguage {
  private String english_name;
  private String iso_639_1;
  private String name;

  // Getters and Setters
  public String getEnglish_name() {
    return english_name;
  }

  public void setEnglish_name(String english_name) {
    this.english_name = english_name;
  }

  public String getIso_639_1() {
    return iso_639_1;
  }

  public void setIso_639_1(String iso_639_1) {
    this.iso_639_1 = iso_639_1;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

class Videos {
  private List<Video> results;

  // Getters and Setters
  public List<Video> getResults() {
    return results;
  }

  public void setResults(List<Video> results) {
    this.results = results;
  }
}

class Video {
  private String iso_639_1;
  private String iso_3166_1;
  private String name;
  private String key;
  private String site;
  private int size;
  private String type;
  private boolean official;
  private String published_at;
  private String id;

  // Getters and Setters
  public String getIso_639_1() {
    return iso_639_1;
  }

  public void setIso_639_1(String iso_639_1) {
    this.iso_639_1 = iso_639_1;
  }

  public String getIso_3166_1() {
    return iso_3166_1;
  }

  public void setIso_3166_1(String iso_3166_1) {
    this.iso_3166_1 = iso_3166_1;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getSite() {
    return site;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isOfficial() {
    return official;
  }

  public void setOfficial(boolean official) {
    this.official = official;
  }

  public String getPublished_at() {
    return published_at;
  }

  public void setPublished_at(String published_at) {
    this.published_at = published_at;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}