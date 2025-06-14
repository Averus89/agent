package pl.dexbytes.model.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends PanacheEntity {
    
    @Column(name = "message_text", nullable = false)
    private String messageText;
    
    @Column(name = "is_user_message", nullable = false)
    private boolean userMessage;
    
    @CreationTimestamp
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @OneToMany(mappedBy = "chatMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatDocument> documents = new ArrayList<>();
    
    // Constructor without documents list
    public ChatMessage(Long id, String messageText, boolean userMessage, LocalDateTime timestamp) {
        this.id = id;
        this.messageText = messageText;
        this.userMessage = userMessage;
        this.timestamp = timestamp;
    }
}