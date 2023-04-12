package src;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;

public class ApiCaller {

    private HashMap<String, String> headers = new HashMap<>();
    private String baseurl = "https://api.myanimelist.net/";
    private String version = "v2";
    private OkHttpClient client = new OkHttpClient().newBuilder().build();

    public static void main(String[] args){
        try {
            ApiCaller caller = new ApiCaller(args[0]);
            System.out.println(caller.get_anime_ranking(16, 70));
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public ApiCaller(String access_token) throws IOException {
        headers.put("Authorization", "Bearer " + access_token);
//        headers.put("Content-Type", "application/json");
    }

    public HashMap<String, Object> get_anime_ranking() throws IOException {
        return get_anime_ranking(500, 0);
    }

    public HashMap<String, Object> get_anime_ranking(int limit) throws IOException {
        return get_anime_ranking(limit, 0);
    }

    public HashMap<String, Object> get_anime_ranking(int limit, int offset) throws IOException {
        Request request = new Request.Builder()
                .url(new UrlBuilder()
                        .setUri("anime/ranking")
                        .addParameter("limit", Integer.toString(limit))
                        .addParameter("offset", Integer.toString(offset))
                        .build())
                .method("GET", null)
                .addHeader("Authorization", headers.get("Authorization"))
                .build();
        Response response = client.newCall(request).execute();
        return new ObjectMapper().readValue(response.body().string(), HashMap.class);
    }
}
