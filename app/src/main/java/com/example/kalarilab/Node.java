package com.example.kalarilab;

import android.app.FragmentManager;

import androidx.fragment.app.Fragment;

public class Node{
    private androidx.fragment.app.Fragment value;
    private Node next;

    public Node(androidx.fragment.app.Fragment value) {
        this.value = value;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getNext() {
        return next;
    }

    public Fragment getValue() {
        return value;
    }

}
