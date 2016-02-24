import domain.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 23-2-2016.
 */
public class UserTests {

    private final List<User> users = new ArrayList();

    @Test
    public void newUsers(){
        User u4 = new User("Justin", "httpS", "geboren 4");
        users.add(u4);

        Assert.assertFalse(users.isEmpty());
    }
}
