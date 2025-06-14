package pl.dexbytes.ai.tools;

import okhttp3.*;
import java.io.File;
import java.io.IOException;

public class OpenAiTranscribeModel {
    
    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String API_URL = "https://api.openai.com/v1/audio/transcriptions";
    
    public String transcribeAudio(File file) {
        OkHttpClient client = new OkHttpClient();
        
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "audio.mp3", 
                    RequestBody.create(file, MediaType.parse("audio/mpeg")))
                .addFormDataPart("model", "gpt-4o-transcribe")
                .build();
        
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + OPENAI_API_KEY)
                .post(requestBody)
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return "Error: " + response.code() + " " + response.message();
            }
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}