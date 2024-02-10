import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Main {
    public static void main(String[] args) throws Exception {
        translateFile("en_US.lang", "ru_RU.lang");
    }

    public static void translateFile(String inputFile, String outputFile) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        int totalLines = countLines(inputFile);
        int translatedLines = 0;
        String line;

        System.out.println("Translating...");

        while ((line = reader.readLine()) != null) {
            String translatedLine = translate("en", "ru", line);
            writer.write(translatedLine);
            writer.newLine();
            translatedLines++;
            int progress = (int) ((double) translatedLines / totalLines * 100);
            System.out.print("\rProgress: " + progress + "%");
        }
        reader.close();
        writer.close();

        System.out.println("\nTranslation completed.");
    }
    public static String translate(String langFrom, String langTo, String text) throws Exception {
        String urlStr = "https://translate.googleapis.com/translate_a/single?client=gtx&sl="
                + langFrom + "&tl=" + langTo + "&dt=t&q=" + URLEncoder.encode(text, "UTF-8");

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String[] arr = response.toString().split("\"");
        return arr[1];
    }
    public static int countLines(String filename) throws IOException {
        LineNumberReader reader = new LineNumberReader(new FileReader(filename));
        int lines = 0;
        while (reader.readLine() != null) {
            lines++;
        }
        reader.close();
        return lines;
    }
}
