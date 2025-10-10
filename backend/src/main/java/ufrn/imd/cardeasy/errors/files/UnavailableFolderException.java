package ufrn.imd.cardeasy.errors.files;

public class UnavailableFolderException extends FileException {
  public UnavailableFolderException() {
    super("Pasta está inacessível e talvez não exista!");
  };
};
