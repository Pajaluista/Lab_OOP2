//package functions;
//
//public class ArrayTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable {
//    private double[] xValues;
//    private double[] yValues;
//
//    // Существующие конструкторы и методы...
//
//    @Override
//    public void remove(int index) {
//        if (index < 0 || index >= getCount()) {
//            throw new IllegalArgumentException("Invalid index: " + index);
//        }
//
//        if (getCount() <= 2) {
//            throw new IllegalStateException("Cannot remove point: minimum 2 points required");
//        }
//
//        // Создаем новые массивы на 1 элемент меньше
//        double[] newXValues = new double[xValues.length - 1];
//        double[] newYValues = new double[yValues.length - 1];
//
//        // Копируем элементы до удаляемого индекса
//        System.arraycopy(xValues, 0, newXValues, 0, index);
//        System.arraycopy(yValues, 0, newYValues, 0, index);
//
//        // Копируем элементы после удаляемого индекса
//        System.arraycopy(xValues, index + 1, newXValues, index, xValues.length - index - 1);
//        System.arraycopy(yValues, index + 1, newYValues, index, yValues.length - index - 1);
//
//        // Заменяем старые массивы новыми
//        xValues = newXValues;
//        yValues = newYValues;
//    }
//}