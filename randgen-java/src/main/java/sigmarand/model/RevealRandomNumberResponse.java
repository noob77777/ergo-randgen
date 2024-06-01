package sigmarand.model;

import sigmarand.dao.RandomNumberGenerationTask;

public record RevealRandomNumberResponse(
        String taskId,
        String unsignedTransaction,
        RandomNumberGenerationTask.TaskStatus taskStatus
) {
}
