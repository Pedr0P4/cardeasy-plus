package ufrn.imd.cardeasy.storages;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import ufrn.imd.cardeasy.errors.files.FileNotFoundException;
import ufrn.imd.cardeasy.errors.files.InvalidPDFFormat;


public abstract class PdfStorage extends FileStorage {
  public PdfStorage() {
    super("pdf");
  };

  protected Path getPdfsStoragePath() {
    return this.getStorageFolder()
      .resolve("pdfs");
  };

  @Override
  public void validate(MultipartFile file) {
    super.validate(file);

    String type = file.getContentType();

    if (type == null) throw new InvalidPDFFormat();

    Boolean valid = type.contains("pdf");

    if(!valid) throw new InvalidPDFFormat();
  };

  public Resource getFile(Integer id) {
    try {
      String filename = this.makeFilename(id.toString());
    
      Resource resource = new UrlResource(
        this.getPdfsStoragePath().resolve(filename).toUri()
      );
        
      return resource;
    } catch (Exception e) {
      throw new FileNotFoundException();
    }
  };
};
