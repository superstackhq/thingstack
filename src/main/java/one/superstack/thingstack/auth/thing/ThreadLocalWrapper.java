package one.superstack.thingstack.auth.thing;

public class ThreadLocalWrapper {

    private static final ThreadLocal<AuthenticatedThing> thingContext;

    static {
        thingContext = new ThreadLocal<>();
    }

    public static AuthenticatedThing getThing() {
        return thingContext.get();
    }

    public static void setThing(AuthenticatedThing thing) {
        thingContext.set(thing);
    }
}
