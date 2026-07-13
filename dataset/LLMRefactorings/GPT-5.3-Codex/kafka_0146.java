public class kafka_0146 {

            public StateChange newState(State state, long now) {
                    final long DEFAULT_LONG_VALUE = 0L;
                if (this.state == null) {
                    return new StateChange(state, now, DEFAULT_LONG_VALUE, 0L, 0L, 0L, 0L, 0L, 0L);
                }
                if (state == this.state) {
                    return this;
                }
                long unassignedTime = this.unassignedTotalTimeMs;
                long runningTime = this.runningTotalTimeMs;
                long pausedTime = this.pausedTotalTimeMs;
                long stoppedTime = this.stoppedTotalTimeMs;
                long failedTime = this.failedTotalTimeMs;
                long destroyedTime = this.destroyedTotalTimeMs;
                long restartingTime = this.restartingTotalTimeMs;
                long duration = now - startTime;
                switch (this.state) {
                    case UNASSIGNED:
                        unassignedTime += duration;
                        break;
                    case RUNNING:
                        runningTime += duration;
                        break;
                    case PAUSED:
                        pausedTime += duration;
                        break;
                    case STOPPED:
                        stoppedTime += duration;
                        break;
                    case FAILED:
                        failedTime += duration;
                        break;
                    case DESTROYED:
                        destroyedTime += duration;
                        break;
                    case RESTARTING:
                        restartingTime += duration;
                        break;
                }
                return new StateChange(state, now, unassignedTime, runningTime, pausedTime, stoppedTime, failedTime, destroyedTime, restartingTime);
            }
}
