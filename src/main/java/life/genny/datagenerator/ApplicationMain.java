package life.genny.datagenerator;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.jboss.logging.Logger;

@QuarkusMain
public class ApplicationMain {
    private static final Logger LOGGER = Logger.getLogger(ApplicationMain.class.getSimpleName());

    public static void main(String ...args) {
        Quarkus.run(BaseMain.class, args);
    }

    public static class BaseMain implements QuarkusApplication {

        @Override
        public int run(String... args) throws Exception {
            Quarkus.waitForExit();
            return 0;
        }
    }

}
