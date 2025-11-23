package ufrn.imd.cardeasy.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ufrn.imd.cardeasy.models.Attachment;

@Repository
public interface AttachmentsRepository extends JpaRepository<Attachment, Integer> {
  @Query(
    // language=sql
    value = """
      SELECT at.* FROM attachment AS at
      WHERE at.card_id = ?1 
      AND at.filename LIKE CONCAT('%', ?2, '%')
      ORDER BY at.filename ASC
    """,
    // language=sql
    countQuery = """
      SELECT COUNT(at.id) FROM attachment AS at
      WHERE at.card_id = ?1
      AND at.filename LIKE CONCAT('%', ?2, '%')
    """,
    nativeQuery = true
  ) public Page<Attachment> searchAllByCard(
    Integer cardId, 
    String query, 
    Pageable pageable
  );
};
