package dev.be.serviceuser.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    UNVERIFIED_EMAIL(HttpStatus.UNAUTHORIZED, "Unverified email"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not founded"),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "Profile not founded"),
    INVALID_EMAIL(HttpStatus.UNAUTHORIZED, "Invalid email"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid password"),
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "Duplicated user name"),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "Duplicated email"),
    UNABLE_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to send email"),
    FOLLOW_ALREADY_EXISTS(HttpStatus.CONFLICT, "user is already following the target"),
    FOLLOW_NOT_FOUND(HttpStatus.CONFLICT, "user never followed the target"),
    INVALID_OPERATION(HttpStatus.UNAUTHORIZED, "A user cannot follow themselves"),
    ALREADY_LIKED_POST(HttpStatus.CONFLICT, "user already liked the post"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "User has invalid permission"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurs"),
    NOTIFICATION_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Connect to notification occurs error"),
    NO_SUCH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "No such algorithm"),
    ;

    private final HttpStatus status;
    private final String message;
}
