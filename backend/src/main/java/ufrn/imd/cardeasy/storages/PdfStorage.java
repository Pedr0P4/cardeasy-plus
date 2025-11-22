package ufrn.imd.cardeasy.storages;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;

@Component
public class PdfStorage extends DocumentStorage {
  private Path getUserPdfsPath() {
    return this.getStorageFolder()
      .resolve("pdfs");
  };

  public void store(Long id, MultipartFile file) {
    this.store(id.toString(), file, this.getUserPdfsPath());
  };

  public void delete(Long id) {
    this.delete(id.toString(), this.getUserPdfsPath());
  };

  public boolean exists(Long id) {
    return this.exists(id.toString(), this.getUserPdfsPath());
  };
}
