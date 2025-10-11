package ufrn.imd.cardeasy.errors.files;

public class UnableSaveFileException extends FileException {
  public UnableSaveFileException() {
    super("não foi possível salvar o arquivo");
  };
};
