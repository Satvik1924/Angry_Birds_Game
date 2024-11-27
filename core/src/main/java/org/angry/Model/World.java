package org.angry.Model;

import org.angry.view.Pig;
import org.angry.view.Shape;
import java.util.ArrayList;

public class World {

    public float dt;
    public int iterations;
    public ArrayList<Body> bodies = new ArrayList<>();
    public ArrayList<Pig> pigs = new ArrayList<>();
    public ArrayList<Handel> contacts = new ArrayList<>();
    private boolean launched = false; // Flag to track launch state

    public void setDt(float dt) {
        this.dt = dt;
    }

    public World(float dt, int iterations) {
        this.dt = dt;
        this.iterations = iterations;
    }

    public void setLaunched(boolean launched) {
        this.launched = launched; // Update the launch state
    }

    public void step() {
        contacts.clear();
        for (int i = 0; i < bodies.size(); ++i) {
            Body A = bodies.get(i);

            for (int j = i + 1; j < bodies.size(); ++j) {
                Body B = bodies.get(j);

                if (A.invMass == 0 && B.invMass == 0) {
                    continue;
                }

                Handel m = new Handel(A, B);
                m.solve();

                if (m.contactCount > 0) {
                    contacts.add(m);
                }
            }
        }

        for (Body body : bodies) {
            integrateForces(body, dt);
        }
        contacts.forEach(Handel::initialize);

        for (int j = 0; j < iterations; ++j) {
            contacts.forEach(Handel::applyImpulse);
        }

        for (Body body : bodies) {
            integrateVelocity(body, dt);
        }

        contacts.forEach(Handel::positionalCorrection);

        for (Body b : bodies) {
            b.force.set(0, 0);
            b.torque = 0;
        }
    }

    public Body add(Shape shape, int x, int y) {
        Body b = new Body(shape, x, y);
        bodies.add(b);
        return b;
    }

    public ArrayList<Pig> getPigs() {
        return pigs; // Return the list of pigs
    }

    public void clear() {
        contacts.clear();
        bodies.clear();
    }

    public void integrateForces(Body b, float dt) {
        if (b.invMass == 0.0f) {
            return;
        }

        float dts = dt * 0.5f;

        b.velocity.addsi(b.force, b.invMass * dts);
        if (launched) { // Apply gravity only if launched
            b.velocity.addsi(ImpulseMath.GRAVITY, dts);
        }
        b.angularVelocity += b.torque * b.invInertia * dts;
    }

    public void integrateVelocity(Body b, float dt) {
        if (b.invMass == 0.0f) {
            return;
        }

        b.position.addsi(b.velocity, dt);
        b.orient += b.angularVelocity * dt;
        b.setOrient(b.orient);
        integrateForces(b, dt);
    }

}