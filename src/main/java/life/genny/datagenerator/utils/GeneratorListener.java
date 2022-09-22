package life.genny.datagenerator.utils;

public interface GeneratorListener {
        void onStart();

        void onFinish();

        void onSuccess();

        void onError(Throwable throwable);
}
