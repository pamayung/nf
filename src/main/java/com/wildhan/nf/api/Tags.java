package com.wildhan.nf.api;

import com.wildhan.nf.core.Response;
import com.wildhan.nf.core.db.DB;
import com.wildhan.nf.core.util.TextUtils;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("app/tag")
@RestController
public class Tags {

    @PostMapping
    public ResponseEntity<String> insertTags(@RequestBody String body){
        try {
            JSONObject jsonObject = new JSONObject(body);

            boolean bActive = false;

            if (jsonObject.isNull("name")){
                return new Response().badRequest();
            }

            String sName = jsonObject.getString("name");

            if (TextUtils.isEmpty(sName)){
                return new Response().badRequest();
            }

            if (jsonObject.isNull("is_active")){
                bActive = true;
            }

            String sql = "insert into tags ( name ) values ( "+ "'" + sName + "'" +" )";

            if (!bActive){
                int isActive = jsonObject.getInt("is_active");

                sql = "insert into tags ( name, is_active ) values ( "+ "'" + sName + "'," + " " + isActive + ")";
            }

            DB db = DB.getInstance();
            int result = db.insertRow(sql);

            if (result > 0){
                return new Response().success();
            }
            else {
                return new Response().failed();
            }

        }
        catch (Exception e){
            e.printStackTrace();

            return new Response().internalError();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTags(@RequestBody String body, @PathVariable int id){
        try {
            JSONObject jsonObject = new JSONObject(body);

            boolean bName = false, bActive = false;
            if (jsonObject.isNull("name")){
                bName = true;
            }
            if (jsonObject.isNull("is_active")){
                bActive = true;
            }

            String sql = "";

            if (!bName && !bActive){
                String sName = jsonObject.getString("name");
                int isActive = jsonObject.getInt("is_active");

                sql = "update tags set " +
                        "name = '" + sName +"'," +
                        "is_active = " + isActive +"," +
                        "update_at = now()" +
                        " where " +
                        "id = " + id;
            }
            else if (!bName){
                String sName = jsonObject.getString("name");

                sql = "update tags set " +
                        "name = '" + sName +"'," +
                        "update_at = now()" +
                        " where " +
                        "id = " + id;
            }
            else if (!bActive){
                int isActive = jsonObject.getInt("is_active");

                sql = "update tags set " +
                        "is_active = " + isActive +"," +
                        "update_at = now()" +
                        " where " +
                        "id = " + id;
            }
            else {
                return new Response().badRequest();
            }

            DB db = DB.getInstance();
            int result = db.insertRow(sql);

            if (result > 0){
                return new Response().success();
            }
            else {
                return new Response().failed();
            }

        }
        catch (Exception e){
            e.printStackTrace();

            return new Response().internalError();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getTags(@PathVariable int id){
        try {
            JSONObject jsonObject = new JSONObject();

            String sql = "select id, name, is_active, create_at, update_at from tags where id = " + id;

            DB db = DB.getInstance();
            Object[] row = db.getRow(sql);

            if (row.length > 0){
                jsonObject.put("id", row[0]);
                jsonObject.put("name", row[1]);
                jsonObject.put("is_active", row[2]);
                jsonObject.put("create_at", row[3]);
                jsonObject.put("update_at", row[4]);
            }

            return new Response().success(jsonObject);

        }
        catch (Exception e){
            e.printStackTrace();

            return new Response().internalError();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTags(@PathVariable int id){
        try {
            String sql = "update tags set " +
                    "is_active = 1," +
                    "update_at = now()" +
                    " where " +
                    "id = " + id;

            DB db = DB.getInstance();
            int result = db.insertRow(sql);

            if (result > 0){
                return new Response().success();
            }
            else {
                return new Response().failed();
            }

        }
        catch (Exception e){
            e.printStackTrace();

            return new Response().internalError();
        }
    }
}
