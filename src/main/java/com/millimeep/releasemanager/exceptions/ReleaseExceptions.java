package com.millimeep.releasemanager.exceptions;

import com.millimeep.releasemanager.release.DuplicateItem;
import com.millimeep.releasemanager.release.ItemNotFound;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ReleaseExceptions {

    @ResponseBody
    @ExceptionHandler({
            ItemNotFound.class})
    @ResponseStatus(NOT_FOUND)
    String releaseComponentNotFoundHandler(ItemNotFound exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(DuplicateItem.class)
    @ResponseStatus(CONFLICT)
    String duplicateReleaseComponentHandler(DuplicateItem exception) {
        return exception.getMessage();
    }
}
