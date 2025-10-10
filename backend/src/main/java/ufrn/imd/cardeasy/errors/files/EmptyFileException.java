package ufrn.imd.cardeasy.errors.files;

public class EmptyFileException extends FileException {
  public EmptyFileException() {
    super("Arquivo está vazio!");
  };
};
