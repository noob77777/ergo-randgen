package sigmarand.model;

public record RegisterRandomNumberGenerationTaskResponse(
        String taskId,
        String unsignedTransaction
) {}
