package pl.dexbytes;

import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.SessionScoped;

@SessionScoped
@RegisterAiService
public interface ChatBotService {

    Multi<String> chat(String userMessage);
}
