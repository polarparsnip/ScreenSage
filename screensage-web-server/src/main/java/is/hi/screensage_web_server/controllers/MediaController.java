package is.hi.screensage_web_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import is.hi.screensage_web_server.entities.Quote;
import is.hi.screensage_web_server.entities.Review;
import is.hi.screensage_web_server.interfaces.MediaServiceInterface;
import is.hi.screensage_web_server.models.MediaDetailed;
import is.hi.screensage_web_server.models.MediaPageResponse;
import is.hi.screensage_web_server.models.ReviewRequest;
import is.hi.screensage_web_server.models.UserPrincipal;


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
   * @return {@link ResponseEntity} containing a list of movies.
   * @throws Exception if an error occurs while retrieving the movies.
   */
  @GetMapping("/movies")
  public ResponseEntity<?> getMovies(
    @RequestParam(required = false) String genreId,
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(name = "search", required = false) String searchQuery
  ) throws Exception {
    
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    MediaPageResponse movies = mediaService.getMedia(userId, "movie", genreId, page, searchQuery);
    System.out.println(movies);
    return ResponseEntity.ok(movies);
  }

  /**
   * Retrieves a paginated list of shows based on user preferences and search criteria.
   *
   * @param genreId the ID of the genre to filter shows (optional).
   * @param page the page number for pagination (default is 1).
   * @param searchQuery the search query to filter shows (optional).
   * @param userId the ID of the user requesting the shows (required).
   * @return {@link ResponseEntity} containing a list of shows.
   * @throws Exception if an error occurs while retrieving the shows.
   */
  @GetMapping("/shows")
  public ResponseEntity<?> getShows(
    @RequestParam(required = false) String genreId,
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(name = "search", required = false) String searchQuery
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    MediaPageResponse shows = mediaService.getMedia(userId, "tv", genreId, page, searchQuery);
    return ResponseEntity.ok(shows);
  }

  /**
   * Retrieves a paginated list of anime based on user preferences and search criteria.
   *
   * @param genreId the ID of the genre to filter anime (optional).
   * @param page the page number for pagination (default is 1).
   * @param searchQuery the search query to filter anime (optional).
   * @param userId the ID of the user requesting the anime (required).
   * @return {@link ResponseEntity} containing a list of anime.
   * @throws Exception if an error occurs while retrieving the list of anime.
   */
  @GetMapping("/anime")
  public ResponseEntity<?> getAnime(
    @RequestParam(required = false) String genreId,
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(name = "search", required = false) String searchQuery
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    MediaPageResponse animeList = mediaService.getMedia(userId, "anime", genreId, page, searchQuery);
    return ResponseEntity.ok(animeList);
  }

  /**
   * Retrieves detailed information about a specific movie.
   *
   * @param movieId the ID of the movie being requested (required).
   * @param userId the ID of the user requesting the movie details (required).
   * @return {@link ResponseEntity} containing the detailed information of the movie.
   */
  @GetMapping("/movies/{movieId}")
  public ResponseEntity<?> getMovie(
    @PathVariable int movieId
  ) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    MediaDetailed movie = mediaService.getSingleMedia(userId, "movie", movieId);
    return ResponseEntity.ok(movie);
  }

  /**
   * Retrieves detailed information about a specific show.
   *
   * @param showId the ID of the show being requested (required).
   * @param userId the ID of the user requesting the show details (required).
   * @return {@link ResponseEntity} containing the detailed information of the show.
   */
  @GetMapping("/shows/{showId}")
  public ResponseEntity<?> getShow(
    @PathVariable int showId
  ) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    MediaDetailed show = mediaService.getSingleMedia(userId, "tv", showId);
    return ResponseEntity.ok(show);
  }

  /**
   * Retrieves detailed information about a specific anime.
   *
   * @param animeId the ID of the anime being requested (required).
   * @param userId the ID of the user requesting the anime details (required).
   * @return {@link ResponseEntity} containing the detailed information of the anime.
   */
  @GetMapping("/anime/{animeId}")
  public ResponseEntity<?> getSingleAnime(
    @PathVariable int animeId
  ) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();
    
    MediaDetailed anime = mediaService.getSingleMedia(userId, "tv", animeId);
    return ResponseEntity.ok(anime);
  }

  /**
   * Retrieves a list of genres available for movies.
   *
   * @return {@link ResponseEntity} containing the available genres for movies.
   */
  @GetMapping("/movies/genres")
  public ResponseEntity<?> getMovieGenres() {
    String genres = mediaService.getGenres("movie");
    return ResponseEntity.ok(genres);
  }

  /**
   * Retrieves a list of genres available for shows.
   *
   * @return {@link ResponseEntity} containing the available genres for shows.
   */
  @GetMapping("/shows/genres")
  public ResponseEntity<?> getShowGenres() {
    String genres = mediaService.getGenres("tv");
    return ResponseEntity.ok(genres);
  }

  /**
   * Retrieves a list of genres available for anime.
   *
   * @return {@link ResponseEntity} containing the available genres for anime.
   */
  @GetMapping("/anime/genres")
  public ResponseEntity<?> getAnimeGenres() {
    String genres = mediaService.getGenres("anime");
    return ResponseEntity.ok(genres);
  }

  /**
   * Retrieves recommendations for a specific movie based on its ID.
   *
   * @param movieId the ID of the movie for which recommendations are requested (required).
   * @return {@link ResponseEntity} containing the recommendations for the specified movie.
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
   * @return {@link ResponseEntity} containing the recommendations for the specified show.
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
   * @return {@link ResponseEntity} containing the recommendations for the specified anime.
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
   * @return {@link ResponseEntity} containing a paginated list of reviews for the specified movie.
   * @throws Exception if an error occurs while retrieving the movie reviews.
   */
  @GetMapping("/movies/{movieId}/reviews")
  public ResponseEntity<?> getMovieReviews(
    @PathVariable int movieId,
    @RequestParam(defaultValue = "1") int page
  ) throws Exception {
    Page<Review> reviewList = mediaService.getMediaReviews("movie", movieId, page);
    return ResponseEntity.ok(reviewList);
  }

  /**
   * Retrieves a paginated list of reviews for a specific show.
   *
   * @param showId the ID of the show for which reviews are requested (required).
   * @param page the page number for pagination (default is 1).
   * @return {@link ResponseEntity} containing a paginated list of reviews for the specified show.
   * @throws Exception if an error occurs while retrieving the show reviews.
   */
  @GetMapping("/shows/{showId}/reviews")
  public ResponseEntity<?> getShowReviews(
    @PathVariable int showId,
    @RequestParam(defaultValue = "1") int page
  ) throws Exception {
    Page<Review> reviewList = mediaService.getMediaReviews("tv", showId, page);
    return ResponseEntity.ok(reviewList);
  }

  /**
   * Retrieves a paginated list of reviews for a specific anime.
   *
   * @param animeId the ID of the anime for which reviews are requested (required).
   * @param page the page number for pagination (default is 1).
   * @return {@link ResponseEntity} containing a paginated list of reviews for the specified anime.
   * @throws Exception if an error occurs while retrieving the anime reviews.
   */
  @GetMapping("/anime/{animeId}/reviews")
  public ResponseEntity<?> getAnimeReviews(
    @PathVariable int animeId,
    @RequestParam(defaultValue = "1") int page
  ) throws Exception {
    Page<Review> reviewList = mediaService.getMediaReviews("tv", animeId, page);
    return ResponseEntity.ok(reviewList);
  }

  /**
   * Posts a review for a specific movie.
   *
   * @param movieId the ID of the movie being reviewed (required).
   * @param reviewRequest the request body containing review details (required).
   * @return {@link ResponseEntity} containing the posted review.
   * @throws Exception if an error occurs while saving the movie review.
   */
  @PostMapping("/movies/{movieId}/reviews")
  public ResponseEntity<?> postMovieReview(
    @PathVariable int movieId, 
    @RequestBody ReviewRequest reviewRequest,
    @RequestParam(defaultValue = "Movie") String title
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();
    Review newReview = mediaService.postMediaReview(userId, "movie", movieId, title, reviewRequest);
    return ResponseEntity.ok(newReview);
  }

  /**
   * Posts a review for a specific show.
   *
   * @param showId the ID of the show being reviewed (required).
   * @param reviewRequest the request body containing review details (required).
   * @return {@link ResponseEntity} containing the posted review.
   * @throws Exception if an error occurs while saving the show review.
   */
  @PostMapping("/shows/{showId}/reviews")
  public ResponseEntity<?> postShowReview(
    @PathVariable int showId, 
    @RequestBody ReviewRequest reviewRequest,
    @RequestParam(defaultValue = "Show") String title
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();
    Review newReview = mediaService.postMediaReview(userId, "tv", showId, title, reviewRequest);
    return ResponseEntity.ok(newReview);
  }

  /**
   * Posts a review for a specific anime.
   *
   * @param animeId the ID of the anime being reviewed (required).
   * @param reviewRequest the request body containing review details (required).
   * @return {@link ResponseEntity} containing the posted review.
   * @throws Exception if an error occurs while saving the anime review.
   */
  @PostMapping("/anime/{animeId}/reviews")
  public ResponseEntity<?> postAnimeeview(
    @PathVariable int animeId, 
    @RequestBody ReviewRequest reviewRequest,
    @RequestParam(defaultValue = "Anime") String title
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();
    Review newReview = mediaService.postMediaReview(userId, "tv", animeId, title, reviewRequest);
    return ResponseEntity.ok(newReview);
  }

    /**
   * Retrieves a random media item to be featured.
   *
   * @return {@link ResponseEntity} containing the random media item.
   */
  @GetMapping("/featured")
  public ResponseEntity<?> getRandomMedia() {
    MediaDetailed randomMedia = mediaService.getRandomMedia();
    return ResponseEntity.ok(randomMedia);
  }

  /**
   * Retrieves a random media quote.
   *
   * @return {@link ResponseEntity} containing the random quote.
   */
  @GetMapping("/quote")
  public ResponseEntity<?> getRandomQuote() {
    Quote randomQuote = mediaService.getRandomQuote();
    return ResponseEntity.ok(randomQuote);
  }

  /**
   * Adds a like by the authenticated user for the movie with the specified ID.
   *
   * @return                 a ResponseEntity containing a success message if like was successful
   * @throws Exception       if an error occurs while adding the like
   */
  @PostMapping("/movies/{movieId}/likes")
  public ResponseEntity<?> postMovieLike(
    @PathVariable int movieId
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    mediaService.toggleMediaLike(userId, "movie", movieId);
    return ResponseEntity.ok("Like status updated");
  }

  /**
   * Adds a like by the authenticated user for the show with the specified ID.
   *
   * @return                 a ResponseEntity containing a success message if like was successful
   * @throws Exception       if an error occurs while adding the like
   */
  @PostMapping("/shows/{showId}/likes")
  public ResponseEntity<?> postShowLike(
    @PathVariable int showId
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    mediaService.toggleMediaLike(userId, "tv", showId);
    return ResponseEntity.ok("Like status updated");
  }

  /**
   * Adds a like by the authenticated user for the anime with the specified ID.
   *
   * @return                 a ResponseEntity containing a success message if like was successful
   * @throws Exception       if an error occurs while adding the like
   */
  @PostMapping("/anime/{animeId}/likes")
  public ResponseEntity<?> postAnimeLike(
    @PathVariable int animeId
  ) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserPrincipal authenticatedUser = (UserPrincipal) authentication.getPrincipal();
    int userId = authenticatedUser.getId();

    mediaService.toggleMediaLike(userId, "tv", animeId);
    return ResponseEntity.ok("Like status updated");
  }

}
