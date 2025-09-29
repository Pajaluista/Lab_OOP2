package functions;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable {

    protected static class Node {
        public Node prev;
        public Node next;
        public double x;
        public double y;

        public Node(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    protected int count;
    protected Node head;

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException("Arrays must have the same length");
        }
        if (xValues.length < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }

        for (int i = 0; i < xValues.length; i++) {
            addNode(xValues[i], yValues[i]);
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }

        if (xFrom > xTo) {
            double temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }

        if (xFrom == xTo) {
            double y = source.apply(xFrom);
            for (int i = 0; i < count; i++) {
                addNode(xFrom, y);
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

    private void addNode(double x, double y) {
        Node newNode = new Node(x, y);

        if (head == null) {
            newNode.next = newNode;
            newNode.prev = newNode;
            head = newNode;
        } else {
            Node last = head.prev;
            last.next = newNode;
            newNode.prev = last;
            newNode.next = head;
            head.prev = newNode;
        }
        count++;
    }

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

    // Реализация TabulatedFunction методов
    @Override public int getCount() { return count; }
    @Override public double getX(int index) { return getNode(index).x; }
    @Override public double getY(int index) { return getNode(index).y; }
    @Override public void setY(int index, double value) { getNode(index).y = value; }
    @Override public double leftBound() { return head.x; }
    @Override public double rightBound() { return head.prev.x; }

    @Override
    public int indexOfX(double x) {
        Node current = head;
        for (int i = 0; i < count; i++) {
            if (Math.abs(current.x - x) < 1e-10) return i;
            current = current.next;
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        Node current = head;
        for (int i = 0; i < count; i++) {
            if (Math.abs(current.y - y) < 1e-10) return i;
            current = current.next;
        }
        return -1;
    }

    // Реализация методов интерполяции
    @Override
    protected int floorIndexOfX(double x) {
        if (x < head.x) return 0;
        Node current = head;
        for (int i = 0; i < count; i++) {
            if (x < current.x) return i - 1;
            if (Math.abs(x - current.x) < 1e-10) return i;
            current = current.next;
        }
        return count - 2;
    }

    @Override protected double extrapolateLeft(double x) {
        return interpolate(x, head.x, head.y, head.next.x, head.next.y);
    }

    @Override protected double extrapolateRight(double x) {
        Node last = head.prev;
        return interpolate(x, last.prev.x, last.prev.y, last.x, last.y);
    }

    @Override protected double interpolate(double x, int floorIndex) {
        Node left = getNode(floorIndex);
        Node right = getNode(floorIndex + 1);
        return interpolate(x, left.x, left.y, right.x, right.y);
    }

    protected double interpolate(double x, double leftX, double leftY, double rightX, double rightY) {
        return leftY + (rightY - leftY) * (x - leftX) / (rightX - leftX);
    }

    // Реализация Insertable
    @Override
    public void insert(double x, double y) {
        int existingIndex = indexOfX(x);
        if (existingIndex != -1) {
            setY(existingIndex, y);
            return;
        }
        int insertIndex = findInsertIndex(x);
        insertNode(insertIndex, x, y);
    }

    private int findInsertIndex(double x) {
        if (head == null || x < head.x) return 0;
        Node current = head;
        for (int i = 0; i < count; i++) {
            if (x < current.x) return i;
            current = current.next;
        }
        return count;
    }

    private void insertNode(int index, double x, double y) {
        Node newNode = new Node(x, y);
        if (head == null) {
            newNode.next = newNode;
            newNode.prev = newNode;
            head = newNode;
        } else if (index == 0) {
            Node last = head.prev;
            newNode.next = head;
            newNode.prev = last;
            head.prev = newNode;
            last.next = newNode;
            head = newNode;
        } else if (index == count) {
            Node last = head.prev;
            newNode.next = head;
            newNode.prev = last;
            last.next = newNode;
            head.prev = newNode;
        } else {
            Node current = getNode(index);
            Node previous = current.prev;
            newNode.next = current;
            newNode.prev = previous;
            previous.next = newNode;
            current.prev = newNode;
        }
        count++;
    }

    // РЕАЛИЗАЦИЯ REMOVABLE - НОВЫЙ МЕТОД
    @Override
    public void remove(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Index out of bounds: " + index + ", count: " + count);
        }

        if (count <= 2) {
            throw new IllegalStateException("Cannot remove point: minimum 2 points required");
        }

        removeNode(index);
    }

    private void removeNode(int index) {
        Node nodeToRemove = getNode(index);

        // Получаем соседние узлы
        Node prevNode = nodeToRemove.prev;
        Node nextNode = nodeToRemove.next;

        // Связываем соседние узлы между собой, исключая удаляемый
        prevNode.next = nextNode;
        nextNode.prev = prevNode;

        // Если удаляем голову, обновляем ссылку на голову
        if (nodeToRemove == head) {
            head = nextNode;
        }

        // Очищаем ссылки удаляемого узла
        nodeToRemove.prev = null;
        nodeToRemove.next = null;

        count--;
    }

    @Override
    public String toString() {
        if (head == null) return "LinkedListTabulatedFunction[empty]";
        StringBuilder sb = new StringBuilder();
        sb.append("LinkedListTabulatedFunction[");
        Node current = head;
        for (int i = 0; i < count; i++) {
            if (i > 0) sb.append(", ");
            sb.append("(").append(current.x).append("; ").append(current.y).append(")");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}