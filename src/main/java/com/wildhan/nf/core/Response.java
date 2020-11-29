package com.wildhan.nf.core;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class Response {

    public ResponseEntity<String> success(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status_code", 200);
        jsonObject.put("message", "success");

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString());
    }

    public ResponseEntity<String> success(Object jo){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status_code", 200);
        jsonObject.put("message", "success");
        jsonObject.put("data", jo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString());
    }

    public ResponseEntity<String> badRequest(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status_code", 400);
        jsonObject.put("message", "bad request");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString());
    }

    public ResponseEntity<String> failed(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status_code", 417);
        jsonObject.put("message", "failed");

        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString());
    }

    public ResponseEntity<String> internalError(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status_code", 500);
        jsonObject.put("message", "internal server error");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonObject.toString());
    }
}
