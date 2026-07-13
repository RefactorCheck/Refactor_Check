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
                        ctx.writeAndFlush(result + QosConstants.BR_STR + QosProcessHandler.PROMPT);
                    }
                } catch (NoSuchCommandException ex) {
                    ctx.writeAndFlush(msg + " :no such command");
                    ctx.writeAndFlush(QosConstants.BR_STR + QosProcessHandler.PROMPT);
                    log.error(QOS_COMMAND_NOT_FOUND, "", "", "can not found command " + commandContext, ex);
                } catch (PermissionDenyException ex) {
                    ctx.writeAndFlush(msg + " :permission deny");
                    ctx.writeAndFlush(QosConstants.BR_STR + QosProcessHandler.PROMPT);
                    log.error(
                            QOS_PERMISSION_DENY_EXCEPTION,
                            "",
                            "",
                            "permission deny to access command " + commandContext,
                            ex);
                } catch (Exception ex) {
                    ctx.writeAndFlush(msg + " :fail to execute commandContext by " + ex.getMessage());
                    ctx.writeAndFlush(QosConstants.BR_STR + QosProcessHandler.PROMPT);
                    log.error(
                            QOS_UNEXPECTED_EXCEPTION, "", "", "execute commandContext got exception " + commandContext, ex);
                }
            }
        }
}
