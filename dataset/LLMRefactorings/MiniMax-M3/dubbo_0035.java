public class dubbo_0035 {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    
            if (StringUtils.isBlank(msg)) {
                ctx.writeAndFlush(QosProcessHandler.PROMPT);
            } else {
                CommandContext commandContext = TelnetCommandDecoder.decode(msg);
                commandContext.setQosConfiguration(qosConfiguration);
                commandContext.setRemote(ctx.channel());
    
                try {
                    String result = commandExecutor.execute(commandContext);
                    if (StringUtils.isEquals(QosConstants.CLOSE, result)) {
                        ctx.writeAndFlush(getByeLabel()).addListener(ChannelFutureListener.CLOSE);
                    } else {
                        writePrompt(ctx, result + QosConstants.BR_STR);
                    }
                } catch (NoSuchCommandException ex) {
                    writePrompt(ctx, msg + " :no such command");
                    log.error(QOS_COMMAND_NOT_FOUND, "", "", "can not found command " + commandContext, ex);
                } catch (PermissionDenyException ex) {
                    writePrompt(ctx, msg + " :permission deny");
                    log.error(
                            QOS_PERMISSION_DENY_EXCEPTION,
                            "",
                            "",
                            "permission deny to access command " + commandContext,
                            ex);
                } catch (Exception ex) {
                    writePrompt(ctx, msg + " :fail to execute commandContext by " + ex.getMessage());
                    log.error(
                            QOS_UNEXPECTED_EXCEPTION, "", "", "execute commandContext got exception " + commandContext, ex);
                }
            }
        }

        private void writePrompt(ChannelHandlerContext ctx, String message) {
            ctx.writeAndFlush(message);
            ctx.writeAndFlush(QosConstants.BR_STR + QosProcessHandler.PROMPT);
        }
}
