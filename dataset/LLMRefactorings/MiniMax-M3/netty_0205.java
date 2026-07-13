public class netty_0205 {

    private void handlePathEvents(QuicheQuicConnection conn) {
        long event;
        while (!conn.isFreed() && (event = Quiche.quiche_conn_path_event_next(conn.address())) > 0) {
            try {
                handlePathEvent(Quiche.quiche_path_event_type(event), event);
            } finally {
                Quiche.quiche_path_event_free(event);
            }
        }
    }

    private void handlePathEvent(int type, long event) {
        if (type == Quiche.QUICHE_PATH_EVENT_NEW) {
            Object[] ret = Quiche.quiche_path_event_new(event);
            InetSocketAddress local = (InetSocketAddress) ret[0];
            InetSocketAddress peer = (InetSocketAddress) ret[1];
            pipeline().fireUserEventTriggered(new QuicPathEvent.New(local, peer));
        } else if (type == Quiche.QUICHE_PATH_EVENT_VALIDATED) {
            Object[] ret = Quiche.quiche_path_event_validated(event);
            InetSocketAddress local = (InetSocketAddress) ret[0];
            InetSocketAddress peer = (InetSocketAddress) ret[1];
            pipeline().fireUserEventTriggered(new QuicPathEvent.Validated(local, peer));
        } else if (type == Quiche.QUICHE_PATH_EVENT_FAILED_VALIDATION) {
            Object[] ret = Quiche.quiche_path_event_failed_validation(event);
            InetSocketAddress local = (InetSocketAddress) ret[0];
            InetSocketAddress peer = (InetSocketAddress) ret[1];
            pipeline().fireUserEventTriggered(new QuicPathEvent.FailedValidation(local, peer));
        } else if (type == Quiche.QUICHE_PATH_EVENT_CLOSED) {
            Object[] ret = Quiche.quiche_path_event_closed(event);
            InetSocketAddress local = (InetSocketAddress) ret[0];
            InetSocketAddress peer = (InetSocketAddress) ret[1];
            pipeline().fireUserEventTriggered(new QuicPathEvent.Closed(local, peer));
        } else if (type == Quiche.QUICHE_PATH_EVENT_REUSED_SOURCE_CONNECTION_ID) {
            Object[] ret = Quiche.quiche_path_event_reused_source_connection_id(event);
            Long seq = (Long) ret[0];
            InetSocketAddress localOld = (InetSocketAddress) ret[1];
            InetSocketAddress peerOld = (InetSocketAddress) ret[2];
            InetSocketAddress local = (InetSocketAddress) ret[3];
            InetSocketAddress peer = (InetSocketAddress) ret[4];
            pipeline().fireUserEventTriggered(
                    new QuicPathEvent.ReusedSourceConnectionId(seq, localOld, peerOld, local, peer));
        } else if (type == Quiche.QUICHE_PATH_EVENT_PEER_MIGRATED) {
            Object[] ret = Quiche.quiche_path_event_peer_migrated(event);
            InetSocketAddress local = (InetSocketAddress) ret[0];
            InetSocketAddress peer = (InetSocketAddress) ret[1];
            pipeline().fireUserEventTriggered(new QuicPathEvent.PeerMigrated(local, peer));
        }
    }
}
