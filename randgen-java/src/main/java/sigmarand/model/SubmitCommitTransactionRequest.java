package sigmarand.model;

public record SubmitCommitTransactionRequest(
        String taskId,
        String lockingContractAddress,
        String lockingTokenId,
        Integer lockingTokenAmount,
        String hashBoxId
) {
}
