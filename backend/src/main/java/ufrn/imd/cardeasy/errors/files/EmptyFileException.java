package ufrn.imd.cardeasy.errors.files;

public class EmptyFileException extends FileException {
  public EmptyFileException() {
    super("arquivo está vazio");
  };
};
