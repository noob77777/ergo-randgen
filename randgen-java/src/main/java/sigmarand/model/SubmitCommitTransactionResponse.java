package sigmarand.model;

import sigmarand.dao.RandomNumberGenerationTask;

public record SubmitCommitTransactionResponse(
        String taskId,
        String lockingContractAddress,
        String lockingTokenId,
        Integer lockingTokenAmount,
        String hashBoxId,
        String commitBoxId,
        String revealBoxId,
        RandomNumberGenerationTask.TaskStatus taskStatus,
        String transactionId
) {
}
