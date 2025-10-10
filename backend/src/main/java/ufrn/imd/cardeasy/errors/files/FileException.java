package ufrn.imd.cardeasy.errors.files;

public abstract class FileException extends RuntimeException {
  public FileException(String message) {
    super(message);
  };
};
