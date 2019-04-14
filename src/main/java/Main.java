import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://api.github.com/users/allegro/repos?page=1&per_page=100");
        String content = getAPIContent(url);
        List<Repo> repos = JSONtoObjectList(content);

        LocalDateTime now = LocalDateTime.now();
        long minTime = Long.MAX_VALUE;
        String result = "";

        for (Repo repo : repos) {
            long diff = getDiff(repo, now);
            if (diff<minTime) {
                result = repo.full_name;
                minTime = diff;
            }
        }
        System.out.println("Last modified repo is: " + result);
    }

    private static String getAPIContent(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content.toString();
    }

    private static List<Repo> JSONtoObjectList(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue( content, objectMapper.getTypeFactory().constructCollectionType(List.class, Repo.class));
    }

    private static LocalDateTime getRepoModDate(Repo repo){
        String secondDate = repo.pushed_at;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return LocalDateTime.parse(secondDate, formatter);
    }

    private static long getDiff(Repo repo, LocalDateTime now){
        LocalDateTime modificationDate = getRepoModDate(repo);
        Duration duration = Duration.between(now, modificationDate);
        return Math.abs(duration.getSeconds());
    }
}
