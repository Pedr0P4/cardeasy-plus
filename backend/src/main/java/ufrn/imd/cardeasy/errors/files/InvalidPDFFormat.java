package ufrn.imd.cardeasy.errors.files;

public class InvalidPDFFormat extends FileException {
  public InvalidPDFFormat() {
    super("Isto não é um PDF.");
  }
}
