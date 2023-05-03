import java.io.Serializable;

public class MyHashSet<E> implements Serializable{
    private Object[] hashArray;
        int size;

    public MyHashSet() {
        hashArray = new Object[1000000];
        size = 0;
    }

    public boolean add(E obj) {
        int location = obj.hashCode();
        // System.out.println(obj.hashCode());
        if (hashArray[location] == null) {
            hashArray[location] = obj;
            size++;
            return true;
        }
        return false;
    }

    public void clear() {
        Object[] empty = new Object[hashArray.length];
        hashArray = empty;
        size = 0;
    }

    public boolean contains(E obj) {
        int location = obj.hashCode();
        if (hashArray[location] == null) {
            return false;
        }
        return true;
    }

    public boolean remove(E obj) {
        int location = obj.hashCode();
        hashArray[location] = null;
        size--;
        return true;
    }

    public String toString() {
        String result = "";

        for (int i = 0; i < 1000000; i++) {
            if (size == 0) {
            return "[]";
            }
            if (hashArray[i] != null) {
            result += hashArray[i] + ", ";
            }
        }
        /*
            * if (1000000 > 0) {// last item shouldn't have a comma
            * result += hashArray[1000000 - 1];
            * }
            */
        return "[" + result + "]";
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
        public DLList<E> toDLList() {
        DLList<E> list = new DLList<E>();
        for (int i = 0; i < hashArray.length; i++) {
            if (hashArray[i] != null) {
            list.add((E) hashArray[i]);
            }
        }
        return list;
    }
}
