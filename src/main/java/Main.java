import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Date now = new java.util.Date();
        String result = "";
        long minTime = Long.MAX_VALUE;
        try {
            Repo[] repo = objectMapper.readValue(new File("response.json"), Repo[].class);

            for (int i = 0; i < repo.length; i++) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                    Date secondDate = sdf.parse(repo[i].getPushed_at());
                    long diff = Math.abs(secondDate.getTime() - now.getTime());

                    if (diff < minTime) {
                        result=repo[i].full_name;
                        minTime = diff;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(result);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
