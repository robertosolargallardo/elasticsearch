package com.tbd.elasticsearch;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @RequestMapping(value = "/test", method = RequestMethod.GET)
     public JSONObject HelloWorld() throws JSONException {
        JSONObject res = new JSONObject();
        //LOGGER.info("HelloWorld Test!");
        res.put("data", "hello world!");
        res.put("errCode", 0);
        return res;
    }
}