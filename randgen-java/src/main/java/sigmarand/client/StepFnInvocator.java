package sigmarand.client;

import com.google.gson.Gson;
import sigmarand.dao.RandomNumberGenerationTask;
import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sfn.model.StartExecutionRequest;
import software.amazon.awssdk.services.sfn.model.StartExecutionResponse;
import java.util.UUID;

public class StepFnInvocator {
    private final SfnClient sfnClient;
    private final String stateMachineArn;

    public StepFnInvocator(SfnClient sfnClient, String stateMachineArn) {
        this.sfnClient = sfnClient;
        this.stateMachineArn = stateMachineArn;
    }

    public StartExecutionResponse invoke(RandomNumberGenerationTask task) {
        StartExecutionRequest executionRequest = StartExecutionRequest.builder()
                .input(new Gson().toJson(task))
                .stateMachineArn(stateMachineArn)
                .name(UUID.randomUUID().toString())
                .build();

        return sfnClient.startExecution(executionRequest);
    }
}
