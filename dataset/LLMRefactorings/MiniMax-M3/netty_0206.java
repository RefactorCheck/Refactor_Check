public class netty_0206 {

    @Override
    protected boolean handleResponse(ChannelHandlerContext ctx, Object response) throws Exception {
        if (response instanceof Socks5InitialResponse) {
            return handleInitialResponse(ctx, (Socks5InitialResponse) response);
        }

        if (response instanceof Socks5PasswordAuthResponse) {
            // Received an authentication response from the server.
            Socks5PasswordAuthResponse res = (Socks5PasswordAuthResponse) response;
            if (res.status() != Socks5PasswordAuthStatus.SUCCESS) {
                throw new ProxyConnectException(exceptionMessage("authStatus: " + res.status()));
            }

            sendConnectCommand(ctx);
            return false;
        }

        if (response instanceof Socks5PrivateAuthResponse) {
            Socks5PrivateAuthResponse res = (Socks5PrivateAuthResponse) response;
            if (res.status() != Socks5PrivateAuthStatus.SUCCESS) {
                throw new ProxyConnectException(exceptionMessage("privateAuthStatus: " + res.status()));
            }

            sendConnectCommand(ctx);
            return false;
        }

        // This should be the last message from the server.
        Socks5CommandResponse res = (Socks5CommandResponse) response;
        if (res.status() != Socks5CommandStatus.SUCCESS) {
            throw new ProxyConnectException(exceptionMessage("status: " + res.status()));
        }

        return true;
    }

    private boolean handleInitialResponse(ChannelHandlerContext ctx, Socks5InitialResponse res) throws Exception {
        Socks5AuthMethod authMethod = socksAuthMethod();
        Socks5AuthMethod resAuthMethod = res.authMethod();
        if (resAuthMethod != Socks5AuthMethod.NO_AUTH && resAuthMethod != authMethod
            && !Socks5AuthMethod.isPrivateMethod(resAuthMethod.byteValue())) {
            // Server did not allow unauthenticated access nor accept the requested authentication scheme.
            throw new ProxyConnectException(exceptionMessage("unexpected authMethod: " + res.authMethod()));
        }

        if (resAuthMethod == Socks5AuthMethod.NO_AUTH) {
            sendConnectCommand(ctx);
        } else if (resAuthMethod == Socks5AuthMethod.PASSWORD) {
            // In case of password authentication, send an authentication request.
            ctx.pipeline().replace(decoderName, decoderName, new Socks5PasswordAuthResponseDecoder());
            sendToProxyServer(new DefaultSocks5PasswordAuthRequest(
                    username != null? username : "", password != null? password : ""));
        } else if (Socks5AuthMethod.isPrivateMethod(resAuthMethod.byteValue())) {
            ctx.pipeline().replace(decoderName, decoderName, new Socks5PrivateAuthResponseDecoder());
            sendToProxyServer(new DefaultSocks5PrivateAuthRequest(privateToken));
        } else {
            // Should never reach here.
            throw new Error("Unexpected authMethod: " + resAuthMethod);
        }

        return false;
    }
}
