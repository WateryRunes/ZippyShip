package utilities;

// mutable 2D vectors
public final class Vector2D {
    public double x, y;

    // constructor for zero vector
    public Vector2D() {
        x = 0;
        y = 0;
    }

    // constructor for vector with given coordinates
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // constructor that copies the argument vector
    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    // set coordinates
    public Vector2D set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    // set coordinates based on argument vector
    public Vector2D set(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    // compare for equality (note Object type argument)
    public boolean equals(Object o) {
        if(o instanceof Vector2D){
            return this.x == ((Vector2D) o).x && this.y == ((Vector2D) o).y;
        }
        return false;
    }

    // String for displaying vector as text
    public String toString() {
        return "Vector2D (" + x + ", " + y + ")";
    }

    //  magnitude (= "length") of this vector
    public double mag() {
        return Math.sqrt((x*x) + (y*y));
    }

    // angle between vector and horizontal axis in radians in range [-PI,PI]
// can be calculated using Math.atan2
    public double angle() {
        return Math.atan2(this.y, this.x);
    }

    // angle between this vector and another vector in range [-PI,PI]
    public double angle(Vector2D other) {
        double result = other.angle() - this.angle();
        if(result < -Math.PI){
            result += 2 * Math.PI;
        }
        else if(result > Math.PI){
            result -= 2 * Math.PI;
        }
        return result;
    }

    // add argument vector
    public Vector2D add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    // add values to coordinates
    public Vector2D add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    // weighted add - surprisingly useful
    public Vector2D addScaled(Vector2D v, double fac) {
        this.x += v.x * fac;
        this.y += v.y * fac;
        return this;
    }

    // subtract argument vector
    public Vector2D subtract(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    // subtract values from coordinates
    public Vector2D subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    // multiply with factor
    public Vector2D mult(double fac) {
        this.x *= fac;
        this.y *= fac;
        return this;
    }

    // rotate by angle given in radians
    public Vector2D rotate(double angle) {
        double newx = x * Math.cos(angle) - y * Math.sin(angle);
        double newy = x * Math.sin(angle) + y * Math.cos(angle);
        this.x = newx;
        this.y = newy;
        return this;
    }

    // "dot product" ("scalar product") with argument vector
    public double dot(Vector2D v) {
        return ((this.x * v.x) + (this.y * v.y));
    }

    // distance to argument vector
    public double dist(Vector2D v) {
        return Math.sqrt(Math.pow(this.x - v.x, 2) + Math.pow(this.y - v.y, 2));
    }

    // normalise vector so that magnitude becomes 1
    public Vector2D normalise() {
        double mag = this.mag();
        this.x /= mag;
        this.y /= mag;
        return this;
    }

    // wrap-around operation, assumes w> 0 and h>0
// remember to manage negative values of the coordinates
    public Vector2D wrap(double w, double h) {
        if(this.x > w){
            this.x -= w;
        }
        else if(this.x < 0){
            this.x += w;
        }
        if(this.y > h){
            this.y -= h;
        }
        else if(this.y < 0){
            this.y += h;
        }
        return this;
    }

    public Vector2D verticalWrap(double w, double h){
        if(this.x > w){
            this.x = w;
        }
        else if(this.x < 0){
            this.x = 0;
        }
        if(this.y > h){
            this.y -= h;
        }
        else if(this.y < 0){
            this.y += h;
        }
        return this;
    }

    // construct vector with given polar coordinates
    public static Vector2D polar(double angle, double mag) {
        return new Vector2D(mag * Math.cos(angle), mag * Math.sin(angle));
    }
}