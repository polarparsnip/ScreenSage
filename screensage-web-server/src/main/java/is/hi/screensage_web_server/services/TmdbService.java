package is.hi.screensage_web_server.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import is.hi.screensage_web_server.config.CustomExceptions.ResourceNotFoundException;
import is.hi.screensage_web_server.models.MediaDetailed;
import is.hi.screensage_web_server.models.MediaPageResponse;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

@Service
public class TmdbService {
  
  private RestTemplate restTemplate = new RestTemplate();

  @Value("${tmdb.api.key}")
  private String tmdbApiKey;

  public MediaPageResponse getMedia(String type, String genreId, int page, String searchQuery) throws JsonMappingException, JsonProcessingException {
    String url;

    if (searchQuery != null && !searchQuery.isEmpty()) {
      url = UriComponentsBuilder.fromHttpUrl(String.format("https://api.themoviedb.org/3/search/%s", type))
        .queryParam("query", searchQuery)
        .queryParam("page", page)
        .queryParam("api_key", tmdbApiKey)
        .toUriString();
    } 

    else if (type == "anime") {
      String genreIds = "16";
      
      if (genreId != null) {
        genreIds = "16," + genreId;
      } else {
        genreIds = "16,10759";
      }

      url = UriComponentsBuilder.fromHttpUrl(String.format("https://api.themoviedb.org/3/discover/tv"))
        .queryParam("with_genres", genreIds)
        .queryParam("origin_country", "JP")
        .queryParam("with_original_language", "ja")
        .queryParam("page", page)
        .queryParam("api_key", tmdbApiKey)
        .toUriString();
    }
    
    else if (genreId != null) {
      try {
        Integer.parseInt(genreId);
        url = UriComponentsBuilder.fromHttpUrl(String.format("https://api.themoviedb.org/3/discover/%s", type))
          .queryParam("with_genres", genreId)
          .queryParam("page", page)
          .queryParam("api_key", tmdbApiKey)
          .toUriString();
      } catch (NumberFormatException e) {
        url = UriComponentsBuilder.fromHttpUrl(String.format("https://api.themoviedb.org/3/%s/%s", type, genreId))
          .queryParam("page", page)
          .queryParam("api_key", tmdbApiKey)
          .toUriString();
      }
    } else {
      url = UriComponentsBuilder.fromHttpUrl(String.format("https://api.themoviedb.org/3/discover/%s", type))
        .queryParam("page", page)
        .queryParam("api_key", tmdbApiKey)
        .queryParam("with_original_language", "en")
        .toUriString();
    }

    String response = restTemplate.getForObject(url, String.class);
  
    ObjectMapper objectMapper = new ObjectMapper();
    MediaPageResponse mediaResponse = objectMapper.readValue(response, MediaPageResponse.class);
    
    return mediaResponse;
  }

  public MediaDetailed getSingleMedia(String type, int id) {
    String url = UriComponentsBuilder.fromHttpUrl(String.format("https://api.themoviedb.org/3/%s/%d",type, id))
      // .queryParam("append_to_response", "videos,credits")
      .queryParam("append_to_response", "videos")
      .queryParam("api_key", tmdbApiKey)
      .toUriString();

    // String response = restTemplate.getForObject(url, String.class);
    // ObjectMapper objectMapper = new ObjectMapper();
    // MediaDetailed media = objectMapper.readValue(response, MediaDetailed.class);

    MediaDetailed media = restTemplate.getForObject(url, MediaDetailed.class);

    return media;
  }

  public String getMediaList(String type, int mediaId, String list) {
    String url = UriComponentsBuilder.fromHttpUrl(String.format("https://api.themoviedb.org/3/%s/%d/%s", type, mediaId, list))
      .queryParam("api_key", tmdbApiKey)
      .toUriString();
    String recommendations = restTemplate.getForObject(url, String.class);
    return recommendations;
  }

  public String getGenres(String type) {
    String url = UriComponentsBuilder.fromHttpUrl(String.format("https://api.themoviedb.org/3/genre/%s/list", type))
      .queryParam("api_key", tmdbApiKey)
      .toUriString();
    String genres = restTemplate.getForObject(url, String.class);
    return genres;
  }


  public MediaDetailed getRandomMedia() {
    Random random = new Random();
    String randomType = random.nextBoolean() ? "movie" : "tv"; 

    String latestMediaUrl = UriComponentsBuilder.fromHttpUrl(String.format("https://api.themoviedb.org/3/%s/latest", randomType))
      .queryParam("api_key", tmdbApiKey)
      .toUriString();

    int latestMediaId;

    MediaDetailed latestMedia;
    try {
      latestMedia = restTemplate.getForObject(latestMediaUrl, MediaDetailed.class);
    } catch (Exception e) {
      throw new RuntimeException("Failed to fetch latest media: ", e);
    }

    if (latestMedia == null) {
      throw new ResourceNotFoundException("No media found with the specified ID");
    }

    latestMediaId = latestMedia.getId();

    MediaDetailed randomMedia = null;

    while (randomMedia == null) {
      int randomId = random.nextInt(latestMediaId) + 1;
      String url = UriComponentsBuilder.fromHttpUrl(String.format("https://api.themoviedb.org/3/%s/%d", randomType, randomId))
        .queryParam("api_key", tmdbApiKey)
        .toUriString();

      try {
        randomMedia = restTemplate.getForObject(url, MediaDetailed.class);
      } catch (HttpClientErrorException e) {
        if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
          System.out.println(
            String.format("Failed to get media for type %s and ID %d", randomType, randomId)
          );
          continue;
        } else {
          throw e;
        }
      }
    }

    return randomMedia;
  }

}