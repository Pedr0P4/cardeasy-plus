package ufrn.imd.cardeasy.storages;

import java.nio.file.Path;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class AvatarStorage extends ImageStorage {
  private Path getUsersAvatarsPath() {
    return this.getStorageFolder()
      .resolve("avatars");
  };

  public void store(UUID id, MultipartFile file) {
    this.store(id.toString(), file, this.getUsersAvatarsPath());
  };

  public void delete(UUID id) {
    this.delete(id.toString(), this.getUsersAvatarsPath());
  };

  public boolean exists(UUID id) {
    return this.exists(id.toString(), this.getUsersAvatarsPath());
  };
};
