import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Repo {

    private String full_name;

    private String pushed_at;

    public String getFull_name() {
        return full_name;
    }

    public String getPushed_at() {
        return pushed_at;
    }
}


