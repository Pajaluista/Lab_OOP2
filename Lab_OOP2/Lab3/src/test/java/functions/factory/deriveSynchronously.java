package functions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TabulatedDifferentialOperatorTest {

    @Test
    public void testDeriveSynchronouslyWithRegularFunction() {
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(new UnitFunction(), 0, 10, 11);
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();

        TabulatedFunction result = operator.deriveSynchronously(baseFunction);
        assertNotNull(result);
        // Дополнительные проверки производной
    }

    @Test
    public void testDeriveSynchronouslyWithSynchronizedFunction() {
        TabulatedFunction baseFunction = new LinkedListTabulatedFunction(new UnitFunction(), 0, 10, 11);
        SynchronizedTabulatedFunction syncFunction = new SynchronizedTabulatedFunction(baseFunction);
        TabulatedDifferentialOperator operator = new TabulatedDifferentialOperator();

        TabulatedFunction result = operator.deriveSynchronously(syncFunction);
        assertNotNull(result);

    }
}