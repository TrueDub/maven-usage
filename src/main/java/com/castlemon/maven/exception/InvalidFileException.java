package com.castlemon.maven.exception;

public class InvalidFileException extends Exception {

  private static final long serialVersionUID = 1L;

  public InvalidFileException(String dirName) {
    super("the file at " + dirName + " is not a directory");
  }

}
