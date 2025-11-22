package ufrn.imd.cardeasy.storages;

import org.springframework.web.multipart.MultipartFile;
import ufrn.imd.cardeasy.errors.files.InvalidPDFFormat;


public abstract class DocumentStorage extends FileStorage{
  DocumentStorage() {
    super("pdf");
  }

  public void validate(MultipartFile file) {
    super.validate(file);
    String type = file.getContentType();
    if (type == null) {
      throw new InvalidPDFFormat();
    }
    boolean valid = type.contains("pdf");
    if(!valid) {
      throw new InvalidPDFFormat();
    }
  }
}
