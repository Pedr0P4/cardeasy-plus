package ufrn.imd.cardeasy.storages;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import ufrn.imd.cardeasy.errors.files.FileException;
import ufrn.imd.cardeasy.errors.files.InvalidImageFormat;
import ufrn.imd.cardeasy.errors.files.UnableSaveFileException;

public abstract class ImageStorage extends FileStorage {
  public ImageStorage() {
    super("webp");
    ImageIO.scanForPlugins();
  };

  @Override
  protected void store(
    String filenameWithoutMime, 
    MultipartFile file,
    Path destiny
  ) {
    try {
      this.validate(file);
      
      try (InputStream inputStream = file.getInputStream()) {
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        try(
          FileOutputStream outputStream = new FileOutputStream(
            destiny.resolve(filenameWithoutMime + "." + this.mime).toFile()
          );
        ) {
          ImageIO.write(bufferedImage, "webp", outputStream);
        };
      };
    } catch(FileException e) {
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      throw new UnableSaveFileException();
    };
  };

  @Override
  public void validate(
    MultipartFile file
  ) {
    super.validate(file);

    if(file.getContentType() == null) throw new InvalidImageFormat();
    
    String type = file.getContentType();
    
    Boolean isValid = 
      type.contains("png") ||
      type.contains("jpeg") ||
      type.contains("jpg") ||
      type.contains("webp");

    if(!isValid) throw new InvalidImageFormat();
  };
};
