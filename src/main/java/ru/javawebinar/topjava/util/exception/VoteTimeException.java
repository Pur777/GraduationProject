package ru.javawebinar.topjava.util.exception;

import org.springframework.http.HttpStatus;

public class VoteTimeException extends ApplicationException {
    public static final String VOTE_TIME_EXCEPTION = "exception.vote.time";

    public VoteTimeException(String args) {
        super(ErrorType.TIME_ERROR, VOTE_TIME_EXCEPTION, HttpStatus.CONFLICT, args);
    }
}
