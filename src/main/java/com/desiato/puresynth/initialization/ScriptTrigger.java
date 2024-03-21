package com.desiato.puresynth.initialization;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class ScriptTrigger {

    @EventListener(ApplicationReadyEvent.class)
    public void runScript() {
        try {
            String scriptPath = "./init_users_mysql.sh";
            ProcessBuilder processBuilder = new ProcessBuilder(scriptPath);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
