package destinations;

import config.LogConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileLogDestination implements LogDestinationImplementor {

    @Override
    public void write(String formattedLog) {
        LogConfig config = LogConfig.getInstance();
        Path filePath = Paths.get(config.getFilePath());

        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.write(
                filePath,
                (formattedLog + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException("Falha ao escrever log em ficheiro", e);
        }
    }
}
