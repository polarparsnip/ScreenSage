package is.hi.screensage_web_server.config;

/**
 * A utility class for defining custom exceptions used across the application, 
 * enabling more descriptive error handling and control over different error cases.
 */
public class CustomExceptions {

  public static class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
      super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
      super(message);
    }
  }

  public static class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
      super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
      super(message);
    }
  }

  public static class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
      super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
