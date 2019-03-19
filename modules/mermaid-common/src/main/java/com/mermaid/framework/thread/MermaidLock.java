package com.mermaid.framework.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Desription:独占锁实现，它在同一时刻只允许一个线程占有锁。
 *
 * @author:Hui CreateDate:2019/3/19 20:26
 * version 1.0
 */
public class MermaidLock implements Lock {
    //定义静态内部类
    private static class Sync extends AbstractQueuedSynchronizer {
        private static final int STATE_ZERO = 0;
        private static final int STATE_ONE = 1;
        //判断是否处于占用状态
        protected boolean isHeldExclusively() {
            return getState() == STATE_ONE;
        }

        //当状态为0,的时候获取锁
        public boolean tryAquire(int state) {
            if(compareAndSetState(STATE_ZERO,STATE_ONE)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        //当状态为1的时候释放锁
        public boolean tryRelease(int release) {
            if(getState() == STATE_ZERO) {
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(STATE_ZERO);
            return true;
        }

        Condition newCondition() {
            return new ConditionObject();
        }
    }

    private final Sync sync = new Sync();
    private final int state = 1;
    @Override
    public void lock() {
        sync.tryAquire(state);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(state);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAquire(state);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(state,unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.tryRelease(state);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
