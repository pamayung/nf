package com.wildhan.nf.api;

import com.wildhan.nf.core.Response;
import com.wildhan.nf.core.db.DB;
import com.wildhan.nf.core.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequestMapping("app")
@RestController
public class TodoList {

    @GetMapping("/todos")
    public ResponseEntity<String> getList(){
        try {
            JSONArray jsonArray = new JSONArray();

            String sql = "select a.id, a.name, a.is_finished, a.is_active, c.name, c.is_active" +
                    " from todos a" +
                    " left join todos_tags b on a.id = b.todo_id" +
                    " left join tags c on b.tags_id = c.id" +
                    " where a.is_active = 1";

            DB db = DB.getInstance();
            ArrayList<Object[]> rows = db.getRows(sql);

            String sTemp = "";
            JSONArray jaTags = null;
            JSONObject joData = null;
            for (int i = 0; i < rows.size(); i++){

                if (!sTemp.contains(rows.get(i)[0]+"")){
                    joData = new JSONObject();

                    joData.put("id", rows.get(i)[0]);
                    joData.put("name", rows.get(i)[1]);
                    joData.put("is_finished", rows.get(i)[2]);
                    joData.put("is_active", rows.get(i)[3]);

                    sTemp += rows.get(i)[0];
                    jaTags = new JSONArray();
                }

                if (!TextUtils.isEmpty(rows.get(i)[4]+"")){
                    JSONObject jo = new JSONObject();
                    jo.put("name", rows.get(i)[4]);
                    jo.put("is_active", rows.get(i)[5]);

                    jaTags.put(jo);
                }


                if (i != rows.size() - 1 && !sTemp.contains(rows.get(i+1)[0]+"") || i == rows.size() - 1){
                    joData.put("tags", jaTags);

                    jsonArray.put(joData);
                }
            }

            return new Response().success(jsonArray);

        }
        catch (Exception e){
            e.printStackTrace();

            return new Response().internalError();
        }
    }

    @GetMapping("/tags")
    public ResponseEntity<String> getTagsActive(){
        try {
            JSONArray jsonArray = new JSONArray();

            String sql = "select id, name, is_active, create_at, update_at from tags where is_active = 1";

            DB db = DB.getInstance();
            ArrayList<Object[]> rows = db.getRows(sql);

            for (Object[] data : rows){
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("id", data[0]);
                jsonObject.put("name", data[1]);
                jsonObject.put("is_active", data[2]);
                jsonObject.put("create_at", data[3]);
                jsonObject.put("update_at", data[4]);

                jsonArray.put(jsonObject);
            }

            return new Response().success(jsonArray);
        }
        catch (Exception e){
            e.printStackTrace();

            return new Response().internalError();
        }
    }
}
