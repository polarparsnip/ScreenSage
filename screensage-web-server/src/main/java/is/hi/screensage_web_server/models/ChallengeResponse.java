package is.hi.screensage_web_server.models;

import is.hi.screensage_web_server.entities.ChallengeOption;

public class ChallengeResponse {
  private ChallengeOption correctOption;
  private boolean answeredCorrectly;

  public ChallengeResponse(ChallengeOption correctOption, boolean answeredCorrectly) {
    this.correctOption = correctOption;
    this.answeredCorrectly = answeredCorrectly;
  }

  public ChallengeOption getCorrectOption() {
    return correctOption;
  }

  public void setCorrectOption(ChallengeOption correctOption) {
    this.correctOption = correctOption;
  }

  public boolean getAnsweredCorrectly() {
    return answeredCorrectly;
  }

  public void setAnsweredCorrectly(boolean answeredCorrectly) {
    this.answeredCorrectly = answeredCorrectly;
  }
}