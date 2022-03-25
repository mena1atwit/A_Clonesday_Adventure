package com.game.game1;

import java.util.Arrays;
import java.util.EmptyStackException;


public class ArrayStack<T> implements StackInterface<T>
{
    private T[] stack;    // Array of stack entries
    private int topIndex; // Index of top entry
    private boolean initialized = false;
    private static final int DEFAULT_CAPACITY = 50;
    private static final int MAX_CAPACITY = 10000;

    public ArrayStack()
    {
        this(DEFAULT_CAPACITY);
    } //default constructor

    public ArrayStack(int initialCapacity)
    {
        checkCapacity(initialCapacity);

        @SuppressWarnings("unchecked")
        T[] tempStack = (T[])new Object[initialCapacity];
        stack = tempStack;
        topIndex = -1;
        initialized = true;
    }
    private void checkInitialization()
    {
        if (!initialized)
            throw new SecurityException("ArrayBag object is not initialized properly.");
    }

    private void checkCapacity(int capacity)
    {
        if (capacity > MAX_CAPACITY)
            throw new IllegalStateException("Attempt to create a bag whose " +
                    "capacity exeeds allowed " +
                    "maximum of " + MAX_CAPACITY);
    }

    public void push(T newEntry)
    {
        checkInitialization();
        ensureCapacity();
        stack[topIndex + 1] = newEntry;
        topIndex++;
    }

    private void ensureCapacity()
    {
        if (topIndex >= stack.length - 1) // If array is full, double its size
        {
            int newLength = 2 * stack.length;
            checkCapacity(newLength);
            stack = Arrays.copyOf(stack, newLength);
        }
    }

    public T pop()
    {
        checkInitialization();
        if (isEmpty())
            throw new EmptyStackException();
        else
        {
            T top = stack[topIndex];
            stack[topIndex] = null;
            topIndex--;
            return top;
        }
    }

    public T peek()
    {
        checkInitialization();
        if (isEmpty())
            throw new EmptyStackException();
        else
            return stack[topIndex];
    }

    public boolean isEmpty()
    {
        return topIndex < 0;
    }

    public void clear() {
        while(!isEmpty()) {
            pop();
        }

    }

    public String toString(){
        // ADD YOUR CODE HERE
        return null;

    }

} // end ArrayStack
