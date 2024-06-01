package sigmarand;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import sigmarand.client.StepFnInvocator;
import sigmarand.dao.RandomNumberGenerationTask;
import sigmarand.dao.RandomNumberGenerationTaskPojo;
import sigmarand.model.RevealRandomNumberRequest;
import sigmarand.model.RevealRandomNumberResponse;
import sigmarand.transaction.RevealTransaction;
import sigmarand.transaction.model.MUnsignedTransaction;
import sigmarand.transaction.util.Util;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sfn.model.StartExecutionResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RevealRandomNumberHandler implements RequestHandler<RevealRandomNumberRequest, RevealRandomNumberResponse> {

    private static final String baseUrl = "https://67zt8ejryg.execute-api.us-east-2.amazonaws.com/beta/random-number/task/";
    private static final String STATE_MACHINE_ARN = "arn:aws:states:us-east-2:330797072184:stateMachine:RevealRandomNumber";

    @Override
    public RevealRandomNumberResponse handleRequest(RevealRandomNumberRequest req, Context ctx) {
        LambdaLogger logger = ctx.getLogger();
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + req.taskId())).build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.log("DB response body:\n" + response.body());
            RandomNumberGenerationTaskPojo task = new Gson().fromJson(response.body(), RandomNumberGenerationTaskPojo.class);
            StartExecutionResponse executionResponse = new StepFnInvocator(
                    SfnClient.builder().region(Region.US_EAST_2).build(),
                    STATE_MACHINE_ARN)
                    .invoke(response.body());
            logger.log("Execution was successful: " + executionResponse);
            MUnsignedTransaction txn = new RevealTransaction(
                    task.commitBoxId,
                    Util.hexToBase64(req.random()),
                    req.address(),
                    task.lockingContractAddress,
                    task.lockingTokenId,
                    task.lockingTokenAmount
            ).buildUnsignedTx();
            return new RevealRandomNumberResponse(
                    req.taskId(),
                    RandomNumberGenerationTask.TaskStatus.REVEAL_IN_PROGRESS,
                    txn.toJson());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
