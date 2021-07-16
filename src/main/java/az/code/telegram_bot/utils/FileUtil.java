package az.code.telegram_bot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUtil {
    @Value("${offer.image.path}")
    String path;
    @Value("${offer.image.extension}")
    String extension;

    public InputFile byteArrToInputFile(byte[] array) {
        InputStream inputStream = new ByteArrayInputStream(array);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(inputStream, "image");
        return inputFile;
    }

    public InputFile pathToInputFile(String path) throws IOException {
        return byteArrToInputFile(Files.readAllBytes(Paths.get(path)));
    }

    public String savePhoto(byte[] array) throws IOException {
        String path = this.path + UUID.randomUUID() + this.extension;
        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(array);
        }
        return path;
    }

    public void deleteWithPath(String path) throws IOException {
        Files.deleteIfExists(Paths.get(path));
    }
}
