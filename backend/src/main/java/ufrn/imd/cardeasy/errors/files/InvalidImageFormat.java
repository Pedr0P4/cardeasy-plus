package ufrn.imd.cardeasy.errors.files;

public class InvalidImageFormat extends FileException {
  public InvalidImageFormat() {
    super("este formato de imagem não está disponível");
  };
};
