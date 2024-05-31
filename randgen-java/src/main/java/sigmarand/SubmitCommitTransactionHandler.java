package sigmarand;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import scorex.crypto.hash.Blake2b256;
import sigmarand.dao.RandomNumberGenerationTask;
import sigmarand.model.SubmitCommitTransactionRequest;
import sigmarand.model.SubmitCommitTransactionResponse;
import sigmarand.transaction.CommitTransaction;

import java.util.UUID;

public class SubmitCommitTransactionHandler implements RequestHandler<SubmitCommitTransactionRequest, SubmitCommitTransactionResponse> {
    private static final String NULL = "null";

    @Override
    public SubmitCommitTransactionResponse handleRequest(SubmitCommitTransactionRequest task, Context context) {
        LambdaLogger logger = context.getLogger();
        byte[] random = Blake2b256.hash(UUID.randomUUID().toString());
        String transactionId = new CommitTransaction(
                task.hashBoxId(),
                random,
                task.lockingContractAddress(),
                task.lockingTokenId(),
                task.lockingTokenAmount()
        ).submitTx();
        logger.log("Submitted commit transaction with id: " + transactionId);
        return new SubmitCommitTransactionResponse(
                task.taskId(),
                task.lockingContractAddress(),
                task.lockingTokenId(),
                task.lockingTokenAmount(),
                task.hashBoxId(),
                NULL,
                NULL,
                RandomNumberGenerationTask.TaskStatus.COMMIT_IN_PROGRESS,
                transactionId
        );
    }
}
