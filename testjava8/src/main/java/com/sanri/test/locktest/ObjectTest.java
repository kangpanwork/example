package com.sanri.test.locktest;

import org.junit.Test;

public class ObjectTest {
    @Test
    public void testLink(){
        Node tail = new Node(1);
        Node t = tail;
        tail = null;
        System.out.println(t);
    }

    class Node {
        private int vlaue;
        private Node next;

        public Node(int vlaue) {
            this.vlaue = vlaue;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }
}
