package ufrn.imd.cardeasy.storages;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

import ufrn.imd.cardeasy.errors.files.UnableSaveFileException;
import ufrn.imd.cardeasy.errors.files.EmptyFileException;
import ufrn.imd.cardeasy.errors.files.FileException;
import ufrn.imd.cardeasy.errors.files.UnavailableFileException;
import ufrn.imd.cardeasy.errors.files.UnavailableFolderException;

public abstract class FileStorage {
  protected String mime;

  public FileStorage(String mime) {
    this.mime = mime;
  };

  protected Path getStorageFolder() {
    try {
      Path path = Paths.get("").toAbsolutePath();
      String last = path.getFileName().toString();

      if(
        last.equals("backend") || 
        last.equals("src")
      ) {
        path = path.getParent();
      };
  
      return path
        .resolve("database")
        .resolve("files");
    } catch (Exception e) {
      throw new UnavailableFolderException();
    }
  };

  protected String makeFilename(
    String filenameWithoutMime
  ) {
    return filenameWithoutMime + "." + this.mime;
  };

  protected void delete(
    String filenameWithoutMime,
    Path path
  ) {
    try {
      Path filePath = path.resolve(this.makeFilename(filenameWithoutMime));
      Files.deleteIfExists(filePath);
    } catch (Exception e) {
      throw new UnavailableFileException();
    };
  };

  protected void store(
    String filenameWithoutMime, 
    MultipartFile file, 
    Path destiny
  ) {
    try {
      this.validate(file);
      String filename = this.makeFilename(filenameWithoutMime);
      file.transferTo(destiny.resolve(filename));
    } catch(FileException e) {
      throw e;
    } catch (Exception e) {
      throw new UnableSaveFileException();
    };
  };

  public void validate(
    MultipartFile file
  ) {
    if(file.isEmpty())
      throw new EmptyFileException();
  };
  
  public boolean exists(
    String filenameWithoutMime,
    Path destiny
  ) {
    String filename = this.makeFilename(filenameWithoutMime);
    return Files.exists(destiny.resolve(filename));
  };
};
