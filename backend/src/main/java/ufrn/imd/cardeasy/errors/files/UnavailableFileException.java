package ufrn.imd.cardeasy.errors.files;

public class UnavailableFileException extends FileException {
  public UnavailableFileException() {
    super("arquivo está inacessível e talvez não exista");
  };
};
