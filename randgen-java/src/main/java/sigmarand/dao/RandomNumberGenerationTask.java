package sigmarand.dao;

import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue
public abstract class RandomNumberGenerationTask {
    public abstract String taskId();
    public abstract String lockingContractAddress();
    public abstract String lockingTokenId();
    public abstract Integer lockingTokenAmount();
    @Nullable abstract String hashBoxId();
    @Nullable public abstract String commitBoxId();
    @Nullable public abstract String revealBoxId();
    public abstract TaskStatus taskStatus();
    public abstract String transactionId();

    public static Builder builder() {
        return new AutoValue_RandomNumberGenerationTask.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder setTaskId(String taskId);
        public abstract Builder setLockingContractAddress(String lockingContractAddress);
        public abstract Builder setLockingTokenId(String lockingTokenId);
        public abstract Builder setLockingTokenAmount(Integer lockingTokenAmount);
        public abstract Builder setHashBoxId(String hashBoxId);
        public abstract Builder setCommitBoxId(String commitBoxId);
        public abstract Builder setRevealBoxId(String revealBoxId);
        public abstract Builder setTaskStatus(TaskStatus taskStatus);
        public abstract Builder setTransactionId(String transactionId);
        public abstract RandomNumberGenerationTask build();
    }

    public enum TaskStatus {
        NOT_STARTED,
        COMMIT_IN_PROGRESS,
        COMMITED,
        REVEAL_IN_PROGRESS,
        COMPLETED
    }
}
