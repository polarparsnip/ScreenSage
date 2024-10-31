package is.hi.screensage_web_server.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import is.hi.screensage_web_server.models.MediaDetailed;
import is.hi.screensage_web_server.models.MediaPageResponse;


import org.springframework.beans.factory.annotation.Value;

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
      url = UriComponentsBuilder.fromHttpUrl(String.format("https://api.themoviedb.org/3/%s/popular", type))
        .queryParam("page", page)
        .queryParam("api_key", tmdbApiKey)
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

}