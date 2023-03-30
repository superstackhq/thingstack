package one.superstack.thingstack.auth.actor;

public class ThreadLocalWrapper {

    private static final ThreadLocal<AuthenticatedActor> actorContext;

    static {
        actorContext = new ThreadLocal<>();
    }

    public static AuthenticatedActor getActor() {
        return actorContext.get();
    }

    public static void setActor(AuthenticatedActor actor) {
        actorContext.set(actor);
    }
}
