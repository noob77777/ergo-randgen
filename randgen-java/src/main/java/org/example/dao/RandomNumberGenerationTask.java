package org.example.dao;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RandomNumberGenerationTask {
    public abstract String taskId();
    public abstract String lockingContractAddress();
    public abstract String lockingTokenId();
    public abstract Integer lockingTokenAmount();
    public abstract String hashBoxId();
    public abstract String commitBoxId();
    public abstract String revealBoxId();
    public abstract TaskStatus taskStatus();

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
        public abstract RandomNumberGenerationTask build();
    }

    public enum TaskStatus {
        NOT_STARTED,
        COMMITED,
        REVEAL_IN_PROGRESS,
        COMPLETED
    }
}
