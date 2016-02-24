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
/*        String className = context.getMethod().getDeclaringClass().getName();
        String method = context.getMethod().getName();
        Object[] param1 = context.getParameters();
        String test = param1.toString();
        logger.log(Level.INFO, "Executing method : {0} in class : {1}", new String[]{method, className});


       // String test = context.getParameters().getClass().getName().*/
        return context.proceed();
    }

}
