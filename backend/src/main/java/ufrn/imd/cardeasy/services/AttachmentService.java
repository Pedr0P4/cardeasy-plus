package ufrn.imd.cardeasy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ufrn.imd.cardeasy.errors.AttachmentNotFound;
import ufrn.imd.cardeasy.errors.CardNotFound;
import ufrn.imd.cardeasy.errors.files.UnableSaveFileException;
import ufrn.imd.cardeasy.models.Attachment;
import ufrn.imd.cardeasy.models.Card;
import ufrn.imd.cardeasy.repositories.AttachmentsRepository;
import ufrn.imd.cardeasy.repositories.CardsRepository;
import ufrn.imd.cardeasy.storages.AttachmentStorage;

@Service
public class AttachmentService {
  private AttachmentStorage pdfs;
  private AttachmentsRepository attachments;
  private CardsRepository cards;

  @Autowired
  public AttachmentService(
    AttachmentStorage pdfs,
    AttachmentsRepository attachments,
    CardsRepository cards
  ) {
    this.pdfs = pdfs;
    this.attachments = attachments;
    this.cards = cards;
  };

  @Transactional
  public Attachment create(Integer cardId, MultipartFile file) {
    Attachment attachment = new Attachment();
    
    Card card = this.cards.findById(cardId)
      .orElseThrow(CardNotFound::new);
    
    attachment.setCard(card);

    if (file.getOriginalFilename() == null)
      throw new UnableSaveFileException();

    attachment.setFilename(
      file.getOriginalFilename()
        .replace(".pdf", "")
    );

    attachment.setSize(file.getSize());

    this.attachments.save(attachment);

    this.pdfs.store(attachment.getId(), file);

    return attachment;
  };

  public boolean existsById(Integer id) {
    return this.attachments.existsById(id);
  };

  public Attachment findById(Integer id) {
    return this.attachments.findById(id)
      .orElseThrow(AttachmentNotFound::new);
  };

  public Resource findContentById(Integer id) {
    return this.pdfs.getFile(id);
  };

  public Page<Attachment> searchAllByCard(
    Integer cardId, 
    String query,
    Pageable pageable
  ) {
    return this.attachments.searchAllByCard(
      cardId,
      query, 
      pageable
    );
  };

  @Transactional
  public Attachment update(Integer id, MultipartFile file) {
    Attachment attachment = this.findById(id);

    if (file.getOriginalFilename() == null)
      throw new UnableSaveFileException();

    attachment.setFilename(
      file.getOriginalFilename()
        .replace(".pdf", "")
    );

    this.pdfs.store(id, file);

    return attachment;
  };

  @Transactional
  public void deleteById(Integer id) {
    this.attachments.deleteById(id);
    this.pdfs.delete(id);
  };
};
