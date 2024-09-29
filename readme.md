# ScreenSage - Television and movie platform

**ScreenSage** is an all-in-one platform for TV and movie enthusiasts. It offers a variety of features to enhance your entertainment experience, including:

- **Personalized Watchlists**: Seamlessly track your favorite TV shows and movies, without limits.
- **Rating System**: Rate and review TV shows and movies in one place. See how your tastes align with others in the community.
- **Daily Challenges**: Participate in fun and engaging daily movie and TV show-related challenges to test your knowledge and earn points.
- **Review Commenting**: Comment on reviews left by other users to share your thoughts or ask follow-up questions.
- **Gamification Elements**: Earn points, climb leaderboards, and engage with others through friendly competition.
- **Cross-Media Integration**: ScreenSage focuses equally on both movies and TV shows, filling a gap left by other platforms that neglect or de-prioritize one in favor of the other.

## Use Cases

- **Log In and Personalized Experience**: Once logged in, users can customize their watchlists and track their viewing habits.
- **Participate in Daily Challenges**: Users can log in daily to attempt new quizzes and puzzles, unlocking points and achievements while competing on leaderboards.
- **Review and Rate Content**: Users can leave thoughtful reviews on any TV show or movie they've watched and see how their opinion compares to the community's ratings.
- **Join Discussions**: Dive into community discussions in the comment section, comment on reviews, and even reply to questions others might have on your thoughts.
- **Track Your Progress**: View your stats, including how well you've performed in challenges, your leaderboard ranking, and other personal achievements.

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/polarparsnip/ScreenSage.git

2. Navigate to the project directory:
   ```bash
   cd ScreenSage

3. Create an .env file in screensage-web-server containing the following variables
   ```bash
   DB_URL=jdbc:postgresql://<your_postgres_path>
   DB_USER=<your_postgres_username>
   DB_PASSWORD=<your_postgres_password>
   JWT_EXPIRATION=<expiration_time_of_jwt_tokens>
   JWT_AUDIENCE=<variable_name_for_your_user_audience>
   JWT_ISSUER=<Variable_name_representing_you>
   ```
4. Run web server
   In the terminal navigate to **screensage-web-server**
   ```bash
   cd ScreenSage
   ```
   and run 
   ```bash
   mvn spring-boot:run
   ```
   or, if using VS Code, open **screensage-web-server** and then press the *Run Java* button while in the **ScreensageWebServerApplication** file.