package hsrm.eibo.mediaplayer.Core.Controller;

import java.util.*;

/**
 * A observable helper class, which manages error and exception handling.
 */
public class ErrorHandler extends Observable{

    /**
     * A List of all occured errors and exceptions.
     */
    private List<Throwable> collectedErrors = new ArrayList<>();

    /**
     * The summary text of the last occured errors.
     */
    private String lastErrorSummary;

    /**
     * singleton instance
     */
    private static ErrorHandler instance = new ErrorHandler();
    private ErrorHandler() {}
    public static ErrorHandler getInstance()
    {
        return instance;
    }

    /**
     * Initializes the UncaughtExceptionHandler of this class.
     */
    public static void initExceptionHandling() {
        ErrorHandler err = ErrorHandler.getInstance();
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
            err.addError(e);
            err.notifyErrorObserver("Es ist ein unerwarteter Fehler aufgetreten");
        });
    }

    /**
     * Adds a new error to the internal list of occurred errors.
     * @param exception the error to add
     */
    public void addError(Throwable exception)
    {
        this.setChanged();
        this.collectedErrors.add(exception);
    }

    /**
     *  Removed the given error from the internal list of occurred errors.
     * @param exception the error to remove
     */
    public void removeError(Throwable exception)
    {
        this.collectedErrors.remove(exception);
    }

    /**
     * Returns all occured errors.
     * Should be called by Observer.
     * @return an {@link ArrayList} of collected errors.
     */
    public List<Throwable> getCollectedErrors()
    {
        return this.collectedErrors;
    }

    /**
     * Returns the describing summary text of the last occured errors. (When {@link #notifyErrorObserver(String)} was called)
     * Should be called by Observer.
     * @return the error summary as String
     */
    public String getLastErrorSummary() {
        return lastErrorSummary;
    }

    /**
     * Notifies all registered Observers and sets the internal error summary text.
     * @param summaryText a human-readable describing error summary of all occured errors.
     */
    public void notifyErrorObserver(String summaryText)
    {
        this.lastErrorSummary = summaryText;
        this.notifyObservers();
        this.collectedErrors.clear();
    }
}
