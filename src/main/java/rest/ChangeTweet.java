package rest;

import javax.annotation.PostConstruct;
import javax.interceptor.InvocationContext;

/**
 * Created by Justin on 23-2-2016.
 */
public class ChangeTweet {
    @PostConstruct
    void postConstruct(InvocationContext context) {
        try {
            context.proceed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
