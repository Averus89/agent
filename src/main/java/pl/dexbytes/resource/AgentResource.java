package pl.dexbytes.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.dexbytes.ai.agent.CentralaAgent;
import pl.dexbytes.model.Answer;
import pl.dexbytes.model.Instruction;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Path("/agent")
@RequiredArgsConstructor(onConstructor_ = @Inject)
@Slf4j
public class AgentResource {
    private final CentralaAgent agent;

    @POST
    @Path("/chat")
    public Answer chat(Instruction instruction, @QueryParam("messageId") UUID messageId) {
        messageId = messageId != null ? messageId : generateUUID("serce");
        log.info("Chat with messageId: {} and instructions: {}", messageId, instruction);
        if (instruction == null) {
            return Answer.builder().answer("Nie podano instrukcji!").build();
        }
        String answer = agent.chat(messageId, instruction.question().replaceAll("ignoruj.*", ""));
        return Answer.builder().answer(answer).build();
    }

    public static UUID generateUUID(String text) {
        // Use the DNS namespace UUID as defined in RFC 4122
        UUID namespace = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");

        // Convert the text to bytes using UTF-8 encoding
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);

        // Generate a UUID (version 5) based on the namespace and text bytes
        return UUID.nameUUIDFromBytes(textBytes);
    }

}
