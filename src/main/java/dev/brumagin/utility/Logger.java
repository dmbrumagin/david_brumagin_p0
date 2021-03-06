package dev.brumagin.utility;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

public class Logger {

    public static void log(String message, LogLevel level){

        String logMessage = level.name() +" " +  message + " " + new Date() + "\n";
        String path = "C:\\Users\\dbrum\\IdeaProjects\\Project0\\src\\main\\java\\dev\\brumagin\\banking.log";

        try {
            Files.write(Paths.get(path),
                    logMessage.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
