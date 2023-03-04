
public class Queue {
	
	 private Object[] elements;
	    private int front;
	    private int rear;
	    private boolean activated;
	    private int capacity;



	    public Queue(int capacity) {
	        elements = new Object[capacity];
	        front = 0;
	        rear = - 1;
	        activated = true;
	        this.capacity = capacity;
	    }
	    public Object dequeue() {
	        if(isEmpty()) {
	            System.out.println("Queue is empty.");
	            return null;
	        }
	        else {
	            Object retData = elements[front];
	            elements[front] = null;
	            front++;
	            return retData;
	        }
	    }
	    public void enqueue(Object data) {
	        if(isFull()) {
	            System.out.println("Queue is overflow.");
	        }
	        else {
	            rear++;
	            elements[rear] = data;
	        }
	    }
	    public Object peek() {
	        if(isEmpty()) {
	            System.out.println("Queue is empty.");
	            return null;
	        }
	        else {
	            return elements[front];
	        }
	    }
	    public boolean isEmpty() {
	        return (front == rear + 1);
	    }
	    public boolean isFull() {
	        return(rear + 1 == elements.length);
	    }
	    public int getSize() {
	        return rear - front + 1;
	    }
	    public int getCapacity() {
	        return capacity;
	    }

	    public boolean isActivated() {
	        return activated;
	    }


}
