package is.hi.screensage_web_server.data;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import is.hi.screensage_web_server.entities.Review;
import is.hi.screensage_web_server.entities.Challenge;
import is.hi.screensage_web_server.entities.ChallengeOption;
import is.hi.screensage_web_server.entities.CompletedChallenge;
import is.hi.screensage_web_server.entities.Like;
import is.hi.screensage_web_server.entities.MediaList;
import is.hi.screensage_web_server.entities.MediaListItem;
import is.hi.screensage_web_server.entities.Quote;
import is.hi.screensage_web_server.entities.Users;
import is.hi.screensage_web_server.repositories.ReviewRepository;
import is.hi.screensage_web_server.models.ChallengeType;
import is.hi.screensage_web_server.repositories.ChallengeRepository;
import is.hi.screensage_web_server.repositories.CompletedChallengeRepository;
import is.hi.screensage_web_server.repositories.LikeRepository;
import is.hi.screensage_web_server.repositories.MediaListRepository;
import is.hi.screensage_web_server.repositories.QuoteRepository;
import is.hi.screensage_web_server.repositories.UserRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DataLoader {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ReviewRepository reviewRepository;
  
  @Autowired
  private ChallengeRepository challengeRepository;

  @Autowired
  private CompletedChallengeRepository completedChallengeRepository;

  @Autowired
  private MediaListRepository mediaListRepository;

  @Autowired
  private QuoteRepository quoteRepository;

  @Autowired
  private LikeRepository likeRepository;

  @Value("${user.default_profile_img}")
  private String defaultProfileImg;

  @Value("${media.test_img_one}")
  private String testImgOne;

  @Value("${media.test_img_two}")
  private String testImgTwo;

  @Value("${media.test_img_three}")
  private String testImgThree;

  private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


  @PostConstruct
  public void loadDummyData() {
    Users testuser1 = new Users("testuser1", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser2 = new Users("testuser2", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser3 = new Users("testuser3", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser4 = new Users("testuser4", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser5 = new Users("testuser5", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser6 = new Users("testuser6", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser7 = new Users("testuser7", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser8 = new Users("testuser8", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser9 = new Users("testuser9", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser10 = new Users("testuser10", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser11 = new Users("testuser11", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser12 = new Users("testuser12", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser13 = new Users("testuser13", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser14 = new Users("testuser14", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser15 = new Users("testuser15", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser16 = new Users("testuser16", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser17 = new Users("testuser17", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser18 = new Users("testuser18", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser19 = new Users("testuser19", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser20 = new Users("testuser20", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser21 = new Users("testuser21", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser22 = new Users("testuser22", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser23 = new Users("testuser23", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser24 = new Users("testuser24", encoder.encode("123"), 3, defaultProfileImg, "0");
    Users testuser25 = new Users("testuser25", encoder.encode("123"), 3, defaultProfileImg, "0");

    userRepository.save(testuser1);
    userRepository.save(testuser2);
    userRepository.save(testuser3);
    userRepository.save(testuser4);
    userRepository.save(testuser5);
    userRepository.save(testuser6);
    userRepository.save(testuser7);
    userRepository.save(testuser8);
    userRepository.save(testuser9);
    userRepository.save(testuser10);
    userRepository.save(testuser11);
    userRepository.save(testuser12);
    userRepository.save(testuser13);
    userRepository.save(testuser14);
    userRepository.save(testuser15);
    userRepository.save(testuser16);
    userRepository.save(testuser17);
    userRepository.save(testuser18);
    userRepository.save(testuser19);
    userRepository.save(testuser20);
    userRepository.save(testuser21);
    userRepository.save(testuser22);
    userRepository.save(testuser23);
    userRepository.save(testuser24);
    userRepository.save(testuser25);
 

    reviewRepository.save(new Review(1184918, testuser1, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser2, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser3, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser4, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser5, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser6, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser7, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser8, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser9, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser10, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser11, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser12, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser13, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser14, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser15, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser16, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser17, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser18, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser19, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser20, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser21, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser22, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser23, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser24, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184918, testuser25, 4, "Very good movie", "movie"));

    reviewRepository.save(new Review(1184917, testuser1, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184916, testuser1, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184915, testuser1, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184914, testuser1, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184913, testuser1, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184912, testuser1, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184911, testuser1, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184910, testuser1, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184909, testuser1, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184908, testuser1, 4, "Very good movie", "movie"));
    reviewRepository.save(new Review(1184907, testuser1, 4, "Very good movie", "movie"));

    MediaList ml1 = new MediaList(testuser1, "My favorite action movies", "short description");
    ml1.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml1, 1184918, "Robot", "Summary", testImgOne, "movies"),
        new MediaListItem(ml1, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(ml1, 1184916, "Peanut", "Summary", testImgThree, "movies")
      )
    );
    mediaListRepository.save(ml1);

    MediaList ml2 = new MediaList(testuser1, "My favorite horror movies", "short description");
    ml2.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml2, 1184918, "Robot", "Summary", testImgOne, "movies"),
        new MediaListItem(ml2, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(ml2, 1184916, "Peanut", "Summary", testImgThree, "movies")
      )
    );
    mediaListRepository.save(ml2);

    MediaList ml3 = new MediaList(testuser1, "My favorite comedy movies", "short description");
    ml3.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml3, 1184918, "Robot", "Summary", testImgOne, "movies"),
        new MediaListItem(ml3, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(ml3, 1184916, "Peanut", "Summary", testImgThree, "movies")
      )
    );
    mediaListRepository.save(ml3);

    MediaList ml4 = new MediaList(testuser1, "My favorite romance movies", "short description");
    ml4.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml4, 1184918, "Robot", "Summary", testImgOne, "movies"),
        new MediaListItem(ml4, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(ml4, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(ml4, 1184916, "Peanut", "Summary", testImgThree, "movies")
      )
    );
    mediaListRepository.save(ml4);

    MediaList ml5 = new MediaList(testuser1, "My favorite war movies", "short description");
    ml5.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml5, 1184918, "Robot", "Summary", testImgOne, "movies"),
        new MediaListItem(ml5, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(ml5, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(ml5, 1184916, "Peanut", "Summary", testImgThree, "movies")
      )
    );
    mediaListRepository.save(ml5);

    MediaList ml6 = new MediaList(testuser1, "My favorite historical movies", "short description");
    ml6.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml6, 1184918, "Robot", "Summary", testImgOne, "movies"),
        new MediaListItem(ml6, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(ml6, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(ml6, 1184916, "Peanut", "Summary", testImgThree, "movies")
      )
    );
    mediaListRepository.save(ml6);

    MediaList ml7 = new MediaList(testuser1, "My favorite adventure movies", "short description");
    ml7.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml7, 1184918, "Robot", "Summary", testImgOne, "movies"),
        new MediaListItem(ml7, 1184916, "Peanut", "Summary", testImgThree, "movies")
      )
    );
    mediaListRepository.save(ml7);

    MediaList ml8 = new MediaList(testuser1, "My favorite documentary movies", "short description");
    ml8.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml8, 1184918, "Robot", "Summary", testImgOne, "movies"),
        new MediaListItem(ml8, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(ml8, 1184916, "Peanut", "Summary", testImgThree, "movies")
      )
    );
    mediaListRepository.save(ml8);

    MediaList ml9 = new MediaList(testuser1, "My favorite mystery movies", "short description");
    ml9.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml9, 1184918, "Robot", "Summary", testImgOne, "movies"),
        new MediaListItem(ml9, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(ml9, 1184916, "Peanut", "Summary", testImgThree, "movies")
      )
    );
    mediaListRepository.save(ml9);

    MediaList ml10 = new MediaList(testuser1, "My favorite zombie movies", "short description");
    ml10.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml10, 1184918, "Robot", "Summary", testImgOne, "movies"),
        new MediaListItem(ml10, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(ml10, 1184916, "Peanut", "Summary", testImgThree, "movies")
      )
    );
    mediaListRepository.save(ml10);

    MediaList ml11 = new MediaList(testuser1, "My favorite thriller shows", "short description");
    ml11.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml11, 1184918, "Robot", "Summary", testImgOne, "shows"),
        new MediaListItem(ml11, 1184917, "Smoothie", "Summary", testImgTwo, "shows"),
        new MediaListItem(ml11, 1184916, "Peanut", "Summary", testImgThree, "shows")
      )
    );
    mediaListRepository.save(ml11);

    MediaList ml12 = new MediaList(testuser1, "My favorite adventure shows", "short description");
    ml12.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml12, 1184918, "Robot", "Summary", testImgOne, "shows"),
        new MediaListItem(ml12, 1184917, "Smoothie", "Summary", testImgTwo, "shows"),
        new MediaListItem(ml12, 1184916, "Peanut", "Summary", testImgThree, "shows")
      )
    );
    mediaListRepository.save(ml12);

    MediaList ml13 = new MediaList(testuser2, "My favorite comedy shows", "short description");
    ml13.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml13, 1184918, "Robot", "Summary", testImgOne, "shows"),
        new MediaListItem(ml13, 1184917, "Smoothie", "Summary", testImgTwo, "shows"),
        new MediaListItem(ml13, 1184916, "Peanut", "Summary", testImgThree, "shows")
      )
    );
    mediaListRepository.save(ml13);

    MediaList ml14 = new MediaList(testuser3, "My favorite fantasy shows", "short description");
    ml14.setMediaListItems(
      Arrays.asList(
        new MediaListItem(ml14, 1184918, "Robot", "Summary", testImgOne, "shows"),
        new MediaListItem(ml14, 1184917, "Smoothie", "Summary", testImgTwo, "shows"),
        new MediaListItem(ml14, 1184916, "Peanut", "Summary", testImgThree, "shows")
      )
    );
    mediaListRepository.save(ml14);

    MediaList wl1 = new MediaList(testuser1);
    wl1.setTitle("Action movies to watch");
    wl1.setWatchlist(true);
    wl1.setSharedWith(List.of(2, 3, 4));
    wl1.setMediaListItems(
      Arrays.asList(
        new MediaListItem(wl1, 1184918, "Robot", "Summary", testImgOne, "movies"),
        new MediaListItem(wl1, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(wl1, 1184916, "Peanut", "Summary", testImgThree, "movies")
      )
    );
    mediaListRepository.save(wl1);

    MediaList wl2 = new MediaList(testuser1);
    wl2.setTitle("Horror movies to watch");
    wl2.setWatchlist(true);
    wl2.setSharedWith(List.of(2, 3, 4));
    wl2.setMediaListItems(
      Arrays.asList(
        new MediaListItem(wl2, 1184918, "Robot", "Summary", testImgOne, "movies"),
        new MediaListItem(wl2, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(wl2, 1184916, "Peanut", "Summary", testImgThree,"movies")
      )
    );
    mediaListRepository.save(wl2);

    MediaList wl3 = new MediaList(testuser2);
    wl3.setTitle("Animated movies to watch");
    wl3.setWatchlist(true);
    wl3.setSharedWith(List.of(3));
    wl3.setMediaListItems(
      Arrays.asList(
        new MediaListItem(wl3, 1184918, "Robot", "Summary", testImgOne, "movies"),
        new MediaListItem(wl3, 1184917, "Smoothie", "Summary", testImgTwo, "movies"),
        new MediaListItem(wl3, 1184916, "Peanut", "Summary", testImgThree,"movies")
      )
    );
    mediaListRepository.save(wl3);

    Challenge challenge1 = new Challenge(
      "When did Inception come out?", 
      ChallengeType.TRIVIA, 
      testImgOne, 
      500
    );
    challenge1.setOptions(
      Arrays.asList(
        new ChallengeOption(challenge1, "2010", true),
        new ChallengeOption(challenge1, "2009"),
        new ChallengeOption(challenge1, "2007"),
        new ChallengeOption(challenge1, "2011")
    ));

    Challenge challenge2 = new Challenge(
      "When did The Dark Knight come out?", 
      ChallengeType.TRIVIA, 
      testImgTwo, 
      500
    );
    challenge2.setOptions(
      Arrays.asList(
        new ChallengeOption(challenge2, "2012"),
        new ChallengeOption(challenge2, "2009", true),
        new ChallengeOption(challenge2, "2005"),
        new ChallengeOption(challenge2, "2010")
    ));

    Challenge challenge3 = new Challenge(
      "When did Interstellar come out?", 
      ChallengeType.TRIVIA, 
      testImgThree, 
      500
    );
    challenge3.setOptions(
      Arrays.asList(
        new ChallengeOption(challenge3, "2012"),
        new ChallengeOption(challenge3, "2018"),
        new ChallengeOption(challenge3, "2020"),
        new ChallengeOption(challenge3, "2014", true)
    ));

    challengeRepository.save(challenge1);
    challengeRepository.save(challenge2);
    challengeRepository.save(challenge3);
    
    completedChallengeRepository.save(new CompletedChallenge(testuser1, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser1, challenge2, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser2, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser3, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser4, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser5, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser6, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser7, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser8, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser9, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser10, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser11, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser12, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser13, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser14, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser15, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser16, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser17, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser18, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser19, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser20, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser21, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser22, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser23, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser24, challenge1, 500));
    completedChallengeRepository.save(new CompletedChallenge(testuser25, challenge1, 500));


    likeRepository.save(new Like(testuser1, ml13));
    likeRepository.save(new Like(testuser6, ml13));
    likeRepository.save(new Like(testuser1, "movies", 1184918));
    likeRepository.save(new Like(testuser2, "movies", 1184918));
    likeRepository.save(new Like(testuser3, "movies", 1184918));


    quoteRepository.save(new Quote("test quote 1", "test quote 1", 1991, "movies", 12341));
    quoteRepository.save(new Quote("test quote 2", "test quote 1", 1992, "movies", 12342));
    quoteRepository.save(new Quote("test quote 3", "test quote 1", 1993, "movies", 12343));
    quoteRepository.save(new Quote("test quote 4", "test quote 1", 1994, "movies", 12344));
    quoteRepository.save(new Quote("test quote 5", "test quote 1", 1995, "movies", 12345));
  }
}
