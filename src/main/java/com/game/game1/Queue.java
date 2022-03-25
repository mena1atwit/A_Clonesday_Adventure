package com.game.game1;
import javafx.scene.image.ImageView;

import java.io.*;
import java.util.*;
public class Queue<T>
{
    private T[] queue;
    int frontIndex = -1;
    int backIndex = -1;
    private static final int DEFAULT_CAPACITY = 1000;
    private static final int MAX_CAPACITY = 1000000;

    public Queue()
    {
        this(DEFAULT_CAPACITY);
    }

    public Queue(int initCap)
    {
        this.queue = (T[]) new Object[initCap + 1];
        frontIndex = 0;
        backIndex = initCap;
    }

    public void add(T newEntry)
    {
        //ensureCapacity();
        backIndex = (backIndex + 1) % queue.length;
        queue[backIndex] = newEntry;
    }

    public T remove()
    {
        if(isEmpty())
        {

        }
        else
        {
            T front = queue[frontIndex];
            queue[frontIndex] = null;
            frontIndex = (frontIndex + 1) % queue.length;
            return front;
        }
        return null;
    }

    public boolean isEmpty() {
        return frontIndex == ((backIndex + 1) % queue.length);
    }
}