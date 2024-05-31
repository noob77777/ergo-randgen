package sigmarand.dao;

/**
 * Pojo class used for serialization to/from JSON.
 * <p>
 * TODO - Try to use the AutoValue class directly for serialization.
 */
public class RandomNumberGenerationTaskPojo {
    public String taskId;
    public String lockingContractAddress;
    public String lockingTokenId;
    public Integer lockingTokenAmount;
    public String hashBoxId;
    public String commitBoxId;
    public String revealBoxId;
    public RandomNumberGenerationTask.TaskStatus taskStatus;
    public String transactionId;
}
