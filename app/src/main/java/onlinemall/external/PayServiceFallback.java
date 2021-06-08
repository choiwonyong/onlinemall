package onlinemall.external;

public class PayServiceFallback implements PayService{
    @Override
    public void payment (Pay pay) {
        //do nothing if you want to forgive it

        System.out.println("Circuit breaker has been opened. Fallback returned instead.");
    }
}
