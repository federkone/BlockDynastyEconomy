package lib.commands.abstractions;


public class CommandResult {
    private final boolean success;
    private final String errorMessage;

    public CommandResult(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    // Getters y métodos de utilidad
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
}
