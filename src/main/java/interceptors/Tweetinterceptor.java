

/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

package interceptors;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

/**
 * Created by Justin on 24-2-2016.
 */


public class Tweetinterceptor {
    private static final Logger logger = Logger.getLogger("Tweetinterceptor");



    @AroundInvoke
    public Object aroundInvoke(InvocationContext context) throws Exception {
        Object[] parameters = context.getParameters();
        String tweetText = (String)parameters[1];
        tweetText = tweetText.replaceAll("vet", "dik");
        tweetText = tweetText.replaceAll("cool", "hard");
        parameters[1] = tweetText;
        context.setParameters(parameters);
        return context.proceed();
    }

}

