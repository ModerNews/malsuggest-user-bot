package src;

import java.util.HashMap;
import java.util.Map;

public class UrlBuilder {
    private String baseurl = "https://api.myanimelist.net/";
    private String version = "v2";
    private String uri;
    private HashMap<String, String> parameters = new HashMap<>();

    public UrlBuilder setUri(String uri){
        this.uri = uri;
        return this;
    }

    public UrlBuilder addParameter(String key, String value){
        parameters.put(key, value);
        return this;
    }

    public String build(){
        StringBuffer queryBuffer = new StringBuffer();
        for(Map.Entry<String, String> param: parameters.entrySet()){
            queryBuffer.append(param.getKey() + "=" + param.getValue() + "&");
        }
        String query;
        if(!(queryBuffer.toString().equals(""))){
            query = "?" + queryBuffer.toString();
        }else {
            query = "";
        }
        return baseurl + version + "/" + uri + query;
    }
}
