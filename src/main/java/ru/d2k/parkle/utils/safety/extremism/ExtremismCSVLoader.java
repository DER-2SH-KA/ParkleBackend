package ru.d2k.parkle.utils.safety.extremism;

import lombok.Getter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import java.util.regex.*;

@Getter
public class ExtremismCSVLoader extends ExtremismMaterialLoader {
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
        final List<String> links = new ArrayList<>();

        try (
                InputStream in = Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream(filePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        ) {
            String lineRaw;

            while ((lineRaw = reader.readLine()) != null) {
                String[] line = lineRaw.trim().split(this.divider);

                Matcher matcher = urlHttpPattern.matcher(line[1].trim());

                while (matcher.find()) {
                    String extremismLinkRaw = matcher.group().trim();
                    String extremismLink = extremismLinkRaw;

                    if (
                        extremismLinkRaw.endsWith("/") ||
                        extremismLink.endsWith(",") ||
                        extremismLink.endsWith("!") ||
                        extremismLink.endsWith(".") ||
                        extremismLink.endsWith("?")
                    ) {
                        StringBuffer sb = new StringBuffer(extremismLinkRaw);
                        sb.deleteCharAt(sb.length() - 1);

                        extremismLink = sb.toString();
                    }

                    links.add(extremismLink);
                }
            }
        }
        catch (IOException ioex) {
            System.err.println("Can't load CSV file with path '" + filePath + "':" + ioex.getMessage());
        }
        catch (Exception ex) {
            System.err.println("Some Exception in CSV Loader: " + ex);
        }

        return links;
    }

}
