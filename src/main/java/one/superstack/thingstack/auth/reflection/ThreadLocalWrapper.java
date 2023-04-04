package one.superstack.thingstack.auth.reflection;

public class ThreadLocalWrapper {

    private static final ThreadLocal<AuthenticatedReflection> reflectionContext;

    static {
        reflectionContext = new ThreadLocal<>();
    }

    public static AuthenticatedReflection getReflection() {
        return reflectionContext.get();
    }

    public static void setReflection(AuthenticatedReflection reflection) {
        reflectionContext.set(reflection);
    }
}
