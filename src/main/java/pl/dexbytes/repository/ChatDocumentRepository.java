
package pl.dexbytes.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import pl.dexbytes.model.entity.ChatDocument;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ChatDocumentRepository implements PanacheRepository<ChatDocument> {
    
    @PersistenceContext
    EntityManager entityManager;
    
    public List<ChatDocument> findByChatMessageId(Long messageId) {
        return entityManager.createQuery(
                "SELECT d FROM ChatDocument d WHERE d.chatMessage.id = :messageId ORDER BY d.uploadTimestamp DESC", 
                ChatDocument.class)
                .setParameter("messageId", messageId)
                .getResultList();
    }
    
    @Transactional
    public ChatDocument save(ChatDocument document) {
        if (document.id == null) {
            entityManager.persist(document);
            return document;
        } else {
            return entityManager.merge(document);
        }
    }
    
    @Transactional
    public void delete(Long id) {
        ChatDocument document = entityManager.find(ChatDocument.class, id);
        if (document != null) {
            entityManager.remove(document);
        }
    }
    
    @Transactional
    public void deleteByChatMessageId(Long messageId) {
        entityManager.createQuery("DELETE FROM ChatDocument d WHERE d.chatMessage.id = :messageId")
                .setParameter("messageId", messageId)
                .executeUpdate();
    }
}