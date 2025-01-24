package com.jair.forohub.infra.exceptions;

public class AccionNoAutorizadaException extends RuntimeException {
    public AccionNoAutorizadaException(String message) {
        super(message);
    }
}
