import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.List;

public class Main {

    private static final String ALLEGRO_REPOS_GITHUB_URL = "https://api.github.com/users/allegro/repos?page=1&per_page=100";

    public static void main(String[] args) throws IOException {
        String content = getContent();
        List<Repo> repositories = convertContentToRepoList(content);

        Repo lastModifiedRepo = findLastModifiedRepo(repositories);
        System.out.println("Last modified repo is: " + lastModifiedRepo.getFull_name());
    }

    private static Repo findLastModifiedRepo(List<Repo> repos) {
        return repos.stream()
                .max(Comparator.comparing(Repo::getPushed_at))
                .orElseThrow(NullPointerException::new);
    }

    private static String getContent() throws IOException {
        URL url = new URL(ALLEGRO_REPOS_GITHUB_URL);
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

    private static List<Repo> convertContentToRepoList(String content) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, Repo.class));
    }
}