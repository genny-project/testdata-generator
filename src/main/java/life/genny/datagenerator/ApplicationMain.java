package life.genny.datagenerator;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class ApplicationMain {
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
