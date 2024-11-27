package org.angry.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorTest {

    private Vector vector1;
    private Vector vector2;
    private Vector resultVector;

    @BeforeEach
    public void setUp() {
        // Initialize vectors
        vector1 = new Vector(3.0f, 4.0f); // Example vector
        vector2 = new Vector(1.0f, 2.0f); // Another example vector
        resultVector = new Vector();
    }

    @Test
    public void testVectorAdd() {
        // Test vector addition (vector1 + vector2)
        vector1.addi(vector2);

        // Assert the expected result of vector addition
        assertEquals(4.0f, vector1.x, "Expected x to be 4.0f");
        assertEquals(6.0f, vector1.y, "Expected y to be 6.0f");
    }

    @Test
    public void testVectorNormalize() {
        // Test vector normalization
        vector1.normalize();

        // Assert that the vector is normalized
        assertEquals(0.6f, vector1.x, 0.01, "Expected x to be normalized");
        assertEquals(0.8f, vector1.y, 0.01, "Expected y to be normalized");
    }
}

