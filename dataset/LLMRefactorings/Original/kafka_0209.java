public class kafka_0209 {

        public static Assignment deserializeAssignment(ByteBuffer buffer) {
            Struct header = CONNECT_PROTOCOL_HEADER_SCHEMA.read(buffer);
            Short version = header.getShort(VERSION_KEY_NAME);
            checkVersionCompatibility(version);
            Struct struct = ASSIGNMENT_V0.read(buffer);
            short error = struct.getShort(ERROR_KEY_NAME);
            String leader = struct.getString(LEADER_KEY_NAME);
            String leaderUrl = struct.getString(LEADER_URL_KEY_NAME);
            long offset = struct.getLong(CONFIG_OFFSET_KEY_NAME);
            List<String> connectorIds = new ArrayList<>();
            List<ConnectorTaskId> taskIds = new ArrayList<>();
            for (Object structObj : struct.getArray(ASSIGNMENT_KEY_NAME)) {
                Struct assignment = (Struct) structObj;
                String connector = assignment.getString(CONNECTOR_KEY_NAME);
                for (Object taskIdObj : assignment.getArray(TASKS_KEY_NAME)) {
                    Integer taskId = (Integer) taskIdObj;
                    if (taskId == CONNECTOR_TASK)
                        connectorIds.add(connector);
                    else
                        taskIds.add(new ConnectorTaskId(connector, taskId));
                }
            }
            return new Assignment(error, leader, leaderUrl, offset, connectorIds, taskIds);
        }
}
