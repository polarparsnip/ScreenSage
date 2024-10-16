package is.hi.screensage_web_server.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;

import is.hi.screensage_web_server.entities.Review;
import is.hi.screensage_web_server.models.Media;
import is.hi.screensage_web_server.models.MediaDetailed;
import is.hi.screensage_web_server.models.ReviewRequest;

/**
 * MediaServiceInterface defines the contract for media-related services,
 * such as managing media-related operations.
 * 
 * Provides methods to retrieve media, get recommendations, manage genres, 
 * and handle reviews for various media types.
 */
public interface MediaServiceInterface {

  /**
   * Retrieves a list of media items based on user preferences and search criteria.
   *
   * @param userId the ID of the user requesting the media.
   * @param type the type of media (e.g., "movie" or "tv").
   * @param genreId the ID of the genre to filter media (optional).
   * @param page the page number for pagination.
   * @param searchQuery the search query to filter media items (optional).
   * @return a list of media items matching the specified criteria.
   * @throws Exception if an error occurs while retrieving the media.
   */
  public List<Media> getMedia(int userId, String type, String genreId, int page, String searchQuery) throws Exception;

  /**
   * Retrieves detailed information about a specific media item.
   *
   * @param userId the ID of the user requesting the media details.
   * @param type the type of media (e.g., "movie" or "tv").
   * @param mediaId the ID of the media item.
   * @return detailed information about the specified media item.
   */
  public MediaDetailed getSingleMedia(int userId, String type, int mediaId);

  /**
   * Retrieves a list of genres available for a specific type of media.
   *
   * @param type the type of media (e.g., "movie" or "tv").
   * @return a JSON string containing the genres available for the specified media type.
   */
  public String getGenres(String type);

  /**
   * Retrieves recommended media based on a specific media item and user preferences.
   *
   * @param type the type of media (e.g., "movie" or "tv").
   * @param mediaId the ID of the media item for which recommendations are requested.
   * @return a JSON string containing the recommended media.
   */
  public String getRecommendations(String type, int mediaId);
  
  /**
   * Retrieves a paginated list of reviews for a specific media item.
   *
   * @param type the type of media (e.g., "movie" or "tv").
   * @param mediaId the ID of the media item for which reviews are requested.
   * @param page the page number for pagination.
   * @return a paginated list of reviews for the specified media item.
   * @throws Exception if an error occurs while retrieving the reviews.
   */
  public Page<Review> getMediaReviews(String type, int mediaId, int page) throws Exception;

  /**
   * Posts a review for a specific media item.
   *
   * @param type the type of media (e.g., "movie" or "tv").
   * @param mediaId the ID of the media item being reviewed.
   * @param reviewRequest the review request containing the review details.
   * @return the posted review.
   * @throws Exception if an error occurs while posting the review.
   */
  public Review postMediaReview(String type, int mediaId, ReviewRequest reviewRequest) throws Exception;

}
