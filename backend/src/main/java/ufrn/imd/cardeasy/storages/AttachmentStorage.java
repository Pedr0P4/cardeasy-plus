package ufrn.imd.cardeasy.storages;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Component
public class AttachmentStorage extends PdfStorage {
  @Override
  protected Path getPdfsStoragePath() {
    return this.getStorageFolder()
      .resolve("attachments");
  };

  public void store(Integer id, MultipartFile file) {
    this.store(id.toString(), file, this.getPdfsStoragePath());
  };

  public void delete(Integer id) {
    this.delete(id.toString(), this.getPdfsStoragePath());
  };

  public boolean exists(Integer id) {
    return this.exists(id.toString(), this.getPdfsStoragePath());
  };
};
