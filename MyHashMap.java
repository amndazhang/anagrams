import java.io.Serializable;

public class MyHashMap<K,V> implements Serializable{
    private Object[] hashArray;
    private int size;
    private MyHashSet<K> keySet;
    
    public MyHashMap(){
        hashArray = new Object[100000];
        keySet = new MyHashSet<K>();
    }

    @ SuppressWarnings("unchecked")
    public V put(K key,V obj){
        // System.out.println(key.hashCode());
        V prev = (V)hashArray[key.hashCode()];
        hashArray[key.hashCode()] = obj;
        keySet.add(key);
        return prev;
    }

    @ SuppressWarnings("unchecked")
    
    public V get(Object o){
        K castedObj = (K) o;
        return (V) hashArray[castedObj.hashCode()];
    }
    @ SuppressWarnings("unchecked")
    public V remove(Object obj){
        K key = (K)obj;
        V removed = (V)hashArray[key.hashCode()];
        hashArray[key.hashCode()] = null;
        keySet.remove(key);
        return removed;
    }


    public int size(){
        return size;
    }

    public MyHashSet<K> keySet(){
        return keySet;
    }

    public String toString(){
        String text = "";
        //key list to DLList
        DLList<K> printV = (DLList<K>)keySet.toDLList();
        //go through keys get the DLList of classes add toString of classes to text
        // System.out.println(printV.size());
        for(int i = 0; i < printV.size(); i++){
            text += printV.get(i).toString() + "\n";
        }
        return text;
    }
}
