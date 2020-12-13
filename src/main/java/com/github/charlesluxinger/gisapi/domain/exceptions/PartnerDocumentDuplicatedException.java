package com.github.charlesluxinger.gisapi.domain.exceptions;

/**
 * Class comments go here...
 *
 * @author Charles Luxinger
 * @version 13/12/20
 */
public class PartnerDocumentDuplicatedException extends IllegalStateException {

    public static final String EXISTS_ERROR_MESSAGE = "Partner with document '#%s' already exists.";

    public PartnerDocumentDuplicatedException(final String document) {
        super(String.format(EXISTS_ERROR_MESSAGE, document));
    }
}
