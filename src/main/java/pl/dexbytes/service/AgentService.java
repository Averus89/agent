package pl.dexbytes.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import pl.dexbytes.model.entity.ChatMessage;
import pl.dexbytes.repository.ChatDocumentRepository;
import pl.dexbytes.repository.ChatMessageRepository;

import java.time.LocalDateTime;

@ApplicationScoped
@RequiredArgsConstructor
public class AgentService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatDocumentRepository chatDocumentRepository;

    @Transactional
    public void saveUserMessage(String userMessage) {
        chatMessageRepository.save(ChatMessage.builder()
                .messageText(userMessage)
                .userMessage(true)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @Transactional
    public void saveAssistantMessage(String userMessage) {
        chatMessageRepository.save(ChatMessage.builder()
                .messageText(userMessage)
                .userMessage(false)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
