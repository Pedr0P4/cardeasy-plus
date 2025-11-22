package ufrn.imd.cardeasy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ufrn.imd.cardeasy.errors.AttachmentNotFound;
import ufrn.imd.cardeasy.errors.CardNotFound;
import ufrn.imd.cardeasy.models.Attachment;
import ufrn.imd.cardeasy.repositories.AttachmentRepository;
import ufrn.imd.cardeasy.repositories.CardsRepository;
import ufrn.imd.cardeasy.storages.PdfStorage;
import java.util.List;

@Service
public class AttachmentService {
  private AttachmentRepository attachments;
  private CardsRepository cards;
  private PdfStorage pdfs;

  @Autowired
  public AttachmentService(AttachmentRepository attachments,
                           CardsRepository cards,
                           PdfStorage pdfs) {
    this.attachments = attachments;
    this.cards = cards;
    this.pdfs = pdfs;
  }

  public Attachment create(Integer card, MultipartFile file) {
    Attachment attachment = new Attachment();
    attachment.setCard(cards.findById(card).orElseThrow(CardNotFound::new));
    attachment.setSize(file.getSize());
    this.attachments.save(attachment);
    this.pdfs.store(attachment.getId(), file);
    return attachment;
  }

  public boolean existsById(Long id) {
    return attachments.existsById(id);
  }

  public Attachment findById(Long id) {
    return attachments.findById(id).orElseThrow(CardNotFound::new);
  }


  public Page<Attachment> searchAllByCard(Integer card, String query, Pageable pageable) {
    return attachments.findAllByCard(cards.findById(card).orElseThrow(CardNotFound::new),query, pageable);
  }

  public Attachment update(Long id, Integer card, MultipartFile file) {
    Attachment attachment = this.findById(id);
    attachment.setCard(cards.findById(card).orElseThrow(CardNotFound::new));
    pdfs.store(id,file);
    return attachment;
  }

  public void deleteById(Long id) {
    pdfs.delete(id);
    attachments.deleteById(id);
  }
}
