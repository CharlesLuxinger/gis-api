package com.github.charlesluxinger.gisapi.domain.exceptions;

/**
 * @author Charles Luxinger
 * @version 13/12/20
 */
public class InvalidObjectIdException extends IllegalArgumentException {

    public static final String INVALID_OBJECT_ID_DETAIL = "Invalid ObjectId #%s";

    public InvalidObjectIdException(final String id) {
        super(String.format(INVALID_OBJECT_ID_DETAIL, id));
    }

}
