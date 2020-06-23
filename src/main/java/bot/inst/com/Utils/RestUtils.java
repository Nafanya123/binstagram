package bot.inst.com.Utils;

import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import kong.unirest.Cookie;
import kong.unirest.Cookies;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Random;

public class RestUtils {

    public static HttpResponse<String> getImportRequest() throws UnsupportedEncodingException, InterruptedException {

        HttpResponse<String> getAuthCookies = Unirest
                .post("https://www.instagram.com/accounts/login/ajax/")
                .field("username", "activinstlt")
                .field("password", "")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("x-csrftoken", "aih03gzuZW9iM3z7sSvrs7OHgqkC1PlK")
                .header("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .asString();

//        HttpResponse<String> test = Unirest
//                .post("https://www.instagram.com/")
//                .asString();

        Cookies cookies = getAuthCookies.getCookies();
        Cookie target = cookies.getNamed("target");
        Cookie csrftoken = cookies.getNamed("csrftoken");
        Cookie ig_did = cookies.getNamed("ig_did");
//        Cookie shbid = cookies.getNamed("shbid");
//        Cookie shbts = cookies.getNamed("shbts");
        Cookie rur = cookies.getNamed("rur");
        Cookie mid = cookies.getNamed("mid");
        Cookie ds_user_id = cookies.getNamed("ds_user_id");
        Cookie sessionid = cookies.getNamed("sessionid");

        HttpResponse<String> getListPhotoByTag = Unirest
                .get("https://www.instagram.com/explore/tags/%D1%84%D0%B0%D1%80%D1%82%D1%83%D0%BA%D0%B8%D0%BD%D0%B0%D0%B7%D0%B0%D0%BA%D0%B0%D0%B7/?__a=1")
                .cookie(target)
                .cookie(csrftoken)
                .cookie(ig_did)
//                .cookie(shbid)
//                .cookie(shbts)
                .cookie(rur)
                .cookie(mid)
                .cookie(ds_user_id)
                .cookie(sessionid)
                .asString();
        String q = getListPhotoByTag.getBody();
        String endCursor = JsonPath.read(q, "$.graphql.hashtag.edge_hashtag_to_media.page_info.end_cursor");
        List<String> listShortcode = JsonPath.read(q, "$.graphql.hashtag.edge_hashtag_to_media.edges[*].node.id");

        HttpResponse<String> getShortcode = null;
//        for(int i = 0; i < 3; i++)
//        {
//            getShortcode = Unirest
//                    .get("https://www.instagram.com/graphql/query/?query_hash=bd33792e9f52a56ae8fa0985521d141d&variables=" + URLEncoder.encode("{\"tag_name\":\"тлт\",\"first\":7,\"after\":\""+endCursor+"\"}", "UTF-8"))
//                    .cookie(target)
//                    .cookie(csrftoken)
//                    .cookie(ig_did)
////                    .cookie(shbid)
////                    .cookie(shbts)
//                    .cookie(rur)
//                    .cookie(mid)
//                    .cookie(ds_user_id)
//                    .cookie(sessionid)
//                    .asString();
//            endCursor = JsonPath.read(getShortcode.getBody(), "$.data.hashtag.edge_hashtag_to_media.page_info.end_cursor");
//            listShortcode.addAll(JsonPath.read(getShortcode.getBody(), "$.data.hashtag.edge_hashtag_to_media.edges[*].node.id"));
//        }
        System.out.println(listShortcode.size());
        HttpResponse<String> like = null;
        Random rnd = new Random();
        int z = 0;
        for(int i = 0; i < listShortcode.size(); i++)
        {
            if(i == 35)
            {
                System.out.println("END*****************");
                return null;
            }

            z++;
            like = Unirest
                    .post("https://www.instagram.com/web/likes/"+listShortcode.get(i)+"/like/")
                    .cookie(target)
                    .cookie(csrftoken)
                    .cookie(ig_did)
//                    .cookie(shbid)
//                    .cookie(shbts)
                    .cookie(rur)
                    .cookie(mid)
                    .cookie(ds_user_id)
                    .cookie(sessionid)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("x-csrftoken", csrftoken.getValue())
                    .header("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                    .asString();
            if(like.getStatus() != 200)
            {
                System.out.println("error");
            }
            try {
                long timer = rnd.nextInt(6552) + 10265;
                System.out.println("timer - " + timer);
                System.out.println("текущая - " + i);
                Thread.sleep(timer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(z > 10)
            {
                System.out.println("z обнулить");
                Thread.sleep(rnd.nextInt(56500) + 26065);
                Unirest
                        .get("https://www.instagram.com/")
                        .cookie(target)
                        .cookie(csrftoken)
                        .cookie(ig_did)
                        .cookie(rur)
                        .cookie(mid)
                        .cookie(ds_user_id)
                        .cookie(sessionid)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .header("x-csrftoken", csrftoken.getValue())
                        .header("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                        .asString();
                z = 0;
            }
        }

        return null;
    }

}
