package sigmarand;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import scorex.crypto.hash.Blake2b256;
import sigmarand.dao.RandomNumberGenerationTask;
import sigmarand.transaction.CommitTransaction;

import java.util.UUID;

public class SubmitCommitTransactionHandler implements RequestHandler<RandomNumberGenerationTask, RandomNumberGenerationTask> {
    private static final String NULL = "null";

    @Override
    public RandomNumberGenerationTask handleRequest(RandomNumberGenerationTask task, Context context) {
        LambdaLogger logger = context.getLogger();
        String random = Blake2b256.hash(UUID.randomUUID().toString());
        String transactionId = new CommitTransaction(
                task.hashBoxId(),
                random.getBytes(),
                task.lockingContractAddress(),
                task.lockingTokenId(),
                task.lockingTokenAmount()
        ).submitTx();
        logger.log("Submitted commit transaction with id: " + transactionId);
        return RandomNumberGenerationTask.builder()
                .setTransactionId(transactionId)
                .setTaskId(task.taskId())
                .setLockingContractAddress(task.lockingContractAddress())
                .setLockingTokenAmount(task.lockingTokenAmount())
                .setLockingTokenId(task.lockingTokenId())
                .setHashBoxId(task.hashBoxId())
                .setCommitBoxId(NULL)
                .setRevealBoxId(NULL)
                .setTaskStatus(RandomNumberGenerationTask.TaskStatus.COMMIT_IN_PROGRESS)
                .build();
    }
}
