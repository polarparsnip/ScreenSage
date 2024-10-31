package is.hi.screensage_web_server.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import is.hi.screensage_web_server.config.CustomExceptions.ResourceNotFoundException;
import is.hi.screensage_web_server.entities.Review;
import is.hi.screensage_web_server.entities.Users;
import is.hi.screensage_web_server.interfaces.MediaServiceInterface;
import is.hi.screensage_web_server.interfaces.UserServiceInterface;
import is.hi.screensage_web_server.models.Media;
import is.hi.screensage_web_server.models.MediaDetailed;
import is.hi.screensage_web_server.models.MediaPageResponse;
import is.hi.screensage_web_server.models.ReviewRequest;
import is.hi.screensage_web_server.repositories.ReviewRepository;


@Service
public class MediaService implements MediaServiceInterface {
  @Autowired
  private TmdbService tmdbService;

  @Autowired
  private ReviewRepository reviewRepository;

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

    for (Media media : mediaList) {
      Double averageRating = reviewRepository.getAverageRatingForMedia(type, media.getId());
      if (averageRating != null) {
        media.setAverage_rating(averageRating.doubleValue());
      }
      Double userRating = reviewRepository.getRatingByUserIdAndMediaIdAndType(userId, type, media.getId());
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

    Pageable pageable = PageRequest.of(0, 10);
    List<Review> recentReviews = reviewRepository.getRecentReviewsForMedia(type, mediaId, pageable);
    if (recentReviews != null) {
      media.setRecent_reviews(recentReviews);
    }

    return media;
  }

  @Override
  public String getGenres(String type) {
    String genres = tmdbService.getGenres(type);
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

}
