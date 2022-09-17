package edu.ktu.screenshotanalyser.exceptions;

import java.io.IOException;

public class InvalidFileContentException extends IOException {
    public InvalidFileContentException(String message) {
        super(message);
    }
}
