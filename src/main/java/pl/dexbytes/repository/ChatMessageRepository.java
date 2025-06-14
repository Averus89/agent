
package pl.dexbytes.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import pl.dexbytes.model.entity.ChatMessage;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ChatMessageRepository implements PanacheRepository<ChatMessage> {
    
    @PersistenceContext
    EntityManager entityManager;
    
    @Transactional
    public ChatMessage save(ChatMessage message) {
        if (message.id == null) {
            entityManager.persist(message);
            return message;
        } else {
            return entityManager.merge(message);
        }
    }
    
    @Transactional
    public void delete(Long id) {
        ChatMessage message = entityManager.find(ChatMessage.class, id);
        if (message != null) {
            entityManager.remove(message);
        }
    }
    
    public List<ChatMessage> findRecentMessages(int limit) {
        return entityManager.createQuery("SELECT m FROM ChatMessage m ORDER BY m.timestamp DESC", ChatMessage.class)
                .setMaxResults(limit)
                .getResultList();
    }
}