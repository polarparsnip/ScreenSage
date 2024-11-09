package is.hi.screensage_web_server.data;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import is.hi.screensage_web_server.entities.Review;
import is.hi.screensage_web_server.entities.Challenge;
import is.hi.screensage_web_server.entities.ChallengeOption;
import is.hi.screensage_web_server.entities.CompletedChallenge;
import is.hi.screensage_web_server.entities.Users;
import is.hi.screensage_web_server.repositories.ReviewRepository;
import is.hi.screensage_web_server.models.ChallengeType;
import is.hi.screensage_web_server.repositories.ChallengeRepository;
import is.hi.screensage_web_server.repositories.CompletedChallengeRepository;
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


    Challenge challenge1 = new Challenge("When did Inception come out?", ChallengeType.TRIVIA, testImgOne, 500);
    challenge1.setOptions(
      Arrays.asList(
        new ChallengeOption(challenge1, "2010", true),
        new ChallengeOption(challenge1, "2009"),
        new ChallengeOption(challenge1, "2007"),
        new ChallengeOption(challenge1, "2011")
    ));

    Challenge challenge2 = new Challenge("When did The Dark Knight come out?", ChallengeType.TRIVIA, testImgTwo, 500);
    challenge2.setOptions(
      Arrays.asList(
        new ChallengeOption(challenge2, "2012"),
        new ChallengeOption(challenge2, "2009", true),
        new ChallengeOption(challenge2, "2005"),
        new ChallengeOption(challenge2, "2010")
    ));

    Challenge challenge3 = new Challenge("When did Interstellar come out?", ChallengeType.TRIVIA, testImgThree, 500);
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
  }
}
