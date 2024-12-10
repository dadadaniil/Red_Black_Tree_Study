package org.example.utils.handler;

public abstract class AbstractHandler implements Handler {
    protected Handler next;

    @Override
    public void setNext(Handler handler) {
        this.next = handler;
    }

    protected void nextHandle(Request request) {
        if (next != null) {
            next.handle(request);
        }
    }
}
