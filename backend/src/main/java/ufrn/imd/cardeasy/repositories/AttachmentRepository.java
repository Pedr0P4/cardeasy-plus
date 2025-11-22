package ufrn.imd.cardeasy.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ufrn.imd.cardeasy.models.Attachment;
import ufrn.imd.cardeasy.models.Card;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
  Page<Attachment> findAllByCard(Card card, String query,  Pageable pageable);
}
