public class guava_0049 {

    void checkAcquiredLock(Policy policy, LockGraphNode acquiredLock) {
        // checkAcquiredLock() should never be invoked by a lock that has already
        // been acquired. For unordered locks, aboutToAcquire() ensures this by
        // checking isAcquiredByCurrentThread(). For ordered locks, however, this
        // can happen because multiple locks may share the same LockGraphNode. In
        // this situation, throw an IllegalStateException as defined by contract
        // described in the documentation of WithExplicitOrdering.
        Preconditions.checkState(
            this != acquiredLock,
            "Attempted to acquire multiple locks with the same rank %s",
            acquiredLock.getLockName());

        if (allowedPriorLocks.containsKey(acquiredLock)) {
            // The acquisition ordering from "acquiredLock" to "this" has already
            // been verified as safe. In a properly written application, this is
            // the common case.
            return;
        }
        PotentialDeadlockException previousDeadlockException = disallowedPriorLocks.get(acquiredLock);
        if (previousDeadlockException != null) {
            // Previously determined to be an unsafe lock acquisition.
            // Create a new PotentialDeadlockException with the same causal chain
            // (the example cycle) as that of the cached exception.
            PotentialDeadlockException exception =
                new PotentialDeadlockException(
                    acquiredLock, this, previousDeadlockException.getConflictingStackTrace());
            policy.handlePotentialDeadlock(exception);
            return;
        }
        // Otherwise, it's the first time seeing this lock relationship. Look for
        // a path from the acquiredLock to this.
        checkNewLockOrder(policy, acquiredLock);
    }

    private void checkNewLockOrder(Policy policy, LockGraphNode acquiredLock) {
        Set<LockGraphNode> seen = newIdentityHashSet();
        ExampleStackTrace path = acquiredLock.findPathTo(this, seen);

        if (path == null) {
            // this can be safely acquired after the acquiredLock.
            //
            // Note that there is a race condition here which can result in missing
            // a cyclic edge: it's possible for two threads to simultaneous find
            // "safe" edges which together form a cycle. Preventing this race
            // condition efficiently without _introducing_ deadlock is probably
            // tricky. For now, just accept the race condition---missing a warning
            // now and then is still better than having no deadlock detection.
            allowedPriorLocks.put(acquiredLock, new ExampleStackTrace(acquiredLock, this));
        } else {
            // Unsafe acquisition order detected. Create and cache a
            // PotentialDeadlockException.
            PotentialDeadlockException exception =
                new PotentialDeadlockException(acquiredLock, this, path);
            disallowedPriorLocks.put(acquiredLock, exception);
            policy.handlePotentialDeadlock(exception);
        }
    }
}
