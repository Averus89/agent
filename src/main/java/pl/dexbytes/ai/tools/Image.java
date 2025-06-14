package pl.dexbytes.ai.tools;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@ApplicationScoped
@Slf4j
public class Image {
    private final ChatModel chatModel;

    public Image() {
        chatModel = OpenAiChatModel.builder()
                .modelName(OpenAiChatModelName.GPT_4_1)
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .build();
    }

    @Tool("Describe image")
    public String describeImage(String url) {
        log.info("Describing image from url: {}", url);
        // Create a user message with the audio content
        UserMessage userMessage = UserMessage.from(
                TextContent.from("Opisz krótko co przedstawia obraz"),
                ImageContent.from(URI.create(url))
        );

        // Send the request to the OpenAI model
        return chatModel.chat(userMessage)
                .aiMessage()
                .text();
    }
}
