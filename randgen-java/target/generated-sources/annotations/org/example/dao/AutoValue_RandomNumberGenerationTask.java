package org.example.dao;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
final class AutoValue_RandomNumberGenerationTask extends RandomNumberGenerationTask {

  private final String taskId;

  private final String lockingContractAddress;

  private final String lockingTokenId;

  private final Integer lockingTokenAmount;

  private final String hashBoxId;

  private final String commitBoxId;

  private final String revealBoxId;

  private final RandomNumberGenerationTask.TaskStatus taskStatus;

  private AutoValue_RandomNumberGenerationTask(
      String taskId,
      String lockingContractAddress,
      String lockingTokenId,
      Integer lockingTokenAmount,
      String hashBoxId,
      String commitBoxId,
      String revealBoxId,
      RandomNumberGenerationTask.TaskStatus taskStatus) {
    this.taskId = taskId;
    this.lockingContractAddress = lockingContractAddress;
    this.lockingTokenId = lockingTokenId;
    this.lockingTokenAmount = lockingTokenAmount;
    this.hashBoxId = hashBoxId;
    this.commitBoxId = commitBoxId;
    this.revealBoxId = revealBoxId;
    this.taskStatus = taskStatus;
  }

  @Override
  public String taskId() {
    return taskId;
  }

  @Override
  public String lockingContractAddress() {
    return lockingContractAddress;
  }

  @Override
  public String lockingTokenId() {
    return lockingTokenId;
  }

  @Override
  public Integer lockingTokenAmount() {
    return lockingTokenAmount;
  }

  @Override
  public String hashBoxId() {
    return hashBoxId;
  }

  @Override
  public String commitBoxId() {
    return commitBoxId;
  }

  @Override
  public String revealBoxId() {
    return revealBoxId;
  }

  @Override
  public RandomNumberGenerationTask.TaskStatus taskStatus() {
    return taskStatus;
  }

  @Override
  public String toString() {
    return "RandomNumberGenerationTask{"
        + "taskId=" + taskId + ", "
        + "lockingContractAddress=" + lockingContractAddress + ", "
        + "lockingTokenId=" + lockingTokenId + ", "
        + "lockingTokenAmount=" + lockingTokenAmount + ", "
        + "hashBoxId=" + hashBoxId + ", "
        + "commitBoxId=" + commitBoxId + ", "
        + "revealBoxId=" + revealBoxId + ", "
        + "taskStatus=" + taskStatus
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof RandomNumberGenerationTask) {
      RandomNumberGenerationTask that = (RandomNumberGenerationTask) o;
      return this.taskId.equals(that.taskId())
          && this.lockingContractAddress.equals(that.lockingContractAddress())
          && this.lockingTokenId.equals(that.lockingTokenId())
          && this.lockingTokenAmount.equals(that.lockingTokenAmount())
          && this.hashBoxId.equals(that.hashBoxId())
          && this.commitBoxId.equals(that.commitBoxId())
          && this.revealBoxId.equals(that.revealBoxId())
          && this.taskStatus.equals(that.taskStatus());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= taskId.hashCode();
    h$ *= 1000003;
    h$ ^= lockingContractAddress.hashCode();
    h$ *= 1000003;
    h$ ^= lockingTokenId.hashCode();
    h$ *= 1000003;
    h$ ^= lockingTokenAmount.hashCode();
    h$ *= 1000003;
    h$ ^= hashBoxId.hashCode();
    h$ *= 1000003;
    h$ ^= commitBoxId.hashCode();
    h$ *= 1000003;
    h$ ^= revealBoxId.hashCode();
    h$ *= 1000003;
    h$ ^= taskStatus.hashCode();
    return h$;
  }

  static final class Builder extends RandomNumberGenerationTask.Builder {
    private String taskId;
    private String lockingContractAddress;
    private String lockingTokenId;
    private Integer lockingTokenAmount;
    private String hashBoxId;
    private String commitBoxId;
    private String revealBoxId;
    private RandomNumberGenerationTask.TaskStatus taskStatus;
    Builder() {
    }
    @Override
    public RandomNumberGenerationTask.Builder setTaskId(String taskId) {
      if (taskId == null) {
        throw new NullPointerException("Null taskId");
      }
      this.taskId = taskId;
      return this;
    }
    @Override
    public RandomNumberGenerationTask.Builder setLockingContractAddress(String lockingContractAddress) {
      if (lockingContractAddress == null) {
        throw new NullPointerException("Null lockingContractAddress");
      }
      this.lockingContractAddress = lockingContractAddress;
      return this;
    }
    @Override
    public RandomNumberGenerationTask.Builder setLockingTokenId(String lockingTokenId) {
      if (lockingTokenId == null) {
        throw new NullPointerException("Null lockingTokenId");
      }
      this.lockingTokenId = lockingTokenId;
      return this;
    }
    @Override
    public RandomNumberGenerationTask.Builder setLockingTokenAmount(Integer lockingTokenAmount) {
      if (lockingTokenAmount == null) {
        throw new NullPointerException("Null lockingTokenAmount");
      }
      this.lockingTokenAmount = lockingTokenAmount;
      return this;
    }
    @Override
    public RandomNumberGenerationTask.Builder setHashBoxId(String hashBoxId) {
      if (hashBoxId == null) {
        throw new NullPointerException("Null hashBoxId");
      }
      this.hashBoxId = hashBoxId;
      return this;
    }
    @Override
    public RandomNumberGenerationTask.Builder setCommitBoxId(String commitBoxId) {
      if (commitBoxId == null) {
        throw new NullPointerException("Null commitBoxId");
      }
      this.commitBoxId = commitBoxId;
      return this;
    }
    @Override
    public RandomNumberGenerationTask.Builder setRevealBoxId(String revealBoxId) {
      if (revealBoxId == null) {
        throw new NullPointerException("Null revealBoxId");
      }
      this.revealBoxId = revealBoxId;
      return this;
    }
    @Override
    public RandomNumberGenerationTask.Builder setTaskStatus(RandomNumberGenerationTask.TaskStatus taskStatus) {
      if (taskStatus == null) {
        throw new NullPointerException("Null taskStatus");
      }
      this.taskStatus = taskStatus;
      return this;
    }
    @Override
    public RandomNumberGenerationTask build() {
      if (this.taskId == null
          || this.lockingContractAddress == null
          || this.lockingTokenId == null
          || this.lockingTokenAmount == null
          || this.hashBoxId == null
          || this.commitBoxId == null
          || this.revealBoxId == null
          || this.taskStatus == null) {
        StringBuilder missing = new StringBuilder();
        if (this.taskId == null) {
          missing.append(" taskId");
        }
        if (this.lockingContractAddress == null) {
          missing.append(" lockingContractAddress");
        }
        if (this.lockingTokenId == null) {
          missing.append(" lockingTokenId");
        }
        if (this.lockingTokenAmount == null) {
          missing.append(" lockingTokenAmount");
        }
        if (this.hashBoxId == null) {
          missing.append(" hashBoxId");
        }
        if (this.commitBoxId == null) {
          missing.append(" commitBoxId");
        }
        if (this.revealBoxId == null) {
          missing.append(" revealBoxId");
        }
        if (this.taskStatus == null) {
          missing.append(" taskStatus");
        }
        throw new IllegalStateException("Missing required properties:" + missing);
      }
      return new AutoValue_RandomNumberGenerationTask(
          this.taskId,
          this.lockingContractAddress,
          this.lockingTokenId,
          this.lockingTokenAmount,
          this.hashBoxId,
          this.commitBoxId,
          this.revealBoxId,
          this.taskStatus);
    }
  }

}
