package hsrm.eibo.mediaplayer.Core.Controller;

import java.util.*;

public class ErrorHandler extends Observable{

    private List<Exception> collectedErrors = new ArrayList<>();
    private String lastErrorSummary;

    private static ErrorHandler instance = new ErrorHandler();
    private ErrorHandler() {}
    public static ErrorHandler getInstance()
    {
        return instance;
    }

    public void addError(Exception exception)
    {
            this.collectedErrors.add(exception);
    }

    public void removeErrorMessage(Exception exception)
    {
        this.collectedErrors.remove(exception);
    }

    public List<Exception> getCollectedErrors()
    {
        return this.collectedErrors;
    }

    public String getLastErrorSummary() {
        return lastErrorSummary;
    }

    public void notifyErrorObserver(Exception key, String summaryText)
    {
        this.lastErrorSummary = summaryText;
        this.notifyObservers(key);
        this.collectedErrors.clear();
    }

}
