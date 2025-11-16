package ru.d2k.parkle.utils.safety.extremism;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import java.util.regex.*;

public final class ExtremismCSVLoader extends ExtremismMaterialLoader{
    private String divider = ";";

    private static Pattern urlHttpPattern =
            Pattern.compile("http(s)?://\\S+", Pattern.CASE_INSENSITIVE);

    @Override
    Set<String> getExtremismLinks(String filePath, Charset charset) {
        return new HashSet<>(
                this.loadLinksFromFile(filePath, charset)
        );
    }

    public String getDivider() { return this.divider; }

    public void setDivider(String divider) { this.divider = divider; }

    private List<String> loadLinksFromFile(String filePath, Charset charset) {
        final List<String> lines = new ArrayList<>();

        try (
                InputStream in = getClass().getResourceAsStream(filePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        ) {
            String line;

            while ((line = reader.readLine()) != null) {
                Matcher matcher = urlHttpPattern.matcher(line);

                while (matcher.find()) {
                    lines.add(matcher.group());
                }
            }
        }
        catch (IOException ioex) {
            System.out.println("Can't load CSV file with path '" + filePath + "':" + ioex.getMessage());
        }

        return lines;
    }

}
