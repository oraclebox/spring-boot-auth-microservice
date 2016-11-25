package me.oraclebox.auth.service.exception

class AuthenticationException extends Exception {
    AuthenticationException() {
    }

    AuthenticationException(String var1) {
        super(var1)
    }

    AuthenticationException(String var1, Throwable var2) {
        super(var1, var2)
    }

    AuthenticationException(Throwable var1) {
        super(var1)
    }

    AuthenticationException(String var1, Throwable var2, boolean var3, boolean var4) {
        super(var1, var2, var3, var4)
    }
}
