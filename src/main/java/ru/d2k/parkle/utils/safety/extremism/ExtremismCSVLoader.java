package ru.d2k.parkle.utils.safety.extremism;

import lombok.Getter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import java.util.regex.*;

@Getter
public final class ExtremismCSVLoader extends ExtremismMaterialLoader {
    private final String filePath;
    private final Charset charset;
    private final String divider;

    private static final Pattern urlHttpPattern =
            Pattern.compile("http(s)?://\\S+", Pattern.CASE_INSENSITIVE);

    public ExtremismCSVLoader(String filePath, Charset charset, String divider) {
        this.filePath = filePath;
        this.charset = charset;
        this.divider = divider;
    }

    @Override
    public Set<String> getExtremismLinks() {
        return new HashSet<>(
                this.loadLinksFromFile(this.filePath, this.charset)
        );
    }

    private List<String> loadLinksFromFile(String filePath, Charset charset) {
        final List<String> lines = new ArrayList<>();

        try (
                InputStream in = getClass().getResourceAsStream(filePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        ) {
            String lineRaw;

            while ((lineRaw = reader.readLine()) != null) {
                String[] line = lineRaw.trim().split(this.divider);
                Matcher matcher = urlHttpPattern.matcher(line[1]);

                while (matcher.find()) {
                    lines.add(matcher.group());
                }
            }
        }
        catch (IOException ioex) {
            System.err.println("Can't load CSV file with path '" + filePath + "':" + ioex.getMessage());
        }
        catch (Exception ex) {
            System.err.println("Some Exception in CSV Loader: " + ex);
        }

        return lines;
    }

}
