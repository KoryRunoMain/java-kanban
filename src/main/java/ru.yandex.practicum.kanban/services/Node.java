package ru.yandex.practicum.kanban.services;

public class Node<E> {
    protected E data;
    protected Node<E> prev;
    protected Node<E> next;

    public Node(Node<E> prev, E data, Node<E> next) {
        this.data = data;
        this.prev = prev;
        this.next = next;
    }

}
