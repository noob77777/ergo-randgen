package sigmarand.model;

import sigmarand.dao.RandomNumberGenerationTask;

public record RegisterRandomNumberGenerationTaskResponse(
        String taskId,
        String unsignedTransaction
) {}
