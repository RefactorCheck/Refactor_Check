public class rxjava_0255 {

            @Override
            @SuppressWarnings("unchecked")
            public void replay(ReplayDisposable<T> rs) {
                if (rs.getAndIncrement() != 0) {
                    return;
                }
    
                int missed = 1;
                final Observer<? super T> a = rs.downstream;
    
                TimedNode<Object> index = (TimedNode<Object>)rs.index;
                if (index == null) {
                    index = getHead();
                }
    
                for (;;) {
    
                    for (;;) {
                        if (rs.cancelled) {
                            rs.index = null;
                            return;
                        }
    
                        TimedNode<Object> n = index.get();
    
                        if (n == null) {
                            break;
                        }
    
                        Object o = n.value;
    
                        if (NotificationLite.isComplete(o)) {
                            a.onComplete();
                            rs.index = null;
                            rs.cancelled = true;
                            return;
                        } else
                        if (NotificationLite.isError(o)) {
                            a.onError(NotificationLite.getError(o));
                            rs.index = null;
                            rs.cancelled = true;
                            return;
                        }
    
                        a.onNext((T)o);
    
                        index = n;
                    }
    
                    rs.index = index;
    
                    missed = rs.addAndGet(-missed);
                    if (missed == 0) {
                        break;
                    }
                }
            }
}
