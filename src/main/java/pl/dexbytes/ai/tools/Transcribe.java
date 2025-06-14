package pl.dexbytes.ai.tools;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
@Slf4j
public class Transcribe {
    private final OpenAiTranscribeModel okHttp = new OpenAiTranscribeModel();
    private final OkHttpClient downloadClient;

    public Transcribe() {
        this.downloadClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        ;
    }

    @Tool("transcribe an audio file from URL")
    public String transcribeAudioFromUrl(String url) {
        try {
            // Download and save the file locally
            File audioFile = downloadFile(url, "audio.mp3");

            // Transcribe the downloaded file
            String result = okHttp.transcribeAudio(audioFile);

            // Clean up the temporary file
            cleanupFile(audioFile);

            return result;
        } catch (IOException e) {
            log.error("Error downloading or processing audio file from URL: {}", url, e);
            return "Error: Failed to download or process audio file - " + e.getMessage();
        }

    }

    private File downloadFile(String fileUrl, String fileName) throws IOException {
        Request request = new Request.Builder()
                .url(fileUrl)
                .get()
                .build();

        // Create temporary file
        Path tempDir = Files.createTempDirectory("audio_downloads");
        File outputFile = new File(tempDir.toFile(), fileName);

        try (Response response = downloadClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to download file: HTTP " + response.code() + " " + response.message());
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new IOException("Response body is null");
            }

            // Write the response body to file
            try (InputStream inputStream = body.byteStream();
                 FileOutputStream outputStream = new FileOutputStream(outputFile);
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {

                byte[] buffer = new byte[8192];
                int bytesRead;
                long totalBytesRead = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;
                }

                log.info("Downloaded {} bytes from {} to {}", totalBytesRead, fileUrl, outputFile.getAbsolutePath());
            }
        }

        return outputFile;
    }

    private void cleanupFile(File file) {
        try {
            if (file.exists()) {
                boolean deleted = file.delete();
                if (deleted) {
                    log.debug("Cleaned up temporary file: {}", file.getAbsolutePath());
                }

                // Also clean up the parent directory if it's empty
                File parentDir = file.getParentFile();
                if (parentDir != null && parentDir.isDirectory() && parentDir.list() != null && parentDir.list().length == 0) {
                    parentDir.delete();
                }
            }
        } catch (Exception e) {
            log.warn("Failed to clean up temporary file: {}", file.getAbsolutePath(), e);
        }
    }


}