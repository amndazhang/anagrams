import java.io.Serializable;

public class DLList<E> implements Serializable {
	
	private Node<E> head;
	private Node<E> tail;
	private int size;
	
	public DLList() {
		head = new Node<E>(null);
		tail = new Node<E>(null);
		head.setNext(tail);
		tail.setPrev(head);
		size = 0;
	}
	
	private Node<E> getNode(int index) {
		if (!inBounds(index))
			return null;
		Node<E> current;
		if (index < size / 2) {
			current = head.next();
			for (int i = 0; i < index; i++)
				current = current.next();
		} else {
			current = tail.prev();
			for (int i = size-1; i > index; i--)
				current = current.prev();
		}
		return current;
	}
	
	public boolean add(E element) {
		Node<E> newNode = new Node<E>(element);
		tail.prev().setNext(newNode);
		newNode.setPrev(tail.prev());
		newNode.setNext(tail);
		tail.setPrev(newNode);
		size++;
		return true;
	}
	
	public void add(int index, E element) {
		Node<E> newNode = new Node<E>(element);
		if (!inBounds(index))
			return;
		Node<E> after = getNode(index);
		Node<E> before = after.prev();
		before.setNext(newNode);
		newNode.setPrev(before);
		newNode.setNext(after);
		after.setPrev(newNode);
		size++;
	}

	public int getIndex(E element){
		for (int i=0; i<size; i++){
			if (element.equals(get(i))){
				return i;
			}
		}
		return -1;
	}
	
	public E get(int index) {
		if (getNode(index) == null)
			return null;
		return getNode(index).get();
	}
	
	public E remove(int index) {
		if (!inBounds(index))
			return null;
		Node<E> result = getNode(index);
		Node<E> before = result.prev();
		Node<E> after = result.next();
		before.setNext(after);
		after.setPrev(before);
		size--;
		return result.get();
	}
	
	public boolean remove(E element) {
		Node<E> oldNode = null;
		Node<E> current = head.next();
		for (int i = 0; i < size; i++) {
			if (element.equals(current.get())) {
				oldNode = current;
				break;
			}
			current = current.next();
		}
		if (oldNode == null)
			return false;
		Node<E> before = oldNode.prev();
		Node<E> after = oldNode.next();
		before.setNext(after);
		after.setPrev(before);
		size--;
		return true;
	}
	
	public void set(int index, E element) {
		if (!inBounds(index))
			return;
		getNode(index).setData(element);
	}
	
	public void scramble() {
		for (int i = 0; i < size; i++) {
			int j = (int)(Math.random()*size);
			E temp = get(i);
			set(i, get(j));
			set(j, temp);
		}
	}
	
	public void empty() {
		head.setNext(tail);
		tail.setPrev(head);
		size = 0;
	}
	
	public int size() {
		return size;
	}
	
	public boolean isEmpty() {
		if (size == 0)
			return true;
		return false;
	}
	
	private boolean inBounds(int index) {
		if (index < 0 || index >= size)
			return false;
		return true;
	}
	
	public String toString() {
		String result = "";
		Node<E> current = head.next();
		for (int i = 0; i < size; i++) {
			result += current.get().toString() + "\n";
			current = current.next();
		}
		return result;
	}
	
	public int indexOf(E element) {
		Node<E> current = head.next();
        for (int i = 0; i < size; i++) {
            if (current.get().equals(element))
                return i;
            current = current.next();
        }
        return -1;
	}

	public boolean contains(E element){
		Node<E> current = head.next();
        for (int i = 0; i < size; i++) {
            if (current.get().equals(element))
                return true;
            current = current.next();
        }
        return false;
	}
	
}