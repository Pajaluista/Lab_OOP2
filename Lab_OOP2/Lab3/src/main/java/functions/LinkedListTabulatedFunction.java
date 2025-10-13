package functions;

import functions.factory.TabulatedFunctionFactory;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction
        implements Insertable, Removable, Serializable {


    private static final long serialVersionUID = 1L;//Это уникальный идентификатор версии класса, используемый при сериализации и десериализации.

    protected Node head;//ссылка на первый узел списка.
    private int count;//— количество точек в функции.

    // Вложенный статический класс Node также должен быть Serializable
    static class Node implements Serializable {//Это один элемент списка

        private static final long serialVersionUID = 1L;

        public Node next;
        public Node prev;
        public double x;
        public double y;
    }


    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {//Создаёт функцию из массивов x и y
        AbstractTabulatedFunction.checkLengthIsTheSame(xValues, yValues);

        if (xValues.length < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }

        AbstractTabulatedFunction.checkSorted(xValues);

        this.count = 0;
        for (int i = 0; i < xValues.length; i++) {
            addNode(xValues[i], yValues[i]);
        }
    }
//Создаёт функцию, дискретизируя другую функцию source.
//Равномерно разбивает интервал [xFrom, xTo] на count точек.
//Вычисляет y, вызывая source.apply(x).
    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }

        this.count = 0;

        if (xFrom > xTo) {
            double temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }

        if (Math.abs(xFrom - xTo) < 1e-10) {
            double value = source.apply(xFrom);
            for (int i = 0; i < count; i++) {
                addNode(xFrom, value);
            }
        } else {
            double step = (xTo - xFrom) / (count - 1);
            for (int i = 0; i < count; i++) {
                double x = xFrom + i * step;
                double y = source.apply(x);
                addNode(x, y);
            }
        }
    }

    // Приватный метод добавления узла
    //Создаёт новый узел (Node) с заданными x и y
    private void addNode(double x, double y) {
        Node newNode = new Node();
        newNode.x = x;
        newNode.y = y;

        if (head == null) {
            head = newNode;
            head.next = head;
            head.prev = head;
        } else {
            Node last = head.prev;
            last.next = newNode;
            newNode.prev = last;
            newNode.next = head;
            head.prev = newNode;
        }
        count++;
    }

    // Приватный метод получения узла по индексу
    //Находит узел по заданному индексу.
    protected Node getNode(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }

        if (index <= count / 2) {
            Node current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {
            Node current = head.prev;
            for (int i = count - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }


    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double getX(int index) {
        return getNode(index).x;
    }

    @Override
    public double getY(int index) {
        return getNode(index).y;
    }

    @Override
    public void setY(int index, double value) {
        getNode(index).y = value;
    }

    @Override
    public int indexOfX(double x) {
        Node current = head;
        for (int i = 0; i < count; i++) {
            if (Math.abs(current.x - x) < 1e-10) {
                return i;
            }
            current = current.next;
        }
        return -1;
    }

    @Override
    //Ищет индекс точки, у которой y совпадает с заданным y
    public int indexOfY(double y) {
        Node current = head;//Создаёт переменную current, которая указывает на первый узел списка.
        for (int i = 0; i < count; i++) {
            if (Math.abs(current.y - y) < 1e-10) {
                return i;
            }
            current = current.next;
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return head.x;
    }

    @Override
    public double rightBound() {
        return head.prev.x;
    }

    @Override
    // в каком интервале находится x
    protected int floorIndexOfX(double x) {
        if (x < head.x) {
            throw new IllegalArgumentException("x = " + x + " is less than left bound " + head.x);
        }

        Node current = head;
        for (int i = 0; i < count; i++) {
            if (x < current.x) {
                return i - 1;
            }
            current = current.next;
        }
        return count - 1;
    }

    @Override
    protected double extrapolateLeft(double x) {
        return interpolate(x, head.x, head.next.x, head.y, head.next.y);
    }

    @Override
    protected double extrapolateRight(double x) {
        Node last = head.prev;
        Node prevLast = last.prev;
        return interpolate(x, prevLast.x, last.x, prevLast.y, last.y);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {//floorIndex — индекс узла, слева от x
        Node leftNode = getNode(floorIndex);
        Node rightNode = leftNode.next;
        return interpolate(x, leftNode.x, rightNode.x, leftNode.y, rightNode.y);
    }

    @Override
    //Вставляет точку (x, y) в нужное место списка.
    //Если x уже есть, меняет y.
    //Если x новый, вставляет узел в нужное место.
    public void insert(double x, double y) {
        // Начинаем с head
        if (head == null) {
            addNode(x, y);
            return;
        }

        // Проверяем, может быть x уже есть?
        Node current = head;
        do {
            if (Math.abs(current.x - x) < 1e-10) {
                current.y = y; // заменяем y
                return;
            }
            if (x < current.x) {
                // Нашли место для вставки
                insertBefore(current, x, y);
                return;
            }
            current = current.next;
        } while (current != head);

        // Если дошли до конца, вставляем в конец
        addNode(x, y);
    }

    // Вспомогательный метод для вставки перед узлом
    private void insertBefore(Node node, double x, double y) {
        Node newNode = new Node();
        newNode.x = x;
        newNode.y = y;

        Node prev = node.prev;
        prev.next = newNode;
        newNode.prev = prev;
        newNode.next = node;
        node.prev = newNode;

        if (node == head) {
            head = newNode; // если вставили перед head
        }
        count++;
    }

    @Override
    //Удаляет узел по заданному индексу
    //Обновляет связи между узлами, чтобы список остался целым.
    public void remove(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Invalid index: " + index);
        }

        if (count <= 2) {
            throw new IllegalStateException("Cannot remove point: minimum 2 points required");
        }

        Node toRemove = getNode(index);

        if (toRemove == head) {
            head = head.next;
        }

        toRemove.prev.next = toRemove.next;
        toRemove.next.prev = toRemove.prev;

        count--;
    }

    @Override
    //Создаёт анонимный класс, реализующий интерфейс Iterator<Point>.
    //Позволяет перебирать все точки (x, y) функции по одной.
    //н возвращает объект, который умеет:
    //Сказать, есть ли ещё одна точка (hasNext).
    //Вернуть следующую точку (next).
    public Iterator<Point> iterator() {
        return new Iterator<Point>() {
            private Node currentNode = head;
            private int returnedCount = 0;

            @Override
            public boolean hasNext() {
                return returnedCount < count;
            }

            @Override
            public Point next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements in the tabulated function");
                }

                Point point = new Point(currentNode.x, currentNode.y);
                currentNode = currentNode.next;
                returnedCount++;
                return point;
            }
        };
    }
}