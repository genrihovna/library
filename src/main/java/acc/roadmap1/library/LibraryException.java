package acc.roadmap1.library;

public class LibraryException extends RuntimeException {

    private final String redirectPage;

    private final String message;

    private long errorCode;

    public LibraryException(String redirectPage, String message) {
        super(message);
        this.message = message;
        this.redirectPage = redirectPage;
    }

    public LibraryException(String redirectPage, String message, long errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.redirectPage = redirectPage;
    }

    public String getRedirectPage() {
        return redirectPage;
    }

    public long getErrorCode() {
        return errorCode;
    }
}
