package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.example.dao.RandomNumberGenerationTask;
import sigmarand.transaction.RegisterTransaction;

// Handler for Register Request
public class Handler implements RequestHandler<RegisterRandomNumberGenerationTaskRequest, RegisterRandomNumberGenerationTaskResponse> {

    @Override
    public RegisterRandomNumberGenerationTaskResponse handleRequest(RegisterRandomNumberGenerationTaskRequest req, Context context)
    {
        LambdaLogger logger = context.getLogger();
        logger.log("Sending from: " + req.addresses().getFirst());
        logger.log("Random hash: " + req.randomHash());
        registerTransaction(req);
        return new RegisterRandomNumberGenerationTaskResponse(RandomNumberGenerationTask.builder().build());
    }

    private void registerTransaction(RegisterRandomNumberGenerationTaskRequest req) {
        new RegisterTransaction(
                req.addresses().getFirst(),
                req.randomHash().getBytes(),
                req.lockingContractAddress(),
                req.lockingTokenId(),
                req.lockingTokenAmount()
        ).buildUnsignedTx();
    }
}