package circular.motion.simulator.utilities;

public class Calculator {

    public static double calculateAngularVelocity(double T) {
        // uses the equation w = 2Pi / T to calculate a value for w
        double w = (2 * Math.PI) / (T / 1000);
        return w;
    }

    public static double calculateLinearVelocity(double orbitRadius, double T) {
        // uses the equation v = wr and w = 2Pi / T to calculate a value for v
        double v = ((2 * Math.PI) / T) * orbitRadius;
        return v;
    }

    public static double calculateCentripetalForce(double m, double v, double r) {
        // uses the equation F = mv^2 / r to calculate a value for F
        double F = (m * (v * v)) / (r / 100);
        return F;
    }

    public static double calculateCentripetalAcceleration(double v, double r) {
        // uses the equation a = v^2 / r to calculate a value for a
        double a = (v * v) / (r / 100);
        return a;
    }

    public static double calculateFrequency(double T) {
        // uses the equation f = 1 / T to calculate a value for f
        double f = 1 / (T / 1000);
        return f;
    }

}
