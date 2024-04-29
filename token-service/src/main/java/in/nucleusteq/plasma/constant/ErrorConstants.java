package in.nucleusteq.plasma.constant;

public class ErrorConstants {
    /**
     * User not found error message constant.
     */
    public static final String USER_NOT_FOUND_MESSAGE = "User account with email id %s is not in valid status or does not exist."
            + " Please contact Plasma support for more information.";

    /**
     * UNAUTHORIZED_EXCEPTION.
     */
    public static final String UNAUTHORIZED_EXCEPTION = "Unauthorized access";
    /**
     * Invalid Token User.
     */
    public static final String INVALID_TOKEN_USER = "Invalid token of user with email id %s ";

    /**
     * Token is Invalid.
     */
    public static final String INVALID_TOKEN = "The token is invalid";

    /**
     * Token is Invalid.
     */
    public static final String EXPIRED_TOKEN = "The token is expired";

    /**
     * Token must not be null.
     */
    public static final String TOKEN_MUST_NOT_BE_NULL_OR_START_WITH_BEARER = "Token must not be null/must start with bearer.";

    /**
     * The Malformed exception message.
     */
    public static final String MALFORMEND_EXCEPTION_MESSAGE = "JWT strings must contain exactly 2 period characters";

    /**
     * The Unsupported exception message.
     */
    public static final String UNSUPPORTED_EXCEPTION_MESSAGE = "Unsigned Claims JWTs are not supported";

    /**
     * The token not belong to the user constant.
     */
    public static final String PROVIDED_TOKEN_INVALID = "Provided token does not belong to the email id %s";

    public static final String RECORD_NOT_FOUND = "Record not found";

}
