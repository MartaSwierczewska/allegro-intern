import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
//    private static String readAll(Reader rd) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        int cp;
//        while ((cp = rd.read()) != -1) {
//            sb.append((char) cp);
//        }
//        return sb.toString();
//    }
//
//    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
//        InputStream is = new URL(url).openStream();
//        try {
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
//            String jsonText = readAll(rd);
//            JSONObject json = new JSONObject(jsonText);
//            return json;
//        } finally {
//            is.close();
//        }
//    }

    public static void main(String[] args) throws IOException, JSONException {

//        Open( "http://date.jsontest.com/", JSON );
        ObjectMapper objectMapper = new ObjectMapper();
        LocalDateTime now = LocalDateTime.now();
        String result = "";
        long minTime = Long.MAX_VALUE;

        Repo[] repo;
        repo = objectMapper.readValue(new File("response.json"), Repo[].class);

        for (Repo value : repo) {
            String secondDate = value.pushed_at;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            LocalDateTime modificationDate = LocalDateTime.parse(secondDate, formatter);

            Duration duration = Duration.between(now, modificationDate);

            long diff = Math.abs(duration.getSeconds());
            if (diff < minTime) {
                result = value.full_name;
                minTime = diff;
            }
        }
        System.out.println("Last modified repo is: " + result);
//        System.out.println(minTime);
    }
}
