package is.hi.screensage_web_server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import is.hi.screensage_web_server.entities.Review;
import is.hi.screensage_web_server.interfaces.MediaServiceInterface;
import is.hi.screensage_web_server.models.Media;
import is.hi.screensage_web_server.models.MediaDetailed;
import is.hi.screensage_web_server.models.ReviewRequest;


/**
 * REST controller for managing media-related operations, including movies, shows, and anime.
 * Provides endpoints for retrieving media, getting recommendations, managing genres, 
 * and handling reviews.
 */
@RestController
public class MediaController {

  @Autowired
  private MediaServiceInterface mediaService;

  /**
   * Retrieves a paginated list of movies based on user preferences and search criteria.
   *
   * @param genreId the ID of the genre to filter movies (optional).
   * @param page the page number for pagination (default is 1).
   * @param searchQuery the search query to filter movies (optional).
   * @param userId the ID of the user requesting the movies (required).
   * @return a ResponseEntity containing a list of movies.
   * @throws Exception if an error occurs while retrieving the movies.
   */
  @GetMapping("/movies")
  public ResponseEntity<?> getMovies(
    @RequestParam(required = false) String genreId,
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(required = false) String searchQuery,
    @RequestParam(required = true) int userId
  ) throws Exception {
    List<Media> movies = mediaService.getMedia(userId, "movie", genreId, page, searchQuery);
    return ResponseEntity.ok(movies);
  }

  /**
   * Retrieves a paginated list of shows based on user preferences and search criteria.
   *
   * @param genreId the ID of the genre to filter shows (optional).
   * @param page the page number for pagination (default is 1).
   * @param searchQuery the search query to filter shows (optional).
   * @param userId the ID of the user requesting the shows (required).
   * @return a ResponseEntity containing a list of shows.
   * @throws Exception if an error occurs while retrieving the shows.
   */
  @GetMapping("/shows")
  public ResponseEntity<?> getShows(
    @RequestParam(required = false) String genreId,
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(required = false) String searchQuery,
    @RequestParam(required = true) int userId
  ) throws Exception {
    List<Media> shows = mediaService.getMedia(userId, "tv", genreId, page, searchQuery);
    return ResponseEntity.ok(shows);
  }

  /**
   * Retrieves a paginated list of anime based on user preferences and search criteria.
   *
   * @param genreId the ID of the genre to filter anime (optional).
   * @param page the page number for pagination (default is 1).
   * @param searchQuery the search query to filter anime (optional).
   * @param userId the ID of the user requesting the anime (required).
   * @return a ResponseEntity containing a list of anime.
   * @throws Exception if an error occurs while retrieving the list of anime.
   */
  @GetMapping("/anime")
  public ResponseEntity<?> getAnime(
    @RequestParam(required = false) String genreId,
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(required = false) String searchQuery,
    @RequestParam(required = true) int userId
  ) throws Exception {
    List<Media> animeList = mediaService.getMedia(userId, "anime", genreId, page, searchQuery);
    return ResponseEntity.ok(animeList);
  }

  /**
   * Retrieves detailed information about a specific movie.
   *
   * @param movieId the ID of the movie being requested (required).
   * @param userId the ID of the user requesting the movie details (required).
   * @return a ResponseEntity containing the detailed information of the movie.
   */
  @GetMapping("/movies/{movieId}")
  public ResponseEntity<?> getMovie(
    @PathVariable int movieId,
    @RequestParam(value = "user_id", required = true) int userId
  ) {
    MediaDetailed movie = mediaService.getSingleMedia(userId, "movie", movieId);
    return ResponseEntity.ok(movie);
  }

  /**
   * Retrieves detailed information about a specific show.
   *
   * @param showId the ID of the show being requested (required).
   * @param userId the ID of the user requesting the show details (required).
   * @return a ResponseEntity containing the detailed information of the show.
   */
  @GetMapping("/shows/{showId}")
  public ResponseEntity<?> getShow(
    @PathVariable int showId,
    @RequestParam(value = "user_id", required = true) int userId
  ) {
    MediaDetailed show = mediaService.getSingleMedia(userId, "tv", showId);
    return ResponseEntity.ok(show);
  }

  /**
   * Retrieves detailed information about a specific anime.
   *
   * @param animeId the ID of the anime being requested (required).
   * @param userId the ID of the user requesting the anime details (required).
   * @return a ResponseEntity containing the detailed information of the anime.
   */
  @GetMapping("/anime/{animeId}")
  public ResponseEntity<?> getSingleAnime(
    @PathVariable int animeId,
    @RequestParam(value = "user_id", required = true) int userId
  ) {
    MediaDetailed anime = mediaService.getSingleMedia(userId, "tv", animeId);
    return ResponseEntity.ok(anime);
  }

  /**
   * Retrieves a list of genres available for movies.
   *
   * @return a ResponseEntity containing the available genres for movies.
   */
  @GetMapping("/movies/genres")
  public ResponseEntity<?> getMovieGenres() {
    String genres = mediaService.getGenres("movie");
    return ResponseEntity.ok(genres);
  }

  /**
   * Retrieves a list of genres available for shows.
   *
   * @return a ResponseEntity containing the available genres for shows.
   */
  @GetMapping("/shows/genres")
  public ResponseEntity<?> getShowGenres() {
    String genres = mediaService.getGenres("tv");
    return ResponseEntity.ok(genres);
  }

  /**
   * Retrieves recommendations for a specific movie based on its ID.
   *
   * @param movieId the ID of the movie for which recommendations are requested (required).
   * @return a ResponseEntity containing the recommendations for the specified movie.
   */
  @GetMapping("/movies/{movieId}/recommendations")
  public ResponseEntity<?> getMovieRecommendations(@PathVariable int movieId) {
    String recommendations = mediaService.getRecommendations("movie", movieId);
    return ResponseEntity.ok(recommendations);
  }

  /**
   * Retrieves recommendations for a specific show based on its ID.
   *
   * @param showId the ID of the show for which recommendations are requested (required).
   * @return a ResponseEntity containing the recommendations for the specified show.
   */
  @GetMapping("/shows/{showId}/recommendations")
  public ResponseEntity<?> getShowRecommendations(@PathVariable int showId) {
    String recommendations = mediaService.getRecommendations("tv", showId);
    return ResponseEntity.ok(recommendations);
  }

  /**
   * Retrieves recommendations for a specific anime based on its ID.
   *
   * @param animeId the ID of the anime for which recommendations are requested (required).
   * @return a ResponseEntity containing the recommendations for the specified anime.
   */
  @GetMapping("/anime/{animeId}/recommendations")
  public ResponseEntity<?> getAnimeRecommendations(@PathVariable int animeId) {
    String recommendations = mediaService.getRecommendations("tv", animeId);
    return ResponseEntity.ok(recommendations);
  }

  /**
   * Retrieves a paginated list of reviews for a specific movie.
   *
   * @param movieId the ID of the movie for which reviews are requested (required).
   * @param page the page number for pagination (default is 1).
   * @return a ResponseEntity containing a paginated list of reviews for the specified movie.
   * @throws Exception if an error occurs while retrieving the movie reviews.
   */
  @GetMapping("/movies/{movieId}/reviews")
  public ResponseEntity<?> getMovieReviews(
    @PathVariable int movieId,
    @RequestParam(defaultValue = "1") int page
  ) throws Exception {
    Page<Review> newReview = mediaService.getMediaReviews("movie", movieId, page);
    return ResponseEntity.ok(newReview);
  }

  /**
   * Retrieves a paginated list of reviews for a specific show.
   *
   * @param showId the ID of the show for which reviews are requested (required).
   * @param page the page number for pagination (default is 1).
   * @return a ResponseEntity containing a paginated list of reviews for the specified show.
   * @throws Exception if an error occurs while retrieving the show reviews.
   */
  @GetMapping("/shows/{showId}/reviews")
  public ResponseEntity<?> getShowReviews(
    @PathVariable int showId,
    @RequestParam(defaultValue = "1") int page
  ) throws Exception {
    Page<Review> newReview = mediaService.getMediaReviews("tv", showId, page);
    return ResponseEntity.ok(newReview);
  }

  /**
   * Retrieves a paginated list of reviews for a specific anime.
   *
   * @param animeId the ID of the anime for which reviews are requested (required).
   * @param page the page number for pagination (default is 1).
   * @return a ResponseEntity containing a paginated list of reviews for the specified anime.
   * @throws Exception if an error occurs while retrieving the anime reviews.
   */
  @GetMapping("/anime/{animeId}/reviews")
  public ResponseEntity<?> getAnimeReviews(
    @PathVariable int animeId,
    @RequestParam(defaultValue = "1") int page
  ) throws Exception {
    Page<Review> newReview = mediaService.getMediaReviews("tv", animeId, page);
    return ResponseEntity.ok(newReview);
  }

  /**
   * Posts a review for a specific movie.
   *
   * @param movieId the ID of the movie being reviewed (required).
   * @param reviewRequest the request body containing review details (required).
   * @return a ResponseEntity containing the posted review.
   * @throws Exception if an error occurs while saving the movie review.
   */
  @PostMapping("/movies/{movieId}/reviews")
  public ResponseEntity<?> postMovieReview(
    @PathVariable int movieId, 
    @RequestBody ReviewRequest reviewRequest
  ) throws Exception {
    Review newReview = mediaService.postMediaReview("movie", movieId, reviewRequest);
    return ResponseEntity.ok(newReview);
  }

  /**
   * Posts a review for a specific show.
   *
   * @param showId the ID of the show being reviewed (required).
   * @param reviewRequest the request body containing review details (required).
   * @return a ResponseEntity containing the posted review.
   * @throws Exception if an error occurs while saving the show review.
   */
  @PostMapping("/shows/{showId}/reviews")
  public ResponseEntity<?> postShowReview(
    @PathVariable int showId, 
    @RequestBody ReviewRequest reviewRequest
  ) throws Exception {
    Review newReview = mediaService.postMediaReview("tv", showId, reviewRequest);
    return ResponseEntity.ok(newReview);
  }

  /**
   * Posts a review for a specific anime.
   *
   * @param animeId the ID of the anime being reviewed (required).
   * @param reviewRequest the request body containing review details (required).
   * @return a ResponseEntity containing the posted review.
   * @throws Exception if an error occurs while saving the anime review.
   */
  @PostMapping("/anime/{animeId}/reviews")
  public ResponseEntity<?> postAnimeeview(
    @PathVariable int animeId, 
    @RequestBody ReviewRequest reviewRequest
  ) throws Exception {
    Review newReview = mediaService.postMediaReview("tv", animeId, reviewRequest);
    return ResponseEntity.ok(newReview);
  }

}
