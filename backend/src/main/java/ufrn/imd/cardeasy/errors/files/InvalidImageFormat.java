package ufrn.imd.cardeasy.errors.files;

public class InvalidImageFormat extends FileException {
  public InvalidImageFormat() {
    super("Este formato de imagem não está disponível!");
  };
};
