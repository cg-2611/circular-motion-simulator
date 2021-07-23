package circular.motion.simulator.utilities;

public class CircularQueue<T> {
    private T[] q;

    private int size;
    private int maxSize;

    private int front;
    private int rear;

    @SuppressWarnings("unchecked")
    public CircularQueue(int maxSize) {
        this.maxSize = maxSize;
        this.size = 0;
        this.front = 0;
        this.rear = -1;

        // assign an instance of a java object cast to an array of type T
        // to the q variable
        q = (T[]) new Object[this.maxSize];
    }

    public void enqueue(T item) {
        // if the queue is not full, calculate the new rear pointer, insert the
        // new item into the queue and increment the size of the queue
        // if the queue is full, dequeue the last item and insert the new one
        if (!isFull()) {
            rear = (rear + 1) % maxSize;
            q[rear] = item;
            size++;
        } else {
            dequeue();
            enqueue(item);
        }
    }

    public T dequeue() {
        T item = null;

        // if the queue is not empty, calculate the new front pointer and decrement
        // the size of the queue, note that the value is not overwritten or deleted
        if (!isEmpty()) {
            item = q[front];
            front = (front + 1) % maxSize;
            size--;
        }

        return item;
    }

    public T get(int index) {
        return q[index];
    }

    public int getFront() {
        return front;
    }

    public int getRear() {
        return rear;
    }

    public int size() {
        return size;
    }

    public boolean isFull() {
        return (size == maxSize);
    }

    public boolean isEmpty() {
        return (size == 0);
    }

}
