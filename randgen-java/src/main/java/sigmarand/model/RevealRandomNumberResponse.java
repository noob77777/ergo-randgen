package sigmarand.model;

import sigmarand.dao.RandomNumberGenerationTask;

public record RevealRandomNumberResponse(
        String taskId,
        RandomNumberGenerationTask.TaskStatus taskStatus,
        String unsignedTransaction
) {}
