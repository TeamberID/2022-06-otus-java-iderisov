import java.util.*;

public class CustomerService {

    private Comparator comparator = Comparator.comparingLong(Customer::getScores);
    private NavigableMap<Customer, String> map = new TreeMap(comparator);

    public Map.Entry<Customer, String> getSmallest() {
        return copy(map.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return copy(map.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }

    private Map.Entry<Customer, String> copy(Map.Entry<Customer, String> entry) {
        if (entry == null) {
            return null;
        }

        Customer original = entry.getKey();
        return Map.entry(new Customer(original.getId(), original.getName(), original.getScores()), entry.getValue());
    }
}
