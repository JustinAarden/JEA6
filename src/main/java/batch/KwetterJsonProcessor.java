/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batch;

import domain.Tweet;

import javax.batch.api.chunk.ItemProcessor;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Dependent//http://docs.oracle.com/javaee/6/api/javax/enterprise/context/Dependent.html
@Named//("kwetterJsonProcessor")
public class kwetterJsonProcessor implements ItemProcessor {

    @Override
    public Object[] processItem(Object t) {
        //http://stackoverflow.com/questions/199718/can-you-instantiate-an-object-instance-from-json-in-net
        //^ This tutorial is based on a .net application, but it's very similar to java.
        String username = "";
        String tweetText = "";
        Date date = new Date();
        String postedFrom = "";

        String json = (String) t;
        while (!json.startsWith("{") & json.length() > 0) { //Every new json line starts with {
            json = json.substring(1, json.length());
        }
        while (!json.endsWith("}") & json.length() > 0) { //Every json line ends with }
            json = json.substring(0, json.length() - 1);
        }
        if (json.indexOf("}") <= json.indexOf("{")) { //When the index of } is bigger then {  the json string is complete ie. {someJsonstuffinhere}
            return new Object[]{null, null};
        }


        JsonParserFactory factory = Json.createParserFactory(null);
        JsonParser parser = factory.createParser(new StringReader(json));

        String key = "";
        String value = "";
        HashMap<String, String> map = new HashMap<>();
        while (parser.hasNext()) {
            switch (parser.next()) {
                case KEY_NAME:
                    key = parser.getString();
                    break;
                case VALUE_STRING:
                    value = parser.getString();
                    map.put(key, value);
                    break;
            }
        }

        username = map.get("screenName");
        tweetText = map.get("tweet");
        postedFrom = map.get("postedFrom");
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get("postDate"));
        } catch (ParseException ex) {
            Logger.getLogger(kwetterJsonProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

        Object[] result = new Object[]{new Tweet(tweetText, date, postedFrom), username};
        System.out.println(Arrays.deepToString(result));
        return result;
    }
}
