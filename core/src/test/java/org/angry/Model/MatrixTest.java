package org.angry.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatrixTest {

    private Matrix matrix1;
    private Matrix matrix2;
    private Matrix resultMatrix;

    @BeforeEach
    public void setUp() {
        // Initialize matrices
        matrix1 = new Matrix(1.0f, 2.0f, 3.0f, 4.0f); // Example matrix
        matrix2 = new Matrix(5.0f, 6.0f, 7.0f, 8.0f); // Another example matrix
        resultMatrix = new Matrix();
    }

    @Test
    public void testMatrixSet() {
        // Test that the set method works correctly
        matrix1.set(2.0f, 3.0f, 4.0f, 5.0f);

        // Assert that the values are correctly set in matrix1
        assertEquals(2.0f, matrix1.m00, "Matrix value m00 should be 2.0f");
        assertEquals(3.0f, matrix1.m01, "Matrix value m01 should be 3.0f");
        assertEquals(4.0f, matrix1.m10, "Matrix value m10 should be 4.0f");
        assertEquals(5.0f, matrix1.m11, "Matrix value m11 should be 5.0f");
    }

    @Test
    public void testMatrixMultiplication() {
        // Test matrix multiplication
        matrix1.mul(matrix2, resultMatrix);

        // Assert the values of the result matrix after multiplication
        assertEquals(19.0f, resultMatrix.m00, "Expected m00 to be 19.0f (1*5 + 2*7)");
        assertEquals(22.0f, resultMatrix.m01, "Expected m01 to be 22.0f (1*6 + 2*8)");
        assertEquals(43.0f, resultMatrix.m10, "Expected m10 to be 43.0f (3*5 + 4*7)");
        assertEquals(50.0f, resultMatrix.m11, "Expected m11 to be 50.0f (3*6 + 4*8)");
    }
}

