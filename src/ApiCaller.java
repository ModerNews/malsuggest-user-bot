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
            System.out.println(caller.update_my_anime_list(49854, "completed", false, 9, 12));
            Thread.sleep(60000);
            caller.delete_my_anime_list(49854);
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
        String fields = "num_episodes";
        Request request = new Request.Builder()
                .url(new UrlBuilder()
                        .setUri("anime/ranking")
                        .addParameter("limit", Integer.toString(limit))
                        .addParameter("offset", Integer.toString(offset))
                        .addParameter("fields", fields)
                        .build())
                .method("GET", null)
                .addHeader("Authorization", headers.get("Authorization"))
                .build();
        Response response = client.newCall(request).execute();
        return new ObjectMapper().readValue(response.body().string(), HashMap.class);
    }

    public HashMap<String, Object> update_my_anime_list(int id,
                                                        String status,
                                                        boolean is_rewatching,
                                                        int score,
                                                        int num_watched_episodes) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, String.format("status=%s&is_rewatching=%s&score=%s&num_watched_episodes=%s",
                                                                        status, is_rewatching, score, num_watched_episodes));
        Request request = new Request.Builder()
                .url(new UrlBuilder()
                        .setUri("anime/" + Integer.toString(id) + "/my_list_status")
                        .build())
                .method("PUT", body)
                .addHeader("Authorization", headers.get("Authorization"))
                .build();
        Response response = client.newCall(request).execute();
        return new ObjectMapper().readValue(response.body().string(), HashMap.class);
    }

    public void delete_my_anime_list(int id) throws IOException {
        Request request = new Request.Builder()
                .url(new UrlBuilder()
                        .setUri("anime/" + Integer.toString(id) + "/my_list_status")
                        .build())
                .method("DELETE", null)
                .addHeader("Authorization", headers.get("Authorization"))
                .build();
        client.newCall(request).execute();
    }
}
