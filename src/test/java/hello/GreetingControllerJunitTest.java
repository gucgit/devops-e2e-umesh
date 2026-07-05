package hello;

import org.junit.jupiter.api.Test;

public class GreetingControllerJunitTest {

    GreetingController greetingController = new GreetingController();

    @Test
    public void shouldGreetings() {
        greetingController.greeting("DellEMC");
    }

}
