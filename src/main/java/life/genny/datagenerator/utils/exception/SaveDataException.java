package life.genny.datagenerator.utils.exception;

public class SaveDataException extends Exception {
    public SaveDataException(Throwable throwable) {
        super(throwable);
    }

    public SaveDataException(String message) {
        super(message);
    }
}
