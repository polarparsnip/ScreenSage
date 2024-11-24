package is.hi.screensage_web_server.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@RestController
public class IndexController {

  @GetMapping("/")
  public Map<String, Object> getApiEndpoints() {

    List<Map<String, Object>> userEndpoints = List.of(
      Map.of(
          "path", "/register",
          "method", "POST",
          "description", "Register a new user"
      ),
      Map.of(
        "path", "/login",
        "method", "POST",
        "description", "Logs in user"
      ),
      Map.of(
        "path", "/users/profile",
        "method", "GET",
        "description", "Gets authenticated user profile information"
      ),
      Map.of(
        "path", "/users/profile/reviews",
        "method", "GET",
        "description", "Gets paginated list of all reviews by the authenticated user"
      ),
      Map.of(
        "path", "/users/profile/lists",
        "method", "GET",
        "description", "Gets paginated list of all media lists by the authenticated user"
      ),
      Map.of(
        "path", "/users/profile/watchlists",
        "method", "GET",
        "description", "Gets paginated list of all watchlists by the authenticated user"
      ),
      Map.of(
        "path", "/users/profile/username",
        "method", "PATCH",
        "description", "Updates username for authenticated user"
      ),
      Map.of(
        "path", "/users/profile/password",
        "method", "PATCH",
        "description", "Updates password for authenticated user"
      ),
      Map.of(
        "path", "/users/profile/image",
        "method", "PATCH",
        "description", "Updates profile image for authenticated user"
      ),
      Map.of(
        "path", "/users/search?query={queryString}",
        "method", "GET",
        "description", "Gets users whose username contain the query string"
      ),
      Map.of(
        "path", "/users/scoreboard",
        "method", "GET",
        "description", "Gets a paginated scoreboard of users ordered by their total challenge scores"
      )
    );

    List<Map<String, Object>> mediaEndpoints = List.of(
      // Movies Endpoints
      Map.of(
          "path", "/movies",
          "method", "GET",
          "description", "Get paginated list of all movies"
      ),
      Map.of(
          "path", "/movies/{id}",
          "method", "GET",
          "description", "Get movie details by ID"
      ),
      Map.of(
          "path", "/movies/{id}/reviews",
          "method", "GET",
          "description", "Get paginated list of all reviews for a movie"
      ),
      Map.of(
          "path", "/movies/{id}/reviews",
          "method", "POST",
          "description", "Add a review for a movie"
      ),
      Map.of(
        "path", "/movies/{id}/recommendations",
        "method", "GET",
        "description", "Get recommendations for a movie by ID"
      ),
      Map.of(
        "path", "/movies/{id}/likes",
        "method", "POST",
        "description", "Adds a like for a movie by ID"
      ),
      Map.of(
        "path", "/movies/genres",
        "method", "GET",
        "description", "Get all movie genres available"
      ),
      
      // Shows Endpoints
      Map.of(
          "path", "/shows",
          "method", "GET",
          "description", "Get paginated list of all shows"
      ),
      Map.of(
          "path", "/shows/{id}",
          "method", "GET",
          "description", "Get show details by ID"
      ),
      Map.of(
          "path", "/shows/{id}/reviews",
          "method", "GET",
          "description", "Get paginated list of all reviews for a show"
      ),
      Map.of(
          "path", "/shows/{id}/reviews",
          "method", "POST",
          "description", "Add a review for a show"
      ),
      Map.of(
        "path", "/shows/{id}/recommendations",
        "method", "GET",
        "description", "Get recommendations for a show by ID"
      ),
      Map.of(
        "path", "/shows/{id}/likes",
        "method", "POST",
        "description", "Adds a like for a show by ID"
      ),
      Map.of(
        "path", "/shows/genres",
        "method", "GET",
        "description", "Get all show genres available"
      ),
      
      // Anime Endpoints
      Map.of(
          "path", "/anime",
          "method", "GET",
          "description", "Get paginated list of all anime"
      ),
      Map.of(
          "path", "/anime/{id}",
          "method", "GET",
          "description", "Get anime details by ID"
      ),
      Map.of(
          "path", "/anime/{id}/reviews",
          "method", "GET",
          "description", "Get paginated list of all reviews for an anime"
      ),
      Map.of(
          "path", "/anime/{id}/reviews",
          "method", "POST",
          "description", "Add a review for an anime"
      ),
      Map.of(
        "path", "/anime/{id}/recommendations",
        "method", "GET",
        "description", "Get recommendations for a anime by ID"
      ),
      Map.of(
        "path", "/anime/{id}/likes",
        "method", "POST",
        "description", "Adds a like for a anime by ID"
      ),
      
      // Featured media and random quote
      Map.of(
        "path", "/featured",
        "method", "GET",
        "description", "Get a random featured media"
      ),
      Map.of(
        "path", "/quote",
        "method", "GET",
        "description", "Get a random media quote"
      )
    );

    List<Map<String, Object>> listEndpoints = List.of(
      Map.of(
          "path", "/lists",
          "method", "GET",
          "description", "Get paginated list of all media lists"
      ),
      Map.of(
        "path", "/lists/{id}",
        "method", "GET",
        "description", "Get media list details by ID"
      ),
      Map.of(
        "path", "/lists/{id}",
        "method", "PATCH",
        "description", "Updates media list details by ID"
      ),
      Map.of(
        "path", "/lists/{id}",
        "method", "DELETE",
        "description", "Deletes media list by ID"
      ),
      Map.of(
        "path", "/lists/{id}/likes",
        "method", "POST",
        "description", "Adds a like to a media list by ID"
      ),
      Map.of(
        "path", "/watchlists",
        "method", "POST",
        "description", "Creates a new watchlist"
      ),
      Map.of(
        "path", "/watchlists/{id}",
        "method", "Get",
        "description", "Get watchlist details by ID"
      ),
      Map.of(
        "path", "/watchlists/{id}",
        "method", "PATCH",
        "description", "Updates watchlist details by ID"
      ),
      Map.of(
        "path", "/watchlists/{id}",
        "method", "DELETE",
        "description", "Deletes watchlist by ID"
      ),
      Map.of(
        "path", "/watchlists/{id}/shared_with",
        "method", "PATCH",
        "description", "Updates watchlist visibility by ID"
      )
    );

    List<Map<String, Object>> challengeEndpoints = List.of(
      Map.of(
          "path", "/challenge",
          "method", "GET",
          "description", "Get daily challenge"
      ),
      Map.of(
        "path", "/challenge/{id}",
        "method", "POST",
        "description", "Submits an answer for a daily challenge by ID"
      ),
      Map.of(
        "path", "/challenge/{id}/has-completed",
        "method", "GET",
        "description", "Checks whether a user has completed a challenge by ID"
      )
    );

    List<Map<String, Object>> endpoints = List.of(
      Map.of("users", userEndpoints),
      Map.of("media", mediaEndpoints),
      Map.of("lists", listEndpoints),
      Map.of("challenge", challengeEndpoints)
    );

    return Map.of("endpoints", endpoints);
  }
}
