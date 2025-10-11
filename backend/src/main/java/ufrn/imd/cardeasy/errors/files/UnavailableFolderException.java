package ufrn.imd.cardeasy.errors.files;

public class UnavailableFolderException extends FileException {
  public UnavailableFolderException() {
    super("pasta está inacessível e talvez não exista");
  };
};
