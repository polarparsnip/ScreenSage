package is.hi.screensage_web_server.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import is.hi.screensage_web_server.config.CustomExceptions.ResourceNotFoundException;
import is.hi.screensage_web_server.entities.Like;
import is.hi.screensage_web_server.entities.Quote;
import is.hi.screensage_web_server.entities.Review;
import is.hi.screensage_web_server.entities.Users;
import is.hi.screensage_web_server.interfaces.MediaServiceInterface;
import is.hi.screensage_web_server.interfaces.UserServiceInterface;
import is.hi.screensage_web_server.models.Media;
import is.hi.screensage_web_server.models.MediaDetailed;
import is.hi.screensage_web_server.models.MediaPageResponse;
import is.hi.screensage_web_server.models.ReviewRequest;
import is.hi.screensage_web_server.repositories.LikeRepository;
import is.hi.screensage_web_server.repositories.QuoteRepository;
import is.hi.screensage_web_server.repositories.ReviewRepository;
import jakarta.transaction.Transactional;


@Service
public class MediaService implements MediaServiceInterface {
  @Autowired
  private TmdbService tmdbService;

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private QuoteRepository quoteRepository;

  @Autowired
  private LikeRepository likeRepository;

  @Lazy
  @Autowired
  private UserServiceInterface userService;

  @Override
  public MediaPageResponse getMedia(
    int userId,
    String type, 
    String genreId, 
    int page, 
    String searchQuery
  ) throws JsonMappingException, JsonProcessingException {

    MediaPageResponse mediaListResponse = tmdbService.getMedia(type, genreId, page, searchQuery);
    List<Media> mediaList = mediaListResponse.getResults();

    String mediaType = type == "anime" ? "tv" : type;

    for (Media media : mediaList) {
      Double averageRating = reviewRepository.getAverageRatingForMedia(mediaType, media.getId());
      if (averageRating != null) {
        media.setAverage_rating(averageRating.doubleValue());
      }
      Double userRating = reviewRepository.getRatingByUserIdAndMediaIdAndType(userId, mediaType, media.getId());
      if (userRating != null) {
        media.setUser_rating(userRating.doubleValue());
      }
    }

    mediaListResponse.setResults(mediaList);

    return mediaListResponse;
  }

  @Override
  public MediaDetailed getSingleMedia(int userId, String type, int mediaId) {
    MediaDetailed media = tmdbService.getSingleMedia(type, mediaId);

    Double averageRating = reviewRepository.getAverageRatingForMedia(type, media.getId());
    if (averageRating != null) {
      media.setAverage_rating(averageRating.doubleValue());
    }
    Double userRating = reviewRepository.getRatingByUserIdAndMediaIdAndType(userId, type, media.getId());
    if (userRating != null) {
      media.setUser_rating(userRating.doubleValue());
    }

    long likeCount = likeRepository.countByTypeAndMediaId(type, mediaId);
    media.setLike_count(likeCount);

    boolean userHasLiked = likeRepository.existsByUser_IdAndTypeAndMediaId(userId, type, mediaId);
    media.setUser_has_liked(userHasLiked);

    Pageable pageable = PageRequest.of(0, 10);
    List<Review> recentReviews = reviewRepository.getRecentReviewsForMedia(type, mediaId, pageable);
    if (recentReviews != null) {
      media.setRecent_reviews(recentReviews);
    }

    return media;
  }

  @Override
  public String getGenres(String type) {

    String genres = tmdbService.getGenres(type.equals("movie") ? "movie" : "tv");

    if (type.equals("anime")) {
      try {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = (ObjectNode) objectMapper.readTree(genres);
        ArrayNode genresArray = (ArrayNode) rootNode.get("genres");

        List<Integer> excludedGenres = Arrays.asList(16, 37, 99, 10763, 10764, 10767);
        ArrayNode filteredGenres = objectMapper.createArrayNode();

        for (JsonNode genre : genresArray) {
          if (!excludedGenres.contains(genre.get("id").asInt())) {
            filteredGenres.add(genre);
          }
        }

        rootNode.set("genres", filteredGenres);
        return objectMapper.writeValueAsString(rootNode);

      } catch (Exception e) {
        System.out.println("Error occured while parsing genres: " + e.getMessage());
        return genres;
      }
    }

    return genres;
  }

  @Override
  public String getRecommendations(String type, int mediaId) {
    String recommendations = tmdbService.getMediaList(type, mediaId, "/recommendations");
    return recommendations;
  }

  @Override
  public Page<Review> getMediaReviews(String type, int mediaId, int page) throws Exception {
    int pageSize = 10;
    PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
    // return reviewRepository.findByMediaIdAndType(type, mediaId, pageRequest);
    
    Page<Review> reviews;
    try {
      reviews = reviewRepository.findByTypeAndMediaId(type, mediaId, pageRequest);
    } catch (Exception e) {
      System.out.println("Could not find reviews: " + e.getMessage());
      throw new Exception("Could not find reviews.");
    }
    return reviews;
  }

  @Override
  public Review postMediaReview(int userId, String type, int mediaId, ReviewRequest reviewRequest) throws Exception {

    if (!userService.userExists(userId)) {
      System.out.println("Error: User not found");
      throw new ResourceNotFoundException("User not found");
    }
    try {
      Double userRating = reviewRepository.getRatingByUserIdAndMediaIdAndType(userId, type, mediaId);
      if (userRating != null) {
        reviewRepository.deleteReviewByUserIdAndMediaIdAndType(userId, type, mediaId);
      }
    } catch (Exception e) {
      System.out.println("Error occured while getting previous review data: " + e.getMessage());
      throw new Exception("Error occurred while getting previous review data.");
    }

    Users user = userService.findUser(userId);
    if (user == null) {
      System.out.println("Error: User not found");
      throw new ResourceNotFoundException("User not found");
    }

    double rating = reviewRequest.getRating();
    String content = reviewRequest.getContent();

    Review review = new Review(mediaId, user, rating, content, type);

    try {
      reviewRepository.save(review);
    } catch (Exception e) {
      System.out.println("Error occured while saving review: " + e.getMessage());
      throw new Exception("Error occurred while saving review.");
    }

    return review;
  }

  @Override
  public Page<Review> getUserReviews(int userId, int page, int pageSize) {
    PageRequest pageRequest = PageRequest.of(page - 1, pageSize);

    Page<Review> userReviews;
    try {
      userReviews = reviewRepository.getUserReviews(userId, pageRequest);
    } catch (Exception e) {
      System.out.println("Could not find user reviews: " + e.getMessage());
      throw new ResourceNotFoundException("Could not find user reviews.");
    }
    return userReviews;
  }

  @Override
  public MediaDetailed getRandomMedia() {
    MediaDetailed randomMedia = tmdbService.getRandomMedia();
    return randomMedia;
  }
  
  @Override
  public Quote getRandomQuote() {
    Quote randomQuote = quoteRepository.getRandomQuote();
    return randomQuote;
  }

  @Override
  @Transactional
  public void toggleMediaLike(int userId, String type, int mediaId) throws Exception {
    Users user = userService.findUser(userId);
    if (user == null) {
      System.out.println("Error: User not found");
      throw new ResourceNotFoundException("User not found");
    }

    boolean userHasLiked = likeRepository.existsByUser_IdAndTypeAndMediaId(userId, type, mediaId);

    try {
      if (userHasLiked) {
        likeRepository.deleteByUserAndTypeAndMediaId(user, type, mediaId);
      } else {
        likeRepository.save(new Like(user, type, mediaId));
      }
    } catch (Exception e) {
      System.out.println("Error occurred while updating media like information: " + e.getMessage());
      throw new RuntimeException("Error occurred while updating media like information.");
    }
  }

}
