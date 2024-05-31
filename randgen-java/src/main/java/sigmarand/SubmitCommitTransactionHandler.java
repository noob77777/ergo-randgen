package sigmarand;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import sigmarand.dao.RandomNumberGenerationTask;
import sigmarand.transaction.model.MUnsignedTransaction;

public class SubmitCommitTransactionHandler implements RequestHandler<RandomNumberGenerationTask, RandomNumberGenerationTask> {

    @Override
    public RandomNumberGenerationTask handleRequest(RandomNumberGenerationTask randomNumberGenerationTask, Context context) {
        return null;
    }
}
