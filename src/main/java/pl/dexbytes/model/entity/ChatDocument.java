package pl.dexbytes.model.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ChatDocument extends PanacheEntity {
    
    @Column(name = "chat_message_id", insertable = false, updatable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long chatMessageId;
    
    @Column(name = "document_name", nullable = false)
    private String documentName;
    
    @Column(name = "document_type", nullable = false)
    private String documentType;
    
    @Lob
    @Column(name = "document_data", nullable = false)
    private byte[] documentData;
    
    @Column(name = "document_size", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long documentSize;
    
    @CreationTimestamp
    @Column(name = "upload_timestamp")
    private LocalDateTime uploadTimestamp;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id", nullable = false)
    private ChatMessage chatMessage;
    
    // Constructor without chatMessage reference (for simplicity in data transfer)
    public ChatDocument(Long id, Long chatMessageId, String documentName, String documentType, 
                        byte[] documentData, Long documentSize, LocalDateTime uploadTimestamp) {
        this.id = id;
        this.chatMessageId = chatMessageId;
        this.documentName = documentName;
        this.documentType = documentType;
        this.documentData = documentData;
        this.documentSize = documentSize;
        this.uploadTimestamp = uploadTimestamp;
    }
}