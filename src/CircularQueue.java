
public class CircularQueue {

	private int rear;
	private int front;
	private Object[]elements;
	private boolean activated;
	private int capacity;
	
	public boolean isActivated() {
		return activated;
	}
	
	//Constructor
	public CircularQueue(int capacity) {
		elements=new Object[capacity];
		rear=-1;
		front=0;
		activated=true;
		this.setCapacity(capacity);
	}
	
	//Function of enqueue
	void enqueue(Object data) {
        if (isFull())
            System.out.println("Queue overflow!!");
        else {
            rear = (rear + 1) % elements.length;
            elements[rear] = data;
        }
    }
	
	Object dequeue() {
        if (isEmpty()) {
            return null;
        } else {
            Object retData = elements[front];
            elements[front] = null;
            front = (front + 1) % elements.length;
            return retData;
        }
    }
	
	Object peek() {
        if (isEmpty()) {
            System.out.println("Queue is empty!!");
            return null;
        } else {
            return elements[front];
        }
    }
	
	boolean isEmpty() {
        return elements[front] == null;
    }
	
	boolean isFull() {
        return (front == (rear + 1) % elements.length) && elements[front] != null && elements[rear] != null;
    }
	
	int size() {
        if (isEmpty())
            return 0;
        if (rear >= front) {
            return rear - front + 1;
        } else if (elements[front] != null) {
            return elements.length - (front - rear) + 1;
        } else {
            return 0;
        }
    }
	
	 public Object[] getElements(){
	        return elements;
	    }
	 
	 public Object clone() throws CloneNotSupportedException
	    {
	        return super.clone();
	    }

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	
}
