package hsrm.eibo.mediaplayer.Core.Controller;

import java.util.*;

public class ErrorHandler extends Observable{

    private List<Throwable> collectedErrors = new ArrayList<>();
    private String lastErrorSummary;

    private static ErrorHandler instance = new ErrorHandler();
    private ErrorHandler() {}
    public static ErrorHandler getInstance()
    {
        return instance;
    }

    public void addError(Throwable exception)
    {
        this.setChanged();
        this.collectedErrors.add(exception);
    }

    public void removeErrorMessage(Throwable exception)
    {
        this.collectedErrors.remove(exception);
    }

    public List<Throwable> getCollectedErrors()
    {
        return this.collectedErrors;
    }

    public String getLastErrorSummary() {
        return lastErrorSummary;
    }

    public void notifyErrorObserver(String summaryText)
    {
        this.lastErrorSummary = summaryText;
        this.notifyObservers();
        this.collectedErrors.clear();
    }
}
