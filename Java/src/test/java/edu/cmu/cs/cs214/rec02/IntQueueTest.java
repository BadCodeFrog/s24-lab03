package edu.cmu.cs.cs214.rec02;

import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;


/**
 * TODO: 
 * 1. The {@link LinkedIntQueue} has no bugs. We've provided you with some example test cases.
 * Write your own unit tests to test against IntQueue interface with specification testing method 
 * using mQueue = new LinkedIntQueue();
 * 
 * 2. 
 * Comment `mQueue = new LinkedIntQueue();` and uncomment `mQueue = new ArrayIntQueue();`
 * Use your test cases from part 1 to test ArrayIntQueue and find bugs in the {@link ArrayIntQueue} class
 * Write more unit tests to test the implementation of ArrayIntQueue, with structural testing method
 * Aim to achieve 100% line coverage for ArrayIntQueue
 *
 * @author Alex Lockwood, George Guo, Terry Li
 */
public class IntQueueTest {

    private IntQueue mQueue;
    private List<Integer> testList;
    private static final int INITIAL_SIZE = 10;

    /**
     * Called before each test.
     */
    @Before
    public void setUp() {
        // comment/uncomment these lines to test each class
        // mQueue = new LinkedIntQueue();
        mQueue = new ArrayIntQueue();

        testList = new ArrayList<>(List.of(1, 2, 3));
    }

    @Test
    public void testIsEmpty() {
        // This is an example unit test
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testNotEmpty() {
        // After enqueue, the queue should not be empty
        mQueue.enqueue(1);
        assertFalse(mQueue.isEmpty());
    }

    @Test
    public void testPeekEmptyQueue() {
        // Without any enqueues, the queue should be empty
        // and peek should return null
        assertNull(mQueue.peek());
    }

    @Test
    public void testPeekNoEmptyQueue() {
        // After enqueue, the queue should not be empty
        // and peek should return the first element
        mQueue.enqueue(1);
        assertEquals(1, mQueue.peek().intValue());
    }

    @Test
    public void testEnqueue() {
        // This is an example unit test
        for (int i = 0; i < testList.size(); i++) {
            mQueue.enqueue(testList.get(i));
            assertEquals(testList.get(0), mQueue.peek());
            assertEquals(i + 1, mQueue.size());
        }
    }

    @Test
    public void testDequeue() {
        // Without any enqueues, the queue should be empty
        // and dequeue should return null
        assertNull(mQueue.dequeue());

        // After enqueue, the queue should contain the elements
        for (int i = 0; i < testList.size(); i++) {
            mQueue.enqueue(testList.get(i));
        }
        // After each dequeue, the queue should return the element at the head of the queue. 
        // Returns null if the queue is empty.
        for (int i = 0; i < testList.size(); i++) {
            assertEquals(testList.get(i), mQueue.dequeue());
            assertEquals(testList.size() - i - 1, mQueue.size());
            if (i != testList.size() - 1) {
                assertEquals(testList.get(i + 1), mQueue.peek());
            } else {
                assertNull(mQueue.peek());
            }
        }
    }

    @Test
    public void testContent() throws IOException {
        // This is an example unit test
        InputStream in = new FileInputStream("src/test/resources/data.txt");
        try (Scanner scanner = new Scanner(in)) {
            scanner.useDelimiter("\\s*fish\\s*");

            List<Integer> correctResult = new ArrayList<>();
            while (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                correctResult.add(input);
                System.out.println("enqueue: " + input);
                mQueue.enqueue(input);
            }

            for (Integer result : correctResult) {
                assertEquals(mQueue.dequeue(), result);
            }
        }
    }

    @Test
    public void testClear() {
        // After enqueue, the queue should contain the elements
        for (int i = 0; i < testList.size(); i++) {
            mQueue.enqueue(testList.get(i));
        }
        // After clear, the queue should be empty
        mQueue.clear();
        assertTrue(mQueue.isEmpty());
    }

    @Test
    public void testEnsureCapacity() {
        // Fill the queue to its initial capacity
        for (int i = 0; i < INITIAL_SIZE; i++) {
            mQueue.enqueue(i);
        }
        // Check size before exceeding capacity
        assertEquals(INITIAL_SIZE, mQueue.size());

        // Exceed the initial capacity
        mQueue.enqueue(INITIAL_SIZE);

        // Check size after exceeding capacity
        assertEquals(INITIAL_SIZE + 1, mQueue.size());

        for (int i = 0; i <= (2 * INITIAL_SIZE + 1); i++) {
            // Check if all elements are in correct order after resizing
            if (i <= INITIAL_SIZE) {
                assertEquals(Integer.valueOf(i), mQueue.dequeue());
            } else {
                // Check if the queue is still working after resizing
                assertNull(mQueue.dequeue());
            }
        }
    }

    @Test
    public void testWrapAround() {
        // Fill the queue to its initial capacity
        for (int i = 0; i < INITIAL_SIZE - 1; i++) {
            mQueue.enqueue(i);
        }

        // Dequeue some elements
        int numDequeues = 3;
        for (int i = 0; i < numDequeues; i++) {
            mQueue.dequeue();
        }

        // Enqueue some elements to trigger resizing and wrap around
        for (int i = INITIAL_SIZE - 1; i < INITIAL_SIZE + numDequeues; i++) {
            mQueue.enqueue(i);
        }

        // Check the size of the queue after resizing
        assertEquals(INITIAL_SIZE, mQueue.size());

        // for (int i = numDequeues; i <= (INITIAL_SIZE + 1); i++) {
        //     // Check if all elements are in correct order after resizing
        //     assertEquals(Integer.valueOf(i), mQueue.dequeue());
        // }

        for (int i = 0; i < INITIAL_SIZE - numDequeues; i++) {
            mQueue.dequeue();
        }
        for (int i = INITIAL_SIZE; i < INITIAL_SIZE + numDequeues; i++) {
            assertEquals(Integer.valueOf(i), mQueue.dequeue());
        }
    }
}
