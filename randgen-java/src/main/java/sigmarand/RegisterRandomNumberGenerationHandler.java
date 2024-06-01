package sigmarand;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.GsonBuilder;
import sigmarand.client.StepFnInvocator;
import sigmarand.dao.RandomNumberGenerationTask;
import sigmarand.model.RegisterRandomNumberGenerationTaskRequest;
import sigmarand.model.RegisterRandomNumberGenerationTaskResponse;
import sigmarand.transaction.RegisterTransaction;
import sigmarand.transaction.model.MUnsignedTransaction;
import sigmarand.transaction.util.Util;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sfn.model.StartExecutionResponse;

import java.util.UUID;

// Handler for Register Request
public class RegisterRandomNumberGenerationHandler implements RequestHandler<RegisterRandomNumberGenerationTaskRequest, RegisterRandomNumberGenerationTaskResponse> {
    private static final String STATE_MACHINE_ARN = "arn:aws:states:us-east-2:330797072184:stateMachine:CommitRandomNumber";

    @Override
    public RegisterRandomNumberGenerationTaskResponse handleRequest(RegisterRandomNumberGenerationTaskRequest req, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Sending from: " + req.address());
        logger.log("Random hash: " + req.randomHash());
        MUnsignedTransaction registerTransaction = registerTransaction(req);
        RandomNumberGenerationTask task = RandomNumberGenerationTask.builder()
                .setTaskId(UUID.randomUUID().toString())
                .setLockingContractAddress(req.lockingContractAddress())
                .setLockingTokenId(req.lockingTokenId())
                .setLockingTokenAmount(req.lockingTokenAmount())
                .setTransactionId(registerTransaction.id())
                .setTaskStatus(RandomNumberGenerationTask.TaskStatus.COMMIT_IN_PROGRESS)
                .build();
        StartExecutionResponse response = new StepFnInvocator(
                SfnClient.builder().region(Region.US_EAST_2).build(),
                STATE_MACHINE_ARN)
                .invoke(new GsonBuilder().serializeNulls().create().toJson(task));
        logger.log("Invocation successful: " + response.executionArn());
        return new RegisterRandomNumberGenerationTaskResponse(
                task.taskId(), registerTransaction.toJson(), RandomNumberGenerationTask.TaskStatus.COMMIT_IN_PROGRESS);
    }

    private MUnsignedTransaction registerTransaction(RegisterRandomNumberGenerationTaskRequest req) {
        return new RegisterTransaction(
                req.address(),
                Util.hexToBase64(req.randomHash()),
                req.lockingContractAddress(),
                req.lockingTokenId(),
                req.lockingTokenAmount()
        ).buildUnsignedTx();
    }
}
